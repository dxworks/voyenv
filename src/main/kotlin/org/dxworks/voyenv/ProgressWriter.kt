package org.dxworks.voyenv

import org.dxworks.voyenv.utils.CLEAR_LINE
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ProgressWriter(
    tasks: List<String>,
    private val progressBarConfig: ProgressBarConfig? = null,
    val refreshTimeMs: Long = 500
) {

    var printFirstTime = true

    val tasks: ConcurrentMap<String, Progress> = tasks.map { it to Progress("Pending") }.toMap(ConcurrentHashMap())

    val maxTaskLength = tasks.map { it.length }.maxByOrNull { it }?.toInt() ?: 0

    var lastRun = 0L

    @Synchronized
    fun update(id: String?, progress: Progress) {
        id?.run {
            tasks[id] = progress
            val currentTimeInMillis = System.currentTimeMillis()
            if (currentTimeInMillis - lastRun > refreshTimeMs || progress.forceWrite) {
                print()
                lastRun = currentTimeInMillis
            }
        }
    }


    fun print() {
        val toWrite = tasks.keys
            .sorted().joinToString("") { key ->
                tasks[key]
                    ?.let { "$CLEAR_LINE${printTaskName(key)}: ${it.message} ${computeProgress(it)}${it.extraMessage}\n" }
                    ?: "\n"
            }

        if (!printFirstTime) {
            System.err.print("\u001b[${tasks.count()}F")
        } else {
            printFirstTime = !printFirstTime
        }
        System.err.print(toWrite)
    }

    private fun printTaskName(key: String) = key.padEnd(maxTaskLength)

    private fun computeProgress(it: Progress): String {
        return when {
            (it.total < 0) -> ""
            progressBarConfig == null -> "${it.percentage} (${it.current}/${it.total})"
            else -> "${createProgressBar(it)} ${it.percentage} (${it.current}/${it.total})"
        }
    }

    private fun createProgressBar(it: Progress): String {
        val pbConfig = progressBarConfig!!
        var returnString = pbConfig.prefix
        val numberOfCharacters: Int = (it.current.toDouble() / it.total * progressBarConfig.width).toInt()
        returnString += progressBarConfig.character.repeat(numberOfCharacters)
        returnString += " ".repeat(pbConfig.width - numberOfCharacters)
        returnString += progressBarConfig.suffix
        return returnString
    }
}

class Progress(
    val message: String,
    val current: Long = 0,
    val total: Long = -1,
    val extraMessage: String = "",
    val forceWrite: Boolean = false
) {
    val percentage = "${((current.toDouble() / total.toDouble()) * 100).toInt()}%"
}

class ProgressBarConfig(
    val character: String = "#",
    val prefix: String = "[",
    val suffix: String = "]",
    val width: Int = 50
)
