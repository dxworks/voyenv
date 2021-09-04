package org.dxworks.voyenv.runtimes

class ExecutableSymlink(
    val executableLinkName: String, // the name of the executable (will be the name of the symlink file)
    val executableAbsolutePath: String // the target of the symlink (the absolute path to the executable)
)
