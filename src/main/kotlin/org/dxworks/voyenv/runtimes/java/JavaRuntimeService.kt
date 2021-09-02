package org.dxworks.voyenv.runtimes.java

import com.google.api.client.http.GenericUrl
import com.google.common.reflect.TypeToken
import com.vdurmont.semver4j.Semver
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.dxworks.utils.java.rest.client.HttpClient
import org.dxworks.voyenv.config.RuntimeConfig
import org.dxworks.voyenv.runtimes.RuntimeService
import org.dxworks.voyenv.utils.decompressTo
import org.dxworks.voyenv.utils.logger
import org.dxworks.voyenv.utils.makeScriptExecutable
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream


class JavaRuntimeService(runtimesDir: File) : RuntimeService("java", runtimesDir) {

    companion object {
        private val logger = logger<JavaRuntimeService>()
    }

    private val httpClient = HttpClient()

    override fun download(config: RuntimeConfig): Pair<String, String> {
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
        val binaryToDownload = jreBinary ?: bestMatchedResponse.binaries!![0]


        val downloadLink = binaryToDownload.packageData!!.link

        logger.info("Downloading Java form $downloadLink")
        val res = httpClient.get(GenericUrl(downloadLink))

        val targetDir = runtimesDir.resolve(config.platform)

        getArchiveInputStream(res.content)
            .decompressTo(targetDir)

        return "java-${semver.major}" to (getJavaBinary(targetDir)?.also { makeScriptExecutable(it) }?.let { it.absolutePath } ?: "")
    }

    private fun getArchiveInputStream(responseInputStream: InputStream): ArchiveInputStream =
        TarArchiveInputStream(GzipCompressorInputStream(BufferedInputStream(responseInputStream)))

    private fun getJavaBinary(targetDir: File): File? {
        return targetDir.walkTopDown()
            .filter { it.name == "java" }
            .firstOrNull()
    }
}
