package com.ok.uiframe

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.textinput.ReactEditText
import com.facebook.react.views.text.ReactTextUpdate
import com.max.uiframe.R
import com.ok.uiframe.data.OnToWinGameConfig
import com.max.uiframe.databinding.ActivityNextBinding
import com.ok.uiframe.widget.TreasureSnatchProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import org.lang.Systemm


class NextActivity:AppCompatActivity() {
    var isFirst = true
    var config = OnToWinGameConfig()
    var binding:ActivityNextBinding?=null
    private val myWinAndRewardFlow = MutableSharedFlow<String>()

    val launcher = MultiProcessActivityLauncher(this) { result ->
        Log.d("debug11", "onCreate: 收到回调了哦++++++++++++")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Systemm.start(this)
        binding = ActivityNextBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding?.config = config
        myWinAndRewardFlow.buffer(capacity = 20)
        var progressBar = findViewById<TreasureSnatchProgressBar>(R.id.progressBar)
        var progress = 50.0
        findViewById<View>(R.id.btnStart).setOnClickListener {
            var text = ReactEditText(this@NextActivity)
            // 创建一个 SpannableString
            val spannableString = SpannableString("你在哪里呢++++++++++++++++++++")
            // 设置部分文本的前景颜色
            val foregroundColorSpan: ForegroundColorSpan = ForegroundColorSpan(Color.RED)
            spannableString.setSpan(foregroundColorSpan, 8, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            var textUpdate = ReactTextUpdate(spannableString,3,true,4,5,6)
            text.maybeSetText(textUpdate)


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

        CoroutineScope(Dispatchers.Main).launch {
            myWinAndRewardFlow.collect { str ->
                delay(1000)
                Log.i("jeemmo", "myWinAndRewardFlow collect myWinBean = $str" )
            }
        }

    }

    companion object{
        fun onBound(binding: ActivityNextBinding) {
            // 绑定完成后的操作
        }
    }
}