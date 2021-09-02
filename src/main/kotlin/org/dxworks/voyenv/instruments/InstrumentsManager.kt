package org.dxworks.voyenv.instruments

import com.vdurmont.semver4j.Semver
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.dxworks.githubminer.service.repository.releases.GithubReleasesService
import org.dxworks.githubminer.service.repository.source.GithubSourceCodeService
import org.dxworks.voyenv.config.InstrumentEnvConfig
import org.dxworks.voyenv.utils.decompressTo
import org.dxworks.voyenv.utils.latest
import org.dxworks.voyenv.utils.logger
import org.dxworks.voyenv.utils.ownerAndRepo
import java.io.File

class InstrumentsManager(location: File, val tokens: List<String>?) {

    companion object {
        private val log = logger<InstrumentsManager>()
    }

    private val instrumentsDir = location.resolve("instruments").apply {
        if (!exists())
            mkdirs()
    }

    fun downloadInstrument(instrumentEnvConfig: InstrumentEnvConfig) {
        val (owner, repo) = ownerAndRepo(instrumentEnvConfig.name)

        val tag = getTag(instrumentEnvConfig, owner, repo)

        if (tag == null) {
            log.error("Could not download instrument for $instrumentEnvConfig")
            return
        }

        if (instrumentEnvConfig.asset != null) {
            log.info("Downloading ${instrumentEnvConfig.name}@$tag:${instrumentEnvConfig.asset}")

            val githubReleasesService = getTokens(instrumentEnvConfig)
                ?.let { GithubReleasesService(owner, repo, githubTokens = it) }
                ?: GithubReleasesService(owner, repo)

            ZipArchiveInputStream(githubReleasesService.downloadReleaseAsset(tag, instrumentEnvConfig.asset))
                .decompressTo(instrumentsDir)

            log.info("${instrumentEnvConfig.name}@$tag:${instrumentEnvConfig.asset} Done")
        } else {
            log.info("Downloading ${instrumentEnvConfig.name}@$tag (source)")
            ZipArchiveInputStream(
                GithubSourceCodeService(owner, repo, token = instrumentEnvConfig.token)
                    .downloadSourceCode(tag)
            ).decompressTo(instrumentsDir)
            log.info("${instrumentEnvConfig.name}@$tag (source) Done")
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
}
