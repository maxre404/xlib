package com.max.uiframe.data

import android.util.Log
import android.widget.EditText
import androidx.databinding.BaseObservable

data class OnToWinGameConfig(
    var maxAmount:Long = 100000,
    var minAmount:Long = 100,
    var amount:String = "20"
):BaseObservable() {

    fun beforeTextChanged(editText: EditText){

    }

    fun afterTextChanged(editText: EditText){
        Log.d("debug11", "afterTextChanged: ++++++++++++这里执行了")
        editText.setSelection(editText.text.length)
        if (amount.toLong()>100){
            amount = "100"
        }else if (amount.toLong()<20){
            amount = "20"
        }
        notifyChange()
    }


}