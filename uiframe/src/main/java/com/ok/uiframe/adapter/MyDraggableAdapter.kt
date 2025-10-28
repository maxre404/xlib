package com.ok.uiframe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.max.uiframe.R
import com.max.xlib.log.LogFile
import java.util.Collections

class MyDraggableAdapter(private val items: MutableList<String>) : 
    RecyclerView.Adapter<MyDraggableAdapter.MyViewHolder>(), ItemMoveAdapter { 

    // ... ViewHolder 和其他 Adapter 方法保持不变 ...
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = items[position]
    }

    override fun getItemCount() = items.size

    // ... 
    
    /**
     * 【实现 ItemMoveAdapter 接口】处理数据交换
     */
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        LogFile.log("fromPosition:$fromPosition  toPosition:$toPosition")
        // 1. 在数据集中交换位置
        // 1. 从原位置取出 Item (List.removeAt 会返回被移除的 Item)
        val itemToMove = items.removeAt(fromPosition)
        // 2. 将 Item 插入到新位置
        // List.add(index, element) 会将元素插入到指定索引，并将该位置及之后的所有元素后移
        items.add(toPosition, itemToMove)
//        Collections.swap(items, fromPosition, toPosition)
        // 2. 通知 Adapter 刷新界面
        notifyItemMoved(fromPosition, toPosition)
        LogFile.log("打印数组:$items")
    }
    
    // 内部类 MyViewHolder
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_text) 
    }
}