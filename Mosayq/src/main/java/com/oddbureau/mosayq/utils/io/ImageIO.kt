package com.oddbureau.mosayq.utils.io

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by Ilya Gazman on 3/6/2016. Customised by Joel Oliveira on 18/12/16
 */

class ImageIO(private val context: Context) {

    private var fileName = "image.jpg"
    private var external: Boolean = false

    fun setFileName(fileName: String): ImageIO {
        this.fileName = fileName
        return this
    }

    fun setExternal(external: Boolean): ImageIO {
        this.external = external
        return this
    }

    fun save(bitmapImage: Bitmap) {
        var fileOutputStream: FileOutputStream? = null
        try {
            var file = createFile()
            fileOutputStream = FileOutputStream(file)
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream!!.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun getFile(): File {
        return createFile()
    }

    private fun createFile(): File {

        val directory: File
        if (external) {
            directory = getAlbumStorageDir("images")
        } else {
            directory = context.filesDir
        }


        return File(directory, fileName)
    }


    private fun getAlbumStorageDir(albumName: String): File {
        val file = File(context.getExternalFilesDir(null), albumName)
        if (!file.mkdirs()) {
            Log.e("ImageSaver", "Directory not created")
        }
        return file
    }


    fun deleteFile(path: String?) : Boolean{
        val uri = Uri.parse(path)
        var deleted = File(uri.path).absoluteFile.delete()
        return deleted
    }


}