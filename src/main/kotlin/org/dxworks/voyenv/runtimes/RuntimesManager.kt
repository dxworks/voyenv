package org.dxworks.voyenv.runtimes

import org.dxworks.voyenv.ProgressWriter
import org.dxworks.voyenv.config.RuntimeConfig
import org.dxworks.voyenv.runtimes.java.JavaRuntimeService
import org.dxworks.voyenv.runtimes.node.NodeRuntimeService
import org.dxworks.voyenv.runtimes.pyhton.PythonRuntimeService
import org.dxworks.voyenv.utils.logger
import org.dxworks.voyenv.utils.makeScriptExecutable
import org.dxworks.voyenv.utils.writeDefaultConfigFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class RuntimesManager(private val releaseDir: File) {

    private val runtimesDir = releaseDir.resolve("runtimes").apply {
        if (!exists())
            mkdirs()
    }

    private val executablePathsDir = runtimesDir.resolve("paths").apply {
        if (!exists())
            mkdirs()
    }

    private val runtimeServices: Set<RuntimeService> = setOf(
        JavaRuntimeService(runtimesDir),
        PythonRuntimeService(runtimesDir),
        NodeRuntimeService(runtimesDir),
    );

    fun downloadAndConfigureRuntimes(runtimes: Map<String, RuntimeConfig>) {
        println("Setting up runtimes:")

        val availableRuntimes = runtimes.filter { getRuntimeService(it.key) != null }

        val progressWriter = ProgressWriter(availableRuntimes.keys.toList())

        availableRuntimes
            .entries
            .parallelStream()
            .map { (runtimeName, config) -> getRuntimeService(runtimeName)!!.download(config, progressWriter) }
            .flatMap { it.stream() }
            .forEach { createExecutableSymlink(it) }


        writeDefaultConfigFile("default-config.yml", releaseDir.resolve(".config.yml"))
        writeDefaultConfigFile("default-doctor.yml", releaseDir.resolve(".doctor.yml"))
        println("Finished setting up runtimes")
    }

    private fun getRuntimeService(runtimeName: String): RuntimeService? {
        val service = runtimeServices.find { it.name == runtimeName }
        if (service == null) {
            log.error("Unknown runtime configuration $runtimeName")
        }
        return service
    }

    private fun createExecutableSymlink(it: ExecutableSymlink) {
        val link = executablePathsDir.resolve(it.executableLinkName).toPath()
        if (Files.exists(link))
            Files.delete(link)

        Files.createSymbolicLink(link, Paths.get(it.executableAbsolutePath))
        makeScriptExecutable(link.toFile())
        makeScriptExecutable(Paths.get(it.executableAbsolutePath).toFile())
    }

    companion object {
        private val log = logger<RuntimesManager>()
    }
}
