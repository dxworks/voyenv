package org.dxworks.voyenv.utils

import org.dxworks.voyenv.Progress
import org.dxworks.voyenv.ProgressWriter
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files

class FilesDownloader {
    fun downloadFile(link: String, targetLocation: File? = null, progressWriter: ProgressWriter? = null, progressWriterId: String? = null): InputStream? {
        return try {
            val url = URL(link)
            val httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val completeFileSize: Int = httpConnection.contentLength

            val inputStream = BufferedInputStream(httpConnection.inputStream)
            val target = targetLocation ?: Files.createTempFile("voyenv", "").toFile()
            val fos = FileOutputStream(target)
            val size = 1024
            val bout = BufferedOutputStream(fos, size)
            val data = ByteArray(size)
            var downloadedFileSize: Long = 0
            var x: Int
            while (inputStream.read(data, 0, size).also { x = it } >= 0) {
                downloadedFileSize += x.toLong()
                progressWriter?.update(
                    progressWriterId,
                    Progress("Downloading", downloadedFileSize, completeFileSize.toLong())
                )
                bout.write(data, 0, x)
            }
            bout.close()
            inputStream.close()
            return BufferedInputStream(FileInputStream(target))
        } catch (e: Exception) {
            log.error("Could not download $link", e)
            null
        }
    }

    companion object {
        private val log = logger<FilesDownloader>()
    }
}
