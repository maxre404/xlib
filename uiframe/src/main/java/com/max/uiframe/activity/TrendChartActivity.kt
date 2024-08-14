package com.max.uiframe.activity

import android.os.Bundle
import android.util.Pair
import androidx.appcompat.app.AppCompatActivity
import com.max.uiframe.R
import com.max.uiframe.widget.OneMWinGoTrendChartView

class TrendChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trend_chart)
//        val oneWinGoTrendChartView = findViewById<OneMWinGoTrendChartView>(R.id.oneWinGoTrendChartView)
//        oneWinGoTrendChartView.setTrendChartData(arrayListOf(
//            Pair("202408090845","0"),Pair("202408090845","9"),Pair("202408090845","1"),Pair("202408090845","0"),Pair("202408090845","2"),
//            Pair("202408090845","1"),Pair("202408090845","8"),Pair("202408090845","5"),Pair("202408090845","2"),Pair("202408090845","9"),
//            Pair("202408090845","2"),Pair("202408090845","3"),Pair("202408090845","4"),Pair("202408090845","5"),Pair("202408090845","6"),
//            Pair("202408090845","7"),Pair("202408090845","8"),Pair("202408090845","9"),Pair("202408090845","2"),Pair("202408090845","4"),
//        ))
    }
}