package org.dxworks.voyagerrelease.instruments

import org.dxworks.voyagerrelease.config.InstrumentEnvConfig
import org.dxworks.voyagerrelease.utils.voyagerInstrumentsResource
import java.io.File

class InstrumentsManager(val instrumentsLocation: File) {


    init {
        if (!instrumentsLocation.exists())
            instrumentsLocation.mkdirs()
    }

    private val instrumentNameToReleaseAssetName: Map<String, String> =
        javaClass.getResource(voyagerInstrumentsResource)!!.readText().split("\n").associate {
            val split = it.split(" ")
            split[0] to split[1]
        }


    fun downloadInstrument(instrumentEnvConfig: InstrumentEnvConfig) {
        
    }
}
