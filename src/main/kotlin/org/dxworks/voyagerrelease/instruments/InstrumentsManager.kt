package org.dxworks.voyagerrelease.instruments

import org.dxworks.githubminer.service.repository.releases.GithubReleasesService
import org.dxworks.githubminer.service.repository.source.GithubSourceCodeService
import org.dxworks.voyagerrelease.config.InstrumentEnvConfig
import org.dxworks.voyagerrelease.utils.logger
import org.dxworks.voyagerrelease.utils.ownerAndRepo
import org.dxworks.voyagerrelease.utils.unzipTo
import java.io.File

class InstrumentsManager(location: File) {

    companion object {
        private val log = logger<InstrumentsManager>()
    }

    private val instrumentsDir = location.resolve("instruments").apply {
        if (!exists())
            mkdirs()
    }

    fun downloadInstrument(instrumentEnvConfig: InstrumentEnvConfig) {
        val (owner, repo) = ownerAndRepo(instrumentEnvConfig.name)
        if (instrumentEnvConfig.asset != null) {
            log.info("Downloading ${instrumentEnvConfig.name}@${instrumentEnvConfig.tag}:${instrumentEnvConfig.asset}")
            val githubReleasesService = instrumentEnvConfig.token
                ?.let { GithubReleasesService(owner, repo, githubTokens = listOf(it)) }
                ?: GithubReleasesService(owner, repo)

            githubReleasesService.downloadReleaseAsset(instrumentEnvConfig.tag, instrumentEnvConfig.asset)
                .unzipTo(instrumentsDir)
            log.info("${instrumentEnvConfig.name}@${instrumentEnvConfig.tag}:${instrumentEnvConfig.asset} Done")
        } else {
            log.info("Downloading ${instrumentEnvConfig.name}@${instrumentEnvConfig.tag} (source)")
            GithubSourceCodeService(owner, repo, token = instrumentEnvConfig.token)
                .downloadSourceCode(instrumentEnvConfig.tag).unzipTo(instrumentsDir)
            log.info("${instrumentEnvConfig.name}@${instrumentEnvConfig.tag} (source) Done")
        }
    }
}
