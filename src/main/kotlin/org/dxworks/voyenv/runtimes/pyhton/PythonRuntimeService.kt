package org.dxworks.voyenv.runtimes.pyhton

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.luben.zstd.ZstdInputStream
import com.google.api.client.http.GenericUrl
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.io.FileUtils
import org.dxworks.githubminer.service.repository.releases.GithubReleasesService
import org.dxworks.utils.java.rest.client.HttpClient
import org.dxworks.voyenv.config.RuntimeConfig
import org.dxworks.voyenv.runtimes.RuntimeService
import org.dxworks.voyenv.utils.decompressTo
import org.dxworks.voyenv.utils.makeScriptExecutable
import java.io.BufferedInputStream
import java.io.File
import java.nio.file.Files

class PythonRuntimeService(runtimesDir: File) : RuntimeService("python", runtimesDir) {

    val allStandaloneReleases by lazy {
        GithubReleasesService("indygreg", "python-build-standalone").getReleases()
    }

    override fun download(config: RuntimeConfig): Pair<String, String> {

        config.version
        config.platform
        config.arch
        config.distribution
        config.type

        val availableVersions = allStandaloneReleases.flatMap { it.assets.orEmpty() }

        val versionsMap =
            availableVersions.filter { it.name != null }.associateBy { PythonStandaloneAssetMetaInfo(it.name!!) }

        versionsMap.keys.sortedByDescending { it.timestamp }.find { it.version == config.version && it.platform == config.platform && it.arch == config.arch }?.apply {
            val releaseAsset = versionsMap[this]!!
            val httpClient = HttpClient()

            val response = httpClient.get(GenericUrl(releaseAsset.browserDownloadUrl))

            TarArchiveInputStream(ZstdInputStream(BufferedInputStream(response.content)))
                .decompressTo(runtimesDir.resolve(config.platform))
        }

        val map: Map<String, Object> = jsonMapper().readValue(runtimesDir.resolve(config.platform).resolve("python/PYTHON.json"))
        val scriptsPath = extractScriptsPath(map)

        val scriptsPathRelativeToVoyager = runtimesDir.resolve(config.platform).resolve("python").resolve(scriptsPath)
        makeScriptExecutable(runtimesDir.resolve(config.platform).resolve("python").resolve(map["python_exe"].toString()))

        return "python-${config.version}" to scriptsPathRelativeToVoyager.absolutePath
    }

    private fun extractScriptsPath(map: Map<String, Object>): String {
        val pythonPaths = map["python_paths"] as Map<String, String>
        return pythonPaths["scripts"].orEmpty()
    }

}
