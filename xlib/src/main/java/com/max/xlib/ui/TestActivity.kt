package com.max.xlib.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.max.xlib.R

@Route(path = "/tg/aod")
class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }
}