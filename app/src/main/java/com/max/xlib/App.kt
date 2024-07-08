package com.max.xlib

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.max.xlib.utils.ApplicationHelper

class App:Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        LogFile.log("加载++++++++")
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationHelper.start(this)
    }
}