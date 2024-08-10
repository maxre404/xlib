package com.max.xlib.utils

import android.content.Context
import android.util.Log
import com.max.xlib.log.LogFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class ApplicationHelper {
    companion object{
       private val TAG = "xlib"
        private fun copyAssetFileToAppDirectory(context: Context, assetFileName: String?) {
            val assetManager = context.assets
            var inStream: InputStream? = null
            var outStream: OutputStream? = null

            try {
                inStream = assetManager.open(assetFileName!!)
                val outFile = File(context.filesDir, assetFileName)
//                val outFile = File("/data/local/tmp", assetFileName)
//                if (!outFile.exists()){
                    outStream = FileOutputStream(outFile)
                    copyFile(inStream, outStream)
                    Log.d(TAG, "file copy success " + outFile.absolutePath)
//                }
            } catch (e: IOException) {
                LogFile.log( "copy failue: " + e.message)
            } finally {
                try {
                    inStream?.close()
                    outStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        // 复制文件的方法
        @Throws(IOException::class)
        private fun copyFile(inputStream: InputStream, out: OutputStream) {
            val buffer = ByteArray(1024)
            var read: Int
            while ((inputStream.read(buffer).also { read = it }) != -1) {
                out.write(buffer, 0, read)
            }
        }
        fun start(context: Context){
            copyAssetFileToAppDirectory(context,"config_script.js")
        }

    }

}