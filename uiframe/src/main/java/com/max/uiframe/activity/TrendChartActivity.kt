package com.max.uiframe.activity

import android.os.Bundle
import android.util.Pair
import androidx.appcompat.app.AppCompatActivity
import com.max.uiframe.R
import com.max.uiframe.widget.OneWinGoTrendChartView

class TrendChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trend_chart)
        val oneWinGoTrendChartView = findViewById<OneWinGoTrendChartView>(R.id.oneWinGoTrendChartView)
        oneWinGoTrendChartView.setTrendChartData(arrayListOf(
            Pair("123456789","0"),Pair("123456789","9"),Pair("123456789","1"),Pair("123456789","0"),Pair("123456789","2"),
            Pair("123456789","1"),Pair("123456789","8"),Pair("123456789","5"),Pair("123456789","2"),Pair("123456789","9"),
            Pair("123456789","2"),Pair("123456789","3"),Pair("123456789","4"),Pair("123456789","5"),Pair("123456789","6"),
            Pair("123456789","7"),Pair("123456789","8"),Pair("123456789","9"),Pair("123456789","2"),Pair("123456789","4"),
        ))
    }
}