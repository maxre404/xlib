package com.ok.uiframe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.max.uiframe.R
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
        // 1. 在数据集中交换位置
        Collections.swap(items, fromPosition, toPosition)

        // 2. 通知 Adapter 刷新界面
        notifyItemMoved(fromPosition, toPosition)
    }
    
    // 内部类 MyViewHolder
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_text) 
    }
}