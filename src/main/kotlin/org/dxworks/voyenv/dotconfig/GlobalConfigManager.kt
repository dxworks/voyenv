package org.dxworks.voyenv.dotconfig

import com.fasterxml.jackson.module.kotlin.readValue
import org.dxworks.voyager.api.global.GlobalConfig
import org.dxworks.voyenv.utils.yamlMapper
import java.io.File

class GlobalConfigManager(releaseDir: File) {

    private val dotConfigPath = releaseDir.resolve(".config.yml")

    private var globalConfig: GlobalConfig = yamlMapper.readValue(dotConfigPath)

    fun mergeRuntimes(runtimes: Map<String, String>) {
        val newRuntimes = HashMap<String, String>()
        newRuntimes.putAll(globalConfig.runtimes)
        newRuntimes.putAll(runtimes)
        val newGlobalConfig =
            GlobalConfig(globalConfig.runsAll, globalConfig.environment, newRuntimes, globalConfig.instrumentsDir)
        globalConfig = newGlobalConfig
        yamlMapper.writeValue(dotConfigPath, globalConfig)
    }
}
