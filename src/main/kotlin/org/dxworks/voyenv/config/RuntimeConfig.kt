package org.dxworks.voyenv.config

data class RuntimeConfig(
    val version: String = "",
    val platform: String = "",
    val arch: String = "",
    val distribution: String = "",
    val type: String = "",
)
