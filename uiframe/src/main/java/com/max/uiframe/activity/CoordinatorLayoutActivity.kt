package com.max.uiframe.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.max.uiframe.R
import com.max.uiframe.adapter.ViewPagerAdapter


class CoordinatorLayoutActivity : AppCompatActivity() {
    private var viewPager: ViewPager2? = null
    private var tabLayout: TabLayout? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coording_layout)

        // 初始化 Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        // 初始化 ViewPager2 和 TabLayout
        viewPager = findViewById<ViewPager2>(R.id.view_pager)
        tabLayout = findViewById<TabLayout>(R.id.tab_layout)


        // 设置 ViewPager2 适配器
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager?.adapter = viewPagerAdapter


        // 绑定 TabLayout 和 ViewPager2
        TabLayoutMediator(
            tabLayout!!, viewPager!!
        ) { tab, position -> tab.setText("Tab " + (position + 1)) }.attach()
    }
}