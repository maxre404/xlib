package com.max.xlib.log

import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CrashHandler(private val logDir: File) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    private fun formatTime(millis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        Log.d("debug11", "uncaughtException: jkl12")
        // 格式化异常信息
        val crashInfo = buildString {
            append("Thread: ${thread.name}\n")
            append("Time: ${formatTime(System.currentTimeMillis())}\n")
            append("Exception: ${throwable}\n")
            append("Stacktrace:\n")
            append(Log.getStackTraceString(throwable))
            append("\n\n")
        }
        Log.d("debug11", "uncaughtException: $crashInfo")
        // 写入文件
        val crashFile = File(logDir, "crash_${formatTime(System.currentTimeMillis())}.log")
        crashFile.writeText(crashInfo)

        // 可选：交给系统默认处理（弹出崩溃对话框等）
        defaultHandler?.uncaughtException(thread, throwable)
    }
}