 package com.family.happiness.network

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

 class PhotoUploadBody(
    private val file: File,
    private val mediaType: MediaType,
    private val callback: () -> Unit,
): RequestBody() {

    companion object{
        const val DEFAULT_BUFFER_SIZE = 1024
    }


    override fun contentType(): MediaType? {
        return mediaType
    }

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var byteCount: Int
        FileInputStream(file).use { fileInputStream ->
            while (fileInputStream.read(buffer).also { byteCount = it } != -1){
                sink.write(buffer, 0, byteCount)
            }
        }
    }
}