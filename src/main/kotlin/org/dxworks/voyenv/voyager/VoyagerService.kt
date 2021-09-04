package org.dxworks.voyenv.voyager

import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.dxworks.githubminer.service.repository.releases.GithubReleasesService
import org.dxworks.voyenv.utils.*
import java.io.File

class VoyagerService(val tokens: List<String>) {

    companion object {
        private val log = logger<VoyagerService>()
    }

    private val githubReleasesService = GithubReleasesService(dxworks, voyager, githubTokens = tokens)

    fun downloadVoyager(tag: String, location: File) {
        log.info("Downloading voyager@$tag")
        val downloadReleaseAsset = githubReleasesService.downloadReleaseAsset(tag, voyagerAssetName)
        ZipArchiveInputStream(downloadReleaseAsset).decompressTo(location)
        unpackVoyagerDir(location)
        makeScriptExecutable(location.resolve("voyager.sh"))
        log.info("Done setting up voyager@$tag")
    }

    private fun unpackVoyagerDir(location: File) {
        val voyagerDir = location.resolve("voyager")
        voyagerDir.resolve("instruments/demo-instrument").deleteRecursively()
        voyagerDir.listFiles()?.forEach {
            it.copyRecursively(location.resolve(it.name), true)
        }
        writeDefaultConfigFile("/default-mission.yml", location.resolve("mission.yml"))
        voyagerDir.deleteRecursively()
    }

}
