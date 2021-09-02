package org.dxworks.voyenv.config

import org.dxworks.voyenv.utils.fieldMissingOrNull
import org.dxworks.voyenv.utils.latest
import org.dxworks.voyenv.utils.logger
import kotlin.system.exitProcess

class InstrumentEnvConfig(
    name: String? = null,
    val tag: String = latest,
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

    override fun toString(): String {
        return "InstrumentEnvConfig(tag='$tag', asset=$asset, token=$token, name='$name')"
    }


}
