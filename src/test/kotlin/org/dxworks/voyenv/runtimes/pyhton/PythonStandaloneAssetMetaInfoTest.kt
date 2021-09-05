package org.dxworks.voyenv.runtimes.pyhton

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PythonStandaloneAssetMetaInfoTest {
    @Test
    internal fun createLinuxMetaInfo() {
        val metaInfo = PythonStandaloneAssetMetaInfo("cpython-3.9.4-x86_64-unknown-linux-gnu-lto-20210414T1515.tar.zst")
        assertEquals("3.9.4", metaInfo.version)
        assertEquals("x86_64", metaInfo.arch)
        assertEquals("unknown-linux", metaInfo.os)
        assertEquals("linux", metaInfo.platform)
        assertEquals("gnu", metaInfo.distribution)
        assertEquals("lto", metaInfo.optimization)
        assertEquals("20210414T1515", metaInfo.timestamp)
    }

    @Test
    internal fun createWindowsMetaInfo() {
        val metaInfo = PythonStandaloneAssetMetaInfo("cpython-3.8.10-i686-pc-windows-msvc-shared-pgo-20210506T0943.tar.zst")
        assertEquals("3.8.10", metaInfo.version)
        assertEquals("i686", metaInfo.arch)
        assertEquals("pc-windows-msvc", metaInfo.os)
        assertEquals("windows", metaInfo.platform)
        assertEquals("shared", metaInfo.distribution)
        assertEquals("pgo", metaInfo.optimization)
        assertEquals("20210506T0943", metaInfo.timestamp)
    }

    @Test
    internal fun createMacMetaInfo() {
        val metaInfo = PythonStandaloneAssetMetaInfo("cpython-3.9.5-aarch64-apple-darwin-pgo+lto-20210506T0943.tar.zst")
        assertEquals("3.9.5", metaInfo.version)
        assertEquals("aarch64", metaInfo.arch)
        assertEquals("apple", metaInfo.os)
        assertEquals("macos", metaInfo.platform)
        assertEquals("darwin", metaInfo.distribution)
        assertEquals("pgo+lto", metaInfo.optimization)
        assertEquals("20210506T0943", metaInfo.timestamp)
    }
}
