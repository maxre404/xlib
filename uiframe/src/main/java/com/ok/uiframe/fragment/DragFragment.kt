package com.ok.uiframe.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.max.uiframe.R
import com.max.xlib.log.LogFile
import com.ok.uiframe.adapter.MyDraggableAdapter
import com.ok.uiframe.widget.DragOnlyItemTouchHelperCallback

class DragFragment:Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_frag_test, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvDrag)

        val data = mutableListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        val adapter = MyDraggableAdapter(data)

// 【！！！关键步骤：安全配置 RecyclerView！！！】
        recyclerView?.let { rv ->

            // 1. 设置 LayoutManager (告诉 RecyclerView 如何布局 Item)
            // 假设您想要一个垂直滚动列表
            rv.layoutManager = GridLayoutManager(rv.context,4)

            // 2. 设置 Adapter (将数据绑定到 View 上)
            rv.adapter = adapter

            // ----------------------------------------------------
            // 以下是您原有的拖动配置代码
            // ----------------------------------------------------

            // 3. 创建 Callback 实例
            // 注意：确保 DragOnlyItemTouchHelperCallback 构造函数与您的定义匹配
            val callback = DragOnlyItemTouchHelperCallback(adapter)

            // 4. 创建 ItemTouchHelper 实例
            val touchHelper = ItemTouchHelper(callback)

            // 5. 附加到 RecyclerView
            touchHelper.attachToRecyclerView(rv)
        }
        view?.findViewById<Button>(R.id.btnSubmit)?.setOnClickListener {
            LogFile.log("获取最新值:$data")
        }
    }


}