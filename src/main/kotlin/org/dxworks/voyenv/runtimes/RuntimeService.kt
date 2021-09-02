package org.dxworks.voyenv.runtimes

import org.dxworks.voyenv.config.RuntimeConfig
import java.io.File

abstract class RuntimeService(val name: String, val runtimesDir: File) {
    abstract fun download(config: RuntimeConfig): Pair<String, String>
}
