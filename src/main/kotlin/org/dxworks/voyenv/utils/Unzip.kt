package org.dxworks.voyenv.utils

import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.utils.IOUtils
import java.io.File
import java.io.IOException
import java.nio.file.Files


class Unzip {

    companion object {
        val logger = logger<Unzip>()
    }

    fun decompress(archiveInputStream: ArchiveInputStream, targetDir: File, rootDirName: String? = null) {
        archiveInputStream.use { inputStream ->
            var entry: ArchiveEntry? = inputStream.nextEntry

            while (entry != null) {
                if (!inputStream.canReadEntryData(entry)) {
                    logger.warn("Could not read entry $entry")
                    // log something?
                    continue;
                }
                val name = fileName(targetDir, entry, rootDirName)
                val f = File(name)
                if (entry.isDirectory) {
                    if (!f.isDirectory && !f.mkdirs()) {
                        throw IOException("failed to create directory $f")
                    }
                } else {
                    val parent = f.parentFile;
                    if (!parent.isDirectory && !parent.mkdirs()) {
                        throw IOException("failed to create directory $parent")
                    }
                    Files.newOutputStream(f.toPath()).use {
                        logger.debug("Writing file ${f.absolutePath} to disk")
                        IOUtils.copy(inputStream, it)
                    }

                }
                entry = inputStream.nextEntry
            }
        }
    }

    private fun fileName(targetDir: File, entry: ArchiveEntry, rootDirName: String?): String {
        val destFile = File(targetDir,
            entry.name.let { if (rootDirName != null) it.replaceBefore("/", rootDirName) else it })
        val destDirPath = targetDir.canonicalPath
        val destFilePath = destFile.canonicalPath
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw IOException("Entry is outside of the target dir: " + entry.name)
        }
        return destFilePath
    }
}
