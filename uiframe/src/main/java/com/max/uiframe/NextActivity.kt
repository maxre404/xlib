package com.max.uiframe

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.max.uiframe.widget.TreasureSnatchProgressBar

class NextActivity:AppCompatActivity() {
    val launcher = MultiProcessActivityLauncher(this) { result ->
        Log.d("debug11", "onCreate: 收到回调了哦++++++++++++")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        var progressBar = findViewById<TreasureSnatchProgressBar>(R.id.progressBar)
        var progress = 50.0
        findViewById<View>(R.id.btnStart).setOnClickListener {
            progress++
            progressBar.setCurrentAmount(progress)
        }
        findViewById<View>(R.id.btnMinus).setOnClickListener {
            progress--
            progressBar.setCurrentAmount(progress)
        }
        findViewById<View>(R.id.btnFailed).setOnClickListener {
            progressBar.updateProgressState(TreasureSnatchProgressState.COLLECT_FAIL)
        }
        findViewById<View>(R.id.btnSuccess).setOnClickListener {
            progressBar.updateProgressState(TreasureSnatchProgressState.COLLECT_SUCCESS)
        }
        findViewById<View>(R.id.btnExceed).setOnClickListener {
            progressBar.updateProgressState(TreasureSnatchProgressState.COLLECT_EXCEED)
        }
        findViewById<View>(R.id.btnFold).setOnClickListener {
            progressBar.setExpand(false)
        }
        findViewById<View>(R.id.btnExpand).setOnClickListener {
            progressBar.setExpand(true)
        }
    }
}