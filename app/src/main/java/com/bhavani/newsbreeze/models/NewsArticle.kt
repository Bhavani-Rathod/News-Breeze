package com.bhavani.newsbreeze.models

import android.util.Log
import java.io.IOException
import java.io.ObjectStreamException
import java.io.Serializable

data class NewsArticle(
    var author: String?,
    var title: String?,
    var description: String?,
    var urlToImage: String?,
    var url: String?,
    var publishedAt: String?,
) :
    Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    @Throws(IOException::class)
    private fun writeObject(out: java.io.ObjectOutputStream) {
        out.writeUTF(author)
        out.writeUTF(title)
        out.writeUTF(description)
        out.writeUTF(urlToImage)
        out.writeUTF(url)
        out.writeUTF(publishedAt)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(intStream: java.io.ObjectInputStream) {
        author = intStream.readUTF()
        title = intStream.readUTF()
        description = intStream.readUTF()
        urlToImage = intStream.readUTF()
        url = intStream.readUTF()
        publishedAt = intStream.readUTF()
    }

    @Throws(ObjectStreamException::class)
    private fun readObjectNoData() {
        Log.d("serial", "readObjectNoData called")

    }
}
