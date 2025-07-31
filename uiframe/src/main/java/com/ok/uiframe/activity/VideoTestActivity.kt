package com.ok.uiframe.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.max.uiframe.R
import com.ok.uiframe.adapter.ViewPagerAdapter
import com.ok.uiframe.widget.VideoControlLayout

class VideoTestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_test)
        val vp = findViewById<ViewPager2>(R.id.vp)
        vp.adapter = ViewPagerAdapter(this)
//        val rv = findViewById<RecyclerView>(R.id.rv)
//        rv.linear().setup {
//            addType<String>(R.layout.item_view)
//            onBind {
//                val itemText = findView<TextView>(R.id.item_text)
//                itemText.text = getModel<CharSequence?>().toString()
//            }
//        }.models = listOf("1","2","3","4","5","6","7","8","9","10")
//        findViewById<View>(R.id.button2).setOnClickListener {
//            Log.d("debug11", "onCreate: 这里时button 的点击事件哦")
//        }
//        val videoControlLayout = findViewById<VideoControlLayout>(R.id.videoControlLayout)
//        videoControlLayout.exceptViewList(listOf(rv))
    }


}