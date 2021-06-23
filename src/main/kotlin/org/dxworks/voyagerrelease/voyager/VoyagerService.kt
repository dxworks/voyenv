package org.dxworks.voyagerrelease.voyager

import org.dxworks.githubminer.service.repository.releases.GithubReleasesService
import org.dxworks.voyagerrelease.commandInterpreterName
import org.dxworks.voyagerrelease.interpreterArg
import org.dxworks.voyagerrelease.isWindows
import org.dxworks.voyagerrelease.utils.*
import java.io.File

class VoyagerService {

    companion object {
        private val log = logger<VoyagerService>()
    }

    private val githubReleasesService = GithubReleasesService(dxworks, voyager)

    fun downloadVoyager(tag: String, location: File) {
        log.info("Downloading voyager@$tag")
        githubReleasesService.downloadReleaseAsset(tag, voyagerAssetName).unzipTo(location)
        unpackVoyagerDir(location)
        makeScriptExecutable(location)
        log.info("Done setting up voyager@$tag")
    }

    private fun unpackVoyagerDir(location: File) {
        val voyagerDir = location.resolve("dx-voyager")
        voyagerDir.listFiles()?.forEach {
            it.copyRecursively(location.resolve(it.name), true)
        }
        voyagerDir.deleteRecursively()
    }

    private fun makeScriptExecutable(location: File) {
        if (!isWindows) {
            ProcessBuilder().directory(location)
                .command(commandInterpreterName, interpreterArg, "chmod +x voyager.sh").start()
                .waitFor()
        }
    }
}
