package org.dxworks.voyenv.config

import org.dxworks.voyenv.utils.fieldMissingOrNull
import org.dxworks.voyenv.utils.logger
import kotlin.system.exitProcess

class Voyenv(
    name: String? = null,
    voyager_version: String? = null,
    instruments: List<InstrumentEnvConfig>? = null,
    runtimes: Map<String, RuntimeConfig>? = null,
    val tokens: List<String>? = null
) {
    companion object {
        private val log = logger<Voyenv>()
        private const val source = "voyager release"
    }

    val name = name ?: kotlin.run {
        log.error(fieldMissingOrNull("name", source))
        exitProcess(1)
    }

    val voyagerVersion = voyager_version!!

    val instruments: List<InstrumentEnvConfig> = instruments ?: emptyList()

    val runtimes: Map<String, RuntimeConfig> = runtimes ?: emptyMap()
}
