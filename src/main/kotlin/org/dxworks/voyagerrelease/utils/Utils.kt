package org.dxworks.voyagerrelease.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStream
import java.util.zip.ZipInputStream

inline fun <reified T : Any> logger(): Logger = LoggerFactory.getLogger(T::class.java)
val yamlMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

fun fieldMissingOrNull(field: String, source: String): String = "'$field' field is missing or null in $source"

fun ownerAndRepo(name: String): Pair<String, String> {
    val ownerAndRepo = name.split("/")
    return ownerAndRepo[0] to ownerAndRepo[1]
}


fun InputStream.unzipTo(location: File) {
    Unzip().extract(ZipInputStream(this), location)
}
