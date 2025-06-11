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
import java.io.File

class LogFile {


    companion object {
        private var isInited = false
        private const val TAG = "xlib"
        fun init(context: Context?) {
            LogThread().setLogPath(context?.externalCacheDir?.path).start()
            val logDir = File(context?.externalCacheDir?.path, "crashLogs")
            if (!logDir.exists()) logDir.mkdirs()
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler(logDir))
            isInited = true
        }
        fun log(message: String?) {
            Log.d(TAG, message!!)
            if (isInited){
//                XLog.d(message)
                LogThread.log(message)
            }
        }
    }
}