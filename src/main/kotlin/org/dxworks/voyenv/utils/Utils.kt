package org.dxworks.voyenv.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.dxworks.voyenv.commandInterpreterName
import org.dxworks.voyenv.interpreterArg
import org.dxworks.voyenv.isWindows
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

inline fun <reified T : Any> logger(): Logger = LoggerFactory.getLogger(T::class.java)
val yamlMapper =
    ObjectMapper(YAMLFactory()).registerKotlinModule().setSerializationInclusion(JsonInclude.Include.NON_ABSENT)

val version by lazy {
    Properties().apply { load(object {}::class.java.classLoader.getResourceAsStream("maven.properties")) }["version"]
}

fun fieldMissingOrNull(field: String, source: String): String = "'$field' field is missing or null in $source"

fun ownerAndRepo(name: String): Pair<String, String> {
    val ownerAndRepo = name.split("/")
    return ownerAndRepo[0] to ownerAndRepo[1]
}


fun ArchiveInputStream.decompressTo(location: File) {
    Unzip().decompress(this, location)
}

fun makeScriptExecutable(location: File) {
    if (!isWindows) {
        ProcessBuilder()
            .command(commandInterpreterName, interpreterArg, "chmod +x ${location.absolutePath}").start()
            .waitFor()
    }
}

fun writeDefaultConfigFile(resourcePath: String, targetFile: File) =
    object {}::class.java.classLoader.getResourceAsStream(resourcePath)
        ?.let { targetFile.writeBytes(it.readAllBytes()) }
