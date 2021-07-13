package org.dxworks.voyagerrelease.runtimes.node

import org.dxworks.voyagerrelease.runtimes.RuntimeService
import java.io.File
import java.io.InputStream

class NodeRuntimeService : RuntimeService("node") {

    override fun download(configuration: Map<String, String>, location: File): InputStream {
        return object : InputStream() {
            override fun read() = -1
        }
    }
}
