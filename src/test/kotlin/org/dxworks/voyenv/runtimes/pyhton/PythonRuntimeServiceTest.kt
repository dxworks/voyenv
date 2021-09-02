package org.dxworks.voyenv.runtimes.pyhton

import org.dxworks.voyenv.config.RuntimeConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class PythonRuntimeServiceTest {
    @Test
    internal fun name() {

        val pythonRuntimeService = PythonRuntimeService(File("."))
        val config: RuntimeConfig = RuntimeConfig("3.9.4", "mac", "x64")

        val allAssets = pythonRuntimeService.allStandaloneReleases.flatMap { it.assets.orEmpty() }.map { it.name }

        println(allAssets)

    }
}
