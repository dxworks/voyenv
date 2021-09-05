package org.dxworks.voyenv

import com.fasterxml.jackson.module.kotlin.readValue
import org.dxworks.githubminer.constants.ANONYMOUS
import org.dxworks.voyager.utils.versionCommandArgs
import org.dxworks.voyenv.config.Voyenv
import org.dxworks.voyenv.instruments.InstrumentsManager
import org.dxworks.voyenv.runtimes.RuntimesManager
import org.dxworks.voyenv.utils.defaultReleaseFileName
import org.dxworks.voyenv.utils.version
import org.dxworks.voyenv.utils.yamlMapper
import org.dxworks.voyenv.voyager.VoyagerService
import java.nio.file.Paths

fun main(args: Array<String>) {
    if (args.size == 1 && versionCommandArgs.contains(args[0])) {
        println("Voyenv $version")
        return
    }

    val voyenv: Voyenv = yamlMapper.readValue(Paths.get(getVoyenvFile(args)).toFile())

    println("Setting up release ${voyenv.name}")

    val releaseDir = Paths.get(voyenv.name).toFile().apply { mkdirs() }

    VoyagerService(voyenv.tokens ?: listOf(ANONYMOUS)).downloadVoyager(voyenv.voyagerVersion, releaseDir)
    InstrumentsManager(releaseDir, voyenv.tokens).downloadAndInstallInstruments(voyenv.instruments)
    RuntimesManager(releaseDir).downloadAndConfigureRuntimes(voyenv.runtimes)
}

private fun getVoyenvFile(args: Array<String>) =
    if (args.size == 1) {
        args[0]
    } else {
        defaultReleaseFileName
    }
