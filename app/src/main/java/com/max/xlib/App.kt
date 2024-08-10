package com.max.xlib

import android.app.Application
import android.content.Context
import org.lang.Systemm

class App:Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Systemm.hook(0)
    }
}