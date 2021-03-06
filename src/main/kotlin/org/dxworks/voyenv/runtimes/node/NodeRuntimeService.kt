package org.dxworks.voyenv.runtimes.node

import org.dxworks.voyenv.ProgressWriter
import org.dxworks.voyenv.config.RuntimeConfig
import org.dxworks.voyenv.runtimes.ExecutableSymlink
import org.dxworks.voyenv.runtimes.RuntimeService
import java.io.File

class NodeRuntimeService(runtimesDir: File) : RuntimeService("node", runtimesDir) {

    override fun download(config: RuntimeConfig, progressWriter: ProgressWriter?): List<ExecutableSymlink> {
        return listOf()
    }

}
