package com.max.xlib.log

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.LinkedBlockingQueue

class LogThread : Thread() {
    private var logPath: File? = null
    private var maxFileSize:Long = 5 * 1024 * 1024 // 5MB

    fun setLogPath(path: String?): LogThread {
        if (null==path){
            throw NullPointerException("path can't be null")
        }
        logPath = File(path,"app_logs")
        if (logPath?.exists()==false){
            logPath?.mkdirs()
        }
        return this
    }
//    fun setMaxFileSize(size: Long): LogThread {
//        maxFileSize = size
//        return this
//    }

    private fun formatTime(millis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    private fun getTodayLogFile(): File {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())
        val files =
            logPath?.listFiles { _, name -> name.startsWith(today) && name.endsWith(".log") }
        // 遍历找到第一个小于5MB的文件
        files?.sortedBy { it.name }?.forEach { file ->
            if (file.length() < maxFileSize) {
                return file
            }
        }
        // 如果都大于等于5MB，则新建一个index累加的新文件
        val index = files?.size ?: 0
        val fileName = if (index == 0) {
            "$today.log"
        } else {
            "${today}_$index.log"
        }
        return File(logPath, fileName)
    }

    override fun run() {
        super.run()
        var currentLogFile = getTodayLogFile()
        while (true) {
            try {
                val logData = logQueue.take()
                // 检查文件大小
                if (currentLogFile.length() > maxFileSize) {
                    currentLogFile = getTodayLogFile()
                }
                currentLogFile.appendText("${if (LogFile.isHideTme) "" else formatTime(logData.time)}  ${logData.message} \n")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {

        private val logQueue = LinkedBlockingQueue<LogData>()

        fun log(message: String) {
            if (logQueue.size<20){//防止未初始化 然后又调用了log 导致内存增多问题
                logQueue.put(LogData(System.currentTimeMillis(), message))
            }
        }
    }
}