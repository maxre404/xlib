package com.ok.uiframe.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.BindingAdapter
import com.drake.brv.annotaion.ItemOrientation
import com.drake.brv.listener.DefaultItemTouchCallback
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.max.uiframe.R
import com.max.xlib.log.LogFile
import com.ok.uiframe.adapter.AlbumDraggableAdapter
import com.ok.uiframe.data.DraggablePhotoAlbumData
import com.ok.uiframe.widget.DragOnlyItemTouchHelperCallback

class DragFragment:Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_frag_test, container, false)
        initView(view)
//        initDragView(view)
        return view
    }

    private fun initView(view: View?) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvDrag)

        val data = mutableListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8", "Item 9", "Item 10")
        val adapter = AlbumDraggableAdapter(data)

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
    @SuppressLint("ClickableViewAccessibility")
    fun  initDragView(view: View?){
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvDrag)
        val data = mutableListOf(DraggablePhotoAlbumData("Item 1"),DraggablePhotoAlbumData("Item 2"),DraggablePhotoAlbumData("Item 3"),DraggablePhotoAlbumData("Item 4"),DraggablePhotoAlbumData("Item 5"),DraggablePhotoAlbumData("Item 6", ItemOrientation.NONE),DraggablePhotoAlbumData("Item 7", ItemOrientation.NONE),DraggablePhotoAlbumData("Item 8", ItemOrientation.NONE),DraggablePhotoAlbumData("Item 9", ItemOrientation.NONE),)
        recyclerView?.grid(4)?.setup {
            addType<DraggablePhotoAlbumData>(R.layout.item_drag_photo_album)
            onCreate {
                val layoutItem = findView<LinearLayout>(R.id.layoutItem)
                layoutItem.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) { // 如果手指按下则开始拖拽
                        itemTouchHelper?.startDrag(this)
                    }
                    return@setOnTouchListener true
                }
            }
            onBind {
                val textView = findView<TextView>(R.id.item_text)
                textView.text = getModel<DraggablePhotoAlbumData>().data
            }
            itemTouchHelper = ItemTouchHelper(object : DefaultItemTouchCallback() {

                /**
                 * 当拖拽动作完成且松开手指时触发
                 */
                override fun onDrag(
                    source: BindingAdapter.BindingViewHolder,
                    target: BindingAdapter.BindingViewHolder
                ) {
                    super.onDrag(source, target)
                }

            })

        }?.models = data




    }


}