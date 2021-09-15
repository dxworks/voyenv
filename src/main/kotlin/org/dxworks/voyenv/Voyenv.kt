package org.dxworks.voyenv

import com.github.ajalt.clikt.core.subcommands
import org.dxworks.voyenv.commands.Install
import org.dxworks.voyenv.commands.Init
import org.dxworks.voyenv.commands.Voyenv

fun main(args: Array<String>) {
//    if (args.size == 1 && versionCommandArgs.contains(args[0])) {
//        println("Voyenv $version")
//        return
//    }
    Voyenv()
        .subcommands(Init(), Install())
        .main(args)
}
