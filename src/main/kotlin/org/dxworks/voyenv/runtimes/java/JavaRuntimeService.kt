package org.dxworks.voyenv.runtimes.java

import com.google.common.reflect.TypeToken
import com.vdurmont.semver4j.Semver
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.dxworks.utils.java.rest.client.HttpClient
import org.dxworks.voyenv.Progress
import org.dxworks.voyenv.ProgressWriter
import org.dxworks.voyenv.config.RuntimeConfig
import org.dxworks.voyenv.runtimes.ExecutableSymlink
import org.dxworks.voyenv.runtimes.RuntimeService
import org.dxworks.voyenv.utils.FilesDownloader
import org.dxworks.voyenv.utils.OS.WINDOWS
import org.dxworks.voyenv.utils.decompressTo
import org.dxworks.voyenv.utils.logger
import java.io.File
import java.io.InputStream


class JavaRuntimeService(runtimesDir: File) : RuntimeService("java", runtimesDir) {

    companion object {
        private val log = logger<JavaRuntimeService>()
    }

    private val httpClient = HttpClient()

    override fun download(config: RuntimeConfig, progressWriter: ProgressWriter?): List<ExecutableSymlink> {
        progressWriter?.update(name, Progress("Searching for package"))
        val downloadLink = getDownloadLink(config)

        if (downloadLink == null) {
            progressWriter?.update(name, Progress("Could not find download link for $name"))
            log.error("Could not find download link for $name: $config")
            return emptyList()
        }

        log.info("Downloading $name from $downloadLink")
        progressWriter?.update(name, Progress("Downloading"))

        val inputStream =
            FilesDownloader().downloadFile(downloadLink, progressWriter = progressWriter, progressWriterId = name)
        return if (inputStream != null) {
            log.info("$name Download Finished")
            log.info("$name Unzipping")
            progressWriter?.update(name, Progress("Unzipping...", total = -1))

            val targetDir = runtimesDir.resolve(config.platform)
            getArchiveInputStream(config, inputStream).decompressTo(targetDir)

            log.info("$name Unzip Finished")
            log.info("$name Extracting executables")
            progressWriter?.update(name, Progress("Extracting executables...", total = -1))
            val executableSymlinks = getJavaBinary(targetDir)?.absolutePath
                ?.let { listOf(ExecutableSymlink("java", it)) } ?: emptyList()

            log.info("$name Finished")
            progressWriter?.update(name, Progress("Finished", total = -1))

            executableSymlinks
        } else {
            progressWriter?.update(name, Progress("Failed", total = -1))
            log.error("$name Download Failed")
            emptyList()
        }
    }

    private fun getDownloadLink(config: RuntimeConfig): String? {
        val semver = Semver(config.version, Semver.SemverType.LOOSE)
        val imageType = config.type.ifBlank { "jre" }

        val response = httpClient.get(
            AdoptRestUrl(
                semver.major.toString(),
                config.arch,
                config.platform,
                config.distribution.ifBlank { "hotspot" }
            )
        )
        val adoptResponses =
            response.parseAs(object : TypeToken<List<AdoptRestResponse>>() {}.type) as List<AdoptRestResponse>

        val bestMatchedResponse = adoptResponses
            .filter { Semver(it.versionData!!.semver, Semver.SemverType.LOOSE).isGreaterThanOrEqualTo(semver) }
            .sortedWith { a, b ->
                -Semver(
                    a.versionData!!.semver,
                    Semver.SemverType.LOOSE
                ).compareTo(Semver(b.versionData!!.semver, Semver.SemverType.LOOSE))
            }
            .first()


        val jreBinary: AdoptBinariesResponse? = bestMatchedResponse.binaries?.find { it.imageType == imageType }
            ?: bestMatchedResponse.binaries?.find { it.imageType == "jdk" }
        val binaryToDownload = jreBinary ?: bestMatchedResponse.binaries!![0]


        return binaryToDownload.packageData!!.link
    }

    private fun getArchiveInputStream(config: RuntimeConfig, inputStream: InputStream): ArchiveInputStream =
        when (config.platform) {
            WINDOWS -> ZipArchiveInputStream(inputStream)
            else -> TarArchiveInputStream(GzipCompressorInputStream(inputStream))
        }

    private fun getJavaBinary(targetDir: File): File? {
        return targetDir.walkTopDown()
            .filter { it.name == "java" || it.name == "java.exe" }
            .firstOrNull()
    }
}
