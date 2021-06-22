package org.dxworks.voyagerrelease

import com.fasterxml.jackson.module.kotlin.readValue
import org.dxworks.voyagerrelease.config.Voyenv
import org.dxworks.voyagerrelease.utils.defaultReleaseFileName
import org.dxworks.voyagerrelease.utils.yamlMapper
import org.dxworks.voyagerrelease.voyager.VoyagerService
import java.nio.file.Path

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
    println(voyenv.instruments.joinToString { "${it.name} : ${it.version}" })

    println("Downloading voyager version " + voyenv.voyagerVersion)

    val releaseDir = Path.of(voyenv.name).toFile().apply { mkdirs() }

    VoyagerService().downloadVoyager(voyenv.voyagerVersion, releaseDir)
}
