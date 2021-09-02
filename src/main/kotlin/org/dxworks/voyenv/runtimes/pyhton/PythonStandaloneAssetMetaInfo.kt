package org.dxworks.voyenv.runtimes.pyhton

import org.dxworks.voyenv.utils.OS

data class PythonStandaloneAssetMetaInfo(
    val assetName: String
) {
    val version: String
    val arch: String
    val os: String
    val platform: String
    val distribution: String
    val optimization: String
    val timestamp: String

    init {
        var remainingName = assetName.removePrefix("cpython-")
        version = remainingName.substringBefore("-")
        remainingName = remainingName.replaceBefore("-", "").removePrefix("-")
        arch = remainingName.substringBefore("-")
        remainingName = remainingName.replaceBefore("-", "").removePrefix("-")
        os = when {
            remainingName.startsWith("pc-") -> WINDOWS_STRING
            remainingName.startsWith("apple-") -> MAC_STRING
            remainingName.startsWith("unknown-") -> LINUX_STRING
            else -> ""
        }
        platform = when(os) {
            WINDOWS_STRING -> OS.WINDOWS
            MAC_STRING -> OS.MAC
            LINUX_STRING -> OS.LINUX
            else -> ""
        }
        remainingName = remainingName.removePrefix("$os-")
        distribution = remainingName.substringBefore("-")
        remainingName = remainingName.replaceBefore("-", "").removePrefix("-")
        optimization = remainingName.substringBefore("-")
        remainingName = remainingName.replaceBefore("-", "").removePrefix("-")
        timestamp = remainingName.removeSuffix(".tar.zst")
    }

    companion object {
        const val WINDOWS_STRING = "pc-windows-msvc"
        const val MAC_STRING = "apple"
        const val LINUX_STRING = "unknown-linux"
    }
}

