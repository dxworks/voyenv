package org.dxworks.voyagerrelease.config

import org.dxworks.voyagerrelease.utils.fieldMissingOrNull
import org.dxworks.voyagerrelease.utils.logger
import kotlin.system.exitProcess

class InstrumentEnvConfig(
    name: String? = null,
    tag: String? = null,
    val asset: String? = null,
    val token: String? = null
) {
    companion object {
        private val log = logger<InstrumentEnvConfig>()
        private const val source = "instrument environment config"
    }

    val name = name ?: kotlin.run {
        log.error(fieldMissingOrNull("name", source))
        exitProcess(1)
    }

    val tag = tag ?: kotlin.run {
        log.error(fieldMissingOrNull("tag", source))
        exitProcess(1)
    }
}
