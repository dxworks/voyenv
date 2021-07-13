package org.dxworks.voyagerrelease.runtimes.java

import org.dxworks.voyagerrelease.utils.unzipTo
import org.junit.jupiter.api.Test
import java.nio.file.Path

internal class JavaRuntimeServiceTest {
    @Test
    fun `download java 11 runtime`() {
        JavaRuntimeService().download(
            mapOf(
                "version" to "11.0.9",
                "arch" to "x64",
                "platform" to "mac"
            ), Path.of("voyager-javaRuntime").toFile()
        ).unzipTo(Path.of("./voyager-javaRelease").toFile())
    }
}
