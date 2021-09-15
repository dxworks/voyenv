package org.dxworks.voyenv.commands

import com.fasterxml.jackson.module.kotlin.readValue
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import org.apache.commons.io.FileUtils
import org.dxworks.voyenv.config.Voyenv
import org.dxworks.voyenv.instruments.InstrumentsManager
import org.dxworks.voyenv.runtimes.RuntimesManager
import org.dxworks.voyenv.utils.defaultVoyenvFileName
import org.dxworks.voyenv.utils.yamlMapper
import org.dxworks.voyenv.voyager.VoyagerService
import java.nio.file.Paths

class Install : CliktCommand(
    help = """Will download and install a voyager release as well as its
    | instruments and runtimes as specified in the configuration file""".trimMargin()
) {

    val voyenvFilePath by argument(help = "The voyenv file with the configuration of the voyager release. By default, it is voyenv.yml")
        .default(defaultVoyenvFileName)

    override fun run() {
        val voyenvFile = Paths.get(voyenvFilePath).toFile()
        val voyenv: Voyenv = yamlMapper.readValue(voyenvFile)

        println("Setting up release ${voyenv.name}")

        val releaseDir = Paths.get(voyenv.name).toFile().apply { mkdirs() }

        VoyagerService().downloadVoyager(voyenv.voyagerVersion, releaseDir)
        InstrumentsManager(releaseDir, voyenv.tokens).downloadAndInstallInstruments(voyenv.instruments)
        RuntimesManager(releaseDir).downloadAndConfigureRuntimes(voyenv.runtimes)

        if (voyenv.name != ".")
            FileUtils.copyFile(voyenvFile, releaseDir.resolve(defaultVoyenvFileName))
    }

}
