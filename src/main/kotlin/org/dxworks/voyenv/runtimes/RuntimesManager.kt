package org.dxworks.voyenv.runtimes

import org.dxworks.voyenv.config.RuntimeConfig
import org.dxworks.voyenv.dotconfig.GlobalConfigManager
import org.dxworks.voyenv.runtimes.java.JavaRuntimeService
import org.dxworks.voyenv.runtimes.node.NodeRuntimeService
import org.dxworks.voyenv.runtimes.pyhton.PythonRuntimeService
import org.dxworks.voyenv.utils.logger
import java.io.File

class RuntimesManager(private val releaseDir: File) {

    private val runtimesDir = releaseDir.resolve("runtimes").apply {
        if (!exists())
            mkdirs()
    }

    private val runtimeServices: Set<RuntimeService> = setOf(
        JavaRuntimeService(runtimesDir),
        PythonRuntimeService(runtimesDir),
        NodeRuntimeService(runtimesDir),
    );

    fun downloadAndConfigureRuntimes(runtimes: Map<String, RuntimeConfig>) {
        val newRuntimesMap = runtimes.mapNotNull { (runtimeName, config) ->
            runtimeServices.find { it.name == runtimeName }?.let { service ->
                addPathToRuntimeInDotConfig(service.download(config))
            }
        }.toMap()
        GlobalConfigManager(releaseDir).mergeRuntimes(newRuntimesMap)
    }

    private fun addPathToRuntimeInDotConfig(runtimePath: Pair<String, String>): Pair<String, String> {
        log.info("Making runtime path ${runtimePath.first}:${runtimePath.second} relative to release directory...")
        val newPair = runtimePath.first to File(runtimePath.second).relativeTo(releaseDir.absoluteFile).toString()
        log.info("Adding runtime ${newPair.first}:${newPair.second}")
        return newPair
    }


    companion object {
        private val log = logger<RuntimesManager>()
    }
}
