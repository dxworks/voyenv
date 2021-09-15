package org.dxworks.voyenv.utils

val versionCommandOptions = listOf("version", "-v", "--version", "-version", "-V")

const val defaultVoyenvFileName = "voyenv.yml"

const val dxworks = "dxworks"
const val voyager = "voyager"
const val voyagerAssetName = "dx-voyager.zip"

const val latest = "latest"

object OS {
    const val WINDOWS = "windows"
    const val LINUX = "linux"
    const val MAC = "mac"
    const val UNKNOWN = "unknown"
}

const val CLEAR_LINE = "\u001b[K"
const val CLEAR_SCREEN = "\u001b[2J"

object INIT {
    const val voyagerVersionTemplate = "voyagerVersion"
    const val voyagerNameTemplate = "voyagerName"
    const val voyagerPlatformTemplate = "platform"
}
