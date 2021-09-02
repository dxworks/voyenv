package org.dxworks.voyenv

import com.fasterxml.jackson.module.kotlin.readValue
import org.dxworks.voyenv.config.Voyenv
import org.dxworks.voyenv.instruments.InstrumentsManager
import org.dxworks.voyenv.runtimes.RuntimesManager
import org.dxworks.voyenv.utils.defaultReleaseFileName
import org.dxworks.voyenv.utils.yamlMapper
import org.dxworks.voyenv.voyager.VoyagerService
import java.nio.file.Path
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    val voyenv: Voyenv = yamlMapper.readValue(
        Path.of(
            if (args.size == 1) {
                args[1]
            } else {
                defaultReleaseFileName
            }
        ).toFile()
    )

    println("Setting up release ${voyenv.name}")

    val releaseDir = Path.of(voyenv.name).toFile().apply { mkdirs() }

    VoyagerService().downloadVoyager(voyenv.voyagerVersion, releaseDir)
    InstrumentsManager(releaseDir, voyenv.tokens).apply { voyenv.instruments.forEach { thread { downloadInstrument(it) } } }
    RuntimesManager(releaseDir).downloadAndConfigureRuntimes(voyenv.runtimes)
}
