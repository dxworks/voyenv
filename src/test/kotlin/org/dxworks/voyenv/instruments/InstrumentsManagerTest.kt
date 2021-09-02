package org.dxworks.voyenv.instruments

import com.vdurmont.semver4j.Semver
import org.junit.jupiter.api.Test

internal class InstrumentsManagerTest {
    @Test
    internal fun testSemver() {
        val versions = listOf("v1.0.0", "v1.2.4-voyage", "12.4.2-voyager")

        val message = versions
            .asSequence()
            .filter { it.startsWith("v") && it.endsWith("-voyager") }
            .map { it.removePrefix("v") }
            .map { Semver(it, Semver.SemverType.LOOSE) }
            .sortedWith(Semver::compareTo)
            .lastOrNull()
            ?.let { "v$it" }
        println(message)
    }
}
