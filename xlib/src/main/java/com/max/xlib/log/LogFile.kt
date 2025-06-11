package com.max.xlib.log

import android.content.Context
import android.util.Log
import java.io.File

class LogFile {


    companion object {
        private var isInited = false
        private  var TAG = "xlib"
        /** 日志打印的时候 是否隐藏时间 默认是显示的 */
        var isHideTme:Boolean = false
        fun setCustomTag(tag:String){
            this.TAG = tag
        }
        fun init(context: Context?) {
            LogThread().setLogPath(context?.externalCacheDir?.path).start()
            initCrashLog(context)
            isInited = true
        }

        private fun initCrashLog(context: Context?) {
            val logDir = File(context?.externalCacheDir?.path, "crashLogs")
            if (!logDir.exists()) logDir.mkdirs()
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler(logDir))
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