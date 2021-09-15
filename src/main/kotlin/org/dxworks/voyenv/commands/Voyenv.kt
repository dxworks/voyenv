package org.dxworks.voyenv.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.versionOption
import org.dxworks.voyager.utils.versionCommandArgs
import org.dxworks.voyenv.utils.version

class Voyenv : CliktCommand(
    help = """Welcome to the voyenv cli!""".trimMargin()
) {
    init {
        versionOption(version, names = versionCommandArgs, message = { "Voyenv $it" })
    }

    override fun run() = Unit

}
