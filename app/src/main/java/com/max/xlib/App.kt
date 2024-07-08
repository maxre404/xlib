package com.max.xlib

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.max.xlib.utils.HookUtil

class App:Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        LogFile.log("加载++++++++")
    }

    override fun onCreate() {
        super.onCreate()
        HookUtil.start(this)
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openDebug();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openLog();
        }
        //初始化路由
        ARouter.init(this);
    }
}