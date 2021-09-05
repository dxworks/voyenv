package org.dxworks.voyenv.instruments

import com.vdurmont.semver4j.Semver
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.dxworks.githubminer.constants.GITHUB_PATH
import org.dxworks.githubminer.service.repository.releases.GithubReleasesService
import org.dxworks.voyenv.Progress
import org.dxworks.voyenv.ProgressWriter
import org.dxworks.voyenv.config.InstrumentEnvConfig
import org.dxworks.voyenv.utils.*
import java.io.File

class InstrumentsManager(location: File, val tokens: List<String>?) {

    companion object {
        private val log = logger<InstrumentsManager>()
    }

    private val instrumentsDir = location.resolve("instruments").apply {
        if (!exists())
            mkdirs()
    }

    private fun getDownloadLink(instrumentEnvConfig: InstrumentEnvConfig): String? {
        val (owner, repo) = ownerAndRepo(instrumentEnvConfig.name)

        val tag = getTag(instrumentEnvConfig, owner, repo)

        if (tag == null) {
            log.error("Could not find tag of instrument for $instrumentEnvConfig")
            return null
        }

        return if (instrumentEnvConfig.asset != null) {
            log.info("Downloading ${instrumentEnvConfig.name}@$tag:${instrumentEnvConfig.asset}")
            "$GITHUB_PATH/$owner/$repo/releases/download/${tag}/${instrumentEnvConfig.asset}"
        } else {
            log.info("Downloading ${instrumentEnvConfig.name}@$tag (source)")
            "$GITHUB_PATH/$owner/$repo/archive/refs/tags/${tag}.zip"
        }
    }

    private fun getTokens(instrumentEnvConfig: InstrumentEnvConfig): List<String>? =
        tokens?.let { list -> instrumentEnvConfig.token?.let { listOf(it) + list } ?: list }

    fun getTag(
        instrumentEnvConfig: InstrumentEnvConfig,
        owner: String,
        repo: String
    ) = if (instrumentEnvConfig.tag == latest) {
        val githubReleasesService = instrumentEnvConfig.token
            ?.let { GithubReleasesService(owner, repo, githubTokens = listOf(it)) }
            ?: GithubReleasesService(owner, repo)
        val releases = githubReleasesService.getReleases()
        log.info("Found ${releases.size} releases for $owner/$repo: ${releases.mapNotNull { it.tagName }}")
        releases
            .asSequence()
            .mapNotNull { it.tagName }
            .filter { it.startsWith("v") && it.endsWith("-voyager") }
            .map { it.removePrefix("v") }
            .map { Semver(it, Semver.SemverType.LOOSE) }
            .sortedWith(Semver::compareTo)
            .lastOrNull()
            ?.let { "v$it" }.also { log.info("Latest version for $owner/$repo is $it") }
    } else
        instrumentEnvConfig.tag

    fun downloadAndInstallInstruments(instruments: List<InstrumentEnvConfig>) {
        println("Downloading instruments:")
        val instrumentLinks: Map<InstrumentEnvConfig, String?> = instruments.associateWith { getDownloadLink(it) }
        val foundInstruments = instrumentLinks.filter { it.value != null }
        val progressWriter = ProgressWriter(foundInstruments.keys.map { it.name })

        foundInstruments.entries.parallelStream()
            .forEach {
                val instrumentEnvConfig = it.key
                val url = it.value
                log.info("Downloading ${instrumentEnvConfig.name}: $url")
                val inputStream = FilesDownloader().downloadFile(url!!, progressWriter = progressWriter, progressWriterId = instrumentEnvConfig.name)
                if (inputStream != null) {
                    log.info("${instrumentEnvConfig.name} Download Finished")
                    log.info("${instrumentEnvConfig.name} Unzipping")
                    progressWriter.update(instrumentEnvConfig.name, Progress("Unzipping...", total = -1))
                    ZipArchiveInputStream(inputStream).decompressTo(instrumentsDir)
                    log.info("${instrumentEnvConfig.name} Finished")
                    progressWriter.update(instrumentEnvConfig.name, Progress("Finished", total = -1))
                } else {
                    progressWriter.update(instrumentEnvConfig.name, Progress("Failed", total = -1))
                    log.error("${instrumentEnvConfig.name} Download Failed")
                }
            }
        println("Finished downloading instruments")
    }
}
