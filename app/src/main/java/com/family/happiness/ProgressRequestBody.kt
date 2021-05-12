package com.family.happiness

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.FileInputStream

class ProgressRequestBody(
    private val fileInputStream: FileInputStream,
    private val mediaType: MediaType
): RequestBody() {
    override fun contentType(): MediaType? {
        return mediaType
    }

    override fun contentLength(): Long {
        return fileInputStream.available().toLong()
    }

    override fun writeTo(sink: BufferedSink) {
        var read: Int
        val bytes = ByteArray(1024)
        while (fileInputStream!!.read(bytes).also { read = it } != -1) {
            sink.write(bytes, 0, read)
        }
    }
}