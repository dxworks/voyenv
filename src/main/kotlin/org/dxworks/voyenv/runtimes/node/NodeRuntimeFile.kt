package org.dxworks.voyenv.runtimes.node

class NodeRuntimeFile(
    val arch: String,
    val platform: String,
    download_url: String
) {
    val downloadUrl = download_url
}
