package org.dxworks.voyenv

import org.dxworks.voyenv.utils.CLEAR_LINE
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ProgressWriter(tasks: List<String>) {

    var printFirstTime = true

    val tasks: ConcurrentMap<String, Progress> = tasks.map { it to Progress("Pending") }.toMap(ConcurrentHashMap())

    var lastRun = 0L

    @Synchronized
    fun update(id: String?, progress: Progress) {
        id?.run {
            tasks[id] = progress
            val currentTimeInMillis = System.currentTimeMillis()
            if (currentTimeInMillis - lastRun > 500 || progress.forceWrite) {
                print()
                lastRun = currentTimeInMillis
            }
        }
    }


    fun print() {
        val toWrite = tasks.keys
            .sorted().joinToString("") { key ->
                tasks[key]
                    ?.let {
                        if (it.total > 0)
                            "$CLEAR_LINE$key: ${it.message} ${it.percentage} (${it.current}/${it.total}) ${it.extraMessage}\n"
                        else
                            "$CLEAR_LINE$key: ${it.message} ${it.extraMessage}\n"
                    }
                    ?: "\n"
            }

        if (!printFirstTime) {
            System.err.print("\u001b[${tasks.count()}F")
        } else {
            printFirstTime = !printFirstTime
        }
        System.err.print(toWrite)
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
