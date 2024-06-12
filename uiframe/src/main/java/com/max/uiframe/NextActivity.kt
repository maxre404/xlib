package com.max.uiframe

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class NextActivity:AppCompatActivity() {
    val launcher = MultiProcessActivityLauncher(this) { result ->
        Log.d("debug11", "onCreate: 收到回调了哦++++++++++++")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        findViewById<View>(R.id.btnStart).setOnClickListener {
            launcher.launch()
        }
    }
}