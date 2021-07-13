package org.dxworks.voyagerrelease.runtimes

import java.io.File
import java.io.InputStream

abstract class RuntimeService(val name: String) {
    abstract fun download(configuration: Map<String, String>, location: File): InputStream
}
