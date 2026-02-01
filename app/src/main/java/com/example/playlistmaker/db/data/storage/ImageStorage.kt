package com.example.playlistmaker.db.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

class ImageStorage(private val context: Context) {

    fun saveImage(uri: Uri): Uri {
        if (uri == Uri.EMPTY) return Uri.EMPTY
        val directory = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlists"
        )

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "cover_${System.currentTimeMillis()}.jpg")

        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                BitmapFactory.decodeStream(input)
                    .compress(Bitmap.CompressFormat.JPEG, 30, output)
            }
        }
        return Uri.fromFile(file)
    }

    fun deleteImage(uri: Uri) {
        val pathString = uri.path ?: return
        val file = File(pathString)
        if (file.exists()) {
            file.delete()
        }
    }
}