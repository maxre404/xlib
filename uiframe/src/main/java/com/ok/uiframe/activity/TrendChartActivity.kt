package com.ok.uiframe.activity

import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.max.uiframe.R
import com.ok.uiframe.adapter.SampleAdapter
import com.ok.uiframe.widget.BottomTipsView
import com.ok.uiframe.widget.OneMWinGoTrendChartView

class TrendChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trend_chart)
        val bottomTipsView = findViewById<BottomTipsView>(R.id.bottomTipsView)
        val oneWinGoTrendChartView = findViewById<OneMWinGoTrendChartView>(R.id.oneWinGoTrendChartView)
        oneWinGoTrendChartView.setTrendChartData(arrayListOf(
            Pair("202408090845","0"),Pair("202408090845","9"),Pair("202408090845","1"),Pair("202408090845","0"),Pair("202408090845","2"),
            Pair("202408090845","1"),Pair("202408090845","8"),Pair("202408090845","5"),Pair("202408090845","2"),Pair("202408090845","9"),
            Pair("202408090845","2"),Pair("202408090845","3"),Pair("202408090845","4"),Pair("202408090845","5"),Pair("202408090845","6"),
            Pair("202408090845","7"),Pair("202408090845","8"),Pair("202408090845","9"),Pair("202408090845","2"),Pair("202408090845","4"),
        ))
        findViewById<View>(R.id.btnShow).setOnClickListener {
            bottomTipsView.showTips()
        }
        val recyclerView = findViewById<RecyclerView>(R.id.rvItem)
       val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val data: MutableList<String> = ArrayList()
        for (i in 1..100) {
            data.add("Item $i")
        }

        val adapter = SampleAdapter(data)
        recyclerView.adapter = adapter
    }
}