package org.dxworks.voyenv.runtimes.pyhton

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.luben.zstd.ZstdInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.dxworks.githubminer.service.repository.releases.GithubReleasesService
import org.dxworks.voyenv.Progress
import org.dxworks.voyenv.ProgressWriter
import org.dxworks.voyenv.config.RuntimeConfig
import org.dxworks.voyenv.runtimes.ExecutableSymlink
import org.dxworks.voyenv.runtimes.RuntimeService
import org.dxworks.voyenv.utils.FilesDownloader
import org.dxworks.voyenv.utils.decompressTo
import org.dxworks.voyenv.utils.logger
import java.io.File

class PythonRuntimeService(runtimesDir: File) : RuntimeService("python", runtimesDir) {

    val allStandaloneReleases by lazy {
        GithubReleasesService("indygreg", "python-build-standalone").getReleases()
    }

    override fun download(config: RuntimeConfig, progressWriter: ProgressWriter?): List<ExecutableSymlink> {
        progressWriter?.update(name, Progress("Searching for package"))
        val availableVersions = allStandaloneReleases.flatMap { it.assets.orEmpty() }

        val versionsMap =
            availableVersions.filter { it.name != null }.associateBy { PythonStandaloneAssetMetaInfo(it.name!!) }

        val downloadLink = versionsMap.keys.sortedByDescending { it.timestamp }
            .find { it.version == config.version && it.platform == config.platform && it.arch == config.arch }
            ?.let { versionsMap[it]!!.browserDownloadUrl }

        if (downloadLink == null) {
            progressWriter?.update(name, Progress("Could not find download link for $name"))
            log.error("Could not find download link for $name: $config")
            return emptyList()
        }

        log.info("Downloading $name form $downloadLink")
        progressWriter?.update(name, Progress("Downloading"))

        val inputStream =
            FilesDownloader().downloadFile(downloadLink, progressWriter = progressWriter, progressWriterId = name)

        return if (inputStream != null) {
            log.info("$name Download Finished")
            log.info("$name Unzipping")
            progressWriter?.update(name, Progress("Unzipping..."))

            val targetDir = runtimesDir.resolve(config.platform)
            TarArchiveInputStream(ZstdInputStream(inputStream)).decompressTo(targetDir)

            log.info("$name Unzip Finished")
            log.info("$name Extracting executables")
            progressWriter?.update(name, Progress("Extracting executables..."))
            try {
                val map: Map<String, Any> =
                    jsonMapper().readValue(runtimesDir.resolve(config.platform).resolve("python/PYTHON.json"))

                val pythonExe =
                    runtimesDir.resolve(config.platform).resolve("python").resolve(map["python_exe"].toString())

                log.info("$name Finished")
                progressWriter?.update(name, Progress("Finished", total = -1))
                listOf(
                    ExecutableSymlink("python", pythonExe.absolutePath),
                    ExecutableSymlink("python3", pythonExe.absolutePath)
                )
            } catch (e: Exception) {
                progressWriter?.update(name, Progress("Error extracting executables..."))
                log.error("Error extracting executables", e)
                emptyList()
            }
        } else {
            progressWriter?.update(name, Progress("Failed"))
            log.error("$name Download Failed")
            emptyList()
        }

    }

    companion object {
        private val log = logger<PythonRuntimeService>()
    }
}
