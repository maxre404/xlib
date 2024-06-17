package com.max.uiframe

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.databinding.OnRebindCallback
import com.max.uiframe.data.OnToWinGameConfig
import com.max.uiframe.databinding.ActivityNextBinding
import com.max.uiframe.widget.TreasureSnatchProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

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
        binding = ActivityNextBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding?.config = config
        myWinAndRewardFlow.buffer(capacity = 20)
        binding?.addOnRebindCallback(object : OnRebindCallback<ActivityNextBinding>() {
            override fun onBound(binding: ActivityNextBinding?) {
                super.onBound(binding)
//                if (isFirst){
//                    binding?.editTextNumberSigned?.setSelection(config.amount.length)
//                    isFirst = false
//                    Log.d("debug11", "onPropertyChanged  绑定完成了哦:+++++++++++ ")
//                }
            }

            override fun onPreBind(binding: ActivityNextBinding?): Boolean {
                return super.onPreBind(binding)
                Log.d("debug11", "onPreBind  onPreBind:+++++++++++ ")
            }

            override fun onCanceled(binding: ActivityNextBinding?) {
                super.onCanceled(binding)
                Log.d("debug11", "onCanceled  onCanceled:+++++++++++ ")
            }
        })
        binding?.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                Log.d("debug11", "onPropertyChanged:+++++++++++ ")
            }

        })
        var progressBar = findViewById<TreasureSnatchProgressBar>(R.id.progressBar)
        var progress = 50.0
        findViewById<View>(R.id.btnStart).setOnClickListener {
//            config.amount = "50"
//            config.notifyChange()
//            progress++
//            progressBar.setCurrentAmount(progress)
            CoroutineScope(Dispatchers.IO).launch {
                for(i in 0..10){
                    Log.d("jeemmo", "onCreate: +++++++:$i")
                    myWinAndRewardFlow.emit(i.toString())
                }
            }
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