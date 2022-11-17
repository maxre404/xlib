package com.max.xlib.log

import android.content.Context
import android.util.Log
import com.elvishew.xlog.BuildConfig
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.ClassicFlattener
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator

class LogFile {

    companion object {
        private var isInited = false
        private const val TAG = "xlib"
        fun init(context: Context?) {
            val filePrinter = FilePrinter.Builder(context?.externalCacheDir!!.path) // 指定保存日志文件的路径
                .fileNameGenerator(DateFileNameGenerator()) // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
                .cleanStrategy(FileLastModifiedCleanStrategy(1 * 7 * 24 * 60 * 60 * 1000)) // 指定日志文件清除策略，保存一周
                .flattener(ClassicFlattener())
                .build()
            val config = LogConfiguration.Builder().tag(TAG).build()
            XLog.init(config, filePrinter)
            isInited = true
        }
        fun log(message: String?) {
            Log.d(TAG, message!!)
            if (isInited){
                XLog.d(message)
            }
        }
    }
}