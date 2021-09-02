package org.dxworks.voyenv

import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.dxworks.githubminer.service.repository.releases.GithubReleasesService
import org.dxworks.voyenv.utils.decompressTo
import org.dxworks.voyenv.utils.dxworks
import org.dxworks.voyenv.utils.voyager
import org.dxworks.voyenv.utils.voyagerAssetName
import org.junit.jupiter.api.Test
import java.net.URL

class Test {

    private val githubReleasesService = GithubReleasesService(dxworks, voyager)


    @Test
    internal fun name() {
        val url = String(URL("https://github.com/dxworks/voyager/releases/download/v1.4.1/dx-voyager.zip").openStream().readAllBytes())

        val github = String(githubReleasesService.downloadReleaseAsset("v1.4.1", voyagerAssetName).readAllBytes())

//        ZipArchiveInputStream(GzipCompressorInputStream(githubReleasesService.downloadReleaseAsset("v1.4.1", voyagerAssetName))).decompressTo("./")

        println(url)
        println(github)

        println(url == github)

    }
}
