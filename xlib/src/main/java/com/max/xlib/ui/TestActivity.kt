package com.max.xlib.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.max.xlib.R

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }
}