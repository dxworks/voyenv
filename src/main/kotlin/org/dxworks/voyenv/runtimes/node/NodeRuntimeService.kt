package org.dxworks.voyenv.runtimes.node

import org.dxworks.voyenv.config.RuntimeConfig
import org.dxworks.voyenv.runtimes.RuntimeService
import java.io.File

class NodeRuntimeService(runtimesDir: File) : RuntimeService("node", runtimesDir) {

    override fun download(config: RuntimeConfig): Pair<String, String> {
        return "node" to "node"
    }

    fun getPathToBinary(config: RuntimeConfig): String {
        return "node"
    }

}
