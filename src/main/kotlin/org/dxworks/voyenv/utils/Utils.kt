package org.dxworks.voyenv.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

inline fun <reified T : Any> logger(): Logger = LoggerFactory.getLogger(T::class.java)
val yamlMapper =
    ObjectMapper(YAMLFactory()).registerKotlinModule().setSerializationInclusion(JsonInclude.Include.NON_ABSENT)

val version: String by lazy {
    Properties().apply { load(object {}::class.java.classLoader.getResourceAsStream("maven.properties")) }["version"] as String
}

fun fieldMissingOrNull(field: String, source: String): String = "'$field' field is missing or null in $source"

fun ownerAndRepo(name: String): Pair<String, String> {
    val ownerAndRepo = name.split("/")
    return ownerAndRepo[0] to ownerAndRepo[1]
}


fun ArchiveInputStream.decompressTo(location: File, rootDirName: String? = null) {
    Unzip().decompress(this, location, rootDirName)
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

fun writeTemplateFile(resourcePath: String, targetFile: File, replacements: Map<String, String>) =
    object {}::class.java.classLoader.getResourceAsStream(resourcePath)
        ?.let { inputStream ->
            val filledText =
                inputStream.bufferedReader().readLines()
                    .joinToString(separator = "\n") { replaceTemplate(it, replacements) }
            targetFile.writeText(filledText)

        }?: println("Warning: Could not copy template file $resourcePath")

fun replaceTemplate(template: String, replacements: Map<String, String>): String {
    var newString = template
    replacements.forEach {
        newString = newString.replace("\${${it.key}}", it.value)
    }
    return newString
}
