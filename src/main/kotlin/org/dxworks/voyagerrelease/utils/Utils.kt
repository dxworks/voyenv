package org.dxworks.voyagerrelease.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T : Any> logger(): Logger = LoggerFactory.getLogger(T::class.java)
val yamlMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

fun fieldMissingOrNull(field: String, source: String): String = "'$field' field is missing or null in $source"
