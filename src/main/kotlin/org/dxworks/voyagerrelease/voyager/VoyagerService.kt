package org.dxworks.voyagerrelease.voyager

import org.dxworks.githubminer.service.repository.releases.GithubReleasesService
import org.dxworks.voyagerrelease.commandInterpreterName
import org.dxworks.voyagerrelease.interpreterArg
import org.dxworks.voyagerrelease.isWindows
import org.dxworks.voyagerrelease.utils.Unzip
import org.dxworks.voyagerrelease.utils.dxworks
import org.dxworks.voyagerrelease.utils.voyager
import org.dxworks.voyagerrelease.utils.voyagerAssetName
import java.io.File
import java.util.zip.ZipInputStream

class VoyagerService {

    private val githubReleasesService = GithubReleasesService(dxworks, voyager)

    fun downloadVoyager(tag: String, location: File) {
        githubReleasesService.downloadReleaseAsset(tag, voyagerAssetName)
            .also { Unzip().extract(ZipInputStream(it), location) }
        unpackVoyagerDir(location)
        makeScriptExecutable(location)
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
