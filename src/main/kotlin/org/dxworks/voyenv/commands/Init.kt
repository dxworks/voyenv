package org.dxworks.voyenv.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import org.dxworks.voyenv.utils.*
import java.nio.file.Files
import java.nio.file.Paths

class Init : CliktCommand("Initializes a voyenv project by creating a voyenv.yml file") {

    val folder by argument(help = "The folder where to generate the voyenv file.")
        .default(".")

    val createDir by option("-c", "--create-dir", help = "Whether to create the output dir")
        .flag(default = false)

    val allInstruments by option("-i", "--all-instruments", help = "Whether to add all instruments")
        .flag(default = false)

    val allRuntimes by option(
        "-r",
        "--all-runtimes",
        help = """Whether to add all runtimes. This implies automatically that all instruments are included"""
    )
        .flag(default = false)

    val voyagerName: String by option("-n", "--name", help = "Name of the voyenv release")
        .default("voyager")

    val voyagerVersion: String by option("-v", "--voyager-version", help = "The voyager version")
        .default("v1.5.0")

    val platform: String by option("-p", "--platform", help = "The target platform for Voyager")
        .choice(OS.WINDOWS, OS.MAC, OS.LINUX)
        .default(osType)


    override fun run() {
        val folderPath = Paths.get(folder)
        if (!Files.exists(folderPath))
            if (createDir) folderPath.toFile().mkdirs()
            else println(
                """
                |Error: Voyenv could not initialize the folder path ${folderPath.toAbsolutePath()} does not exist.
                |If you want Voyenv to create the directory use the -c (--create-dir) flag
                """.trimMargin()
            )
        val voyenvFile = folderPath.resolve(defaultVoyenvFileName)

        val templateReplacements = mapOf(
            INIT.voyagerNameTemplate to voyagerName,
            INIT.voyagerVersionTemplate to voyagerVersion,
            INIT.voyagerPlatformTemplate to platform
        )

        when {
            !allRuntimes && !allInstruments -> writeTemplateFile(
                "init/simple-voyenv.yml", voyenvFile.toFile(), templateReplacements
            )
            !allRuntimes && allInstruments -> writeTemplateFile(
                "init/instruments-voyenv.yml", voyenvFile.toFile(), templateReplacements
            )
            allRuntimes -> writeTemplateFile(
                "init/full-voyenv.yml", voyenvFile.toFile(), templateReplacements
            )
        }
    }
}
