package org.dxworks.voyagerrelease.config

import org.dxworks.voyagerrelease.utils.fieldMissingOrNull
import org.dxworks.voyagerrelease.utils.logger
import kotlin.system.exitProcess

class Voyenv(
    name: String? = null,
    voyager_version: String? = null,
    instruments: List<InstrumentEnvConfig>? = null
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
}
