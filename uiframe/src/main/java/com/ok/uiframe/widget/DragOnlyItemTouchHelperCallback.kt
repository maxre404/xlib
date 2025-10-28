package com.ok.uiframe.widget

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ok.uiframe.adapter.ItemMoveAdapter
import java.util.Collections

/**
 * 专门处理 RecyclerView 拖动重新排序的 Callback
 * @param adapter 实现了拖动数据交换逻辑的 RecyclerView.Adapter
 */
class DragOnlyItemTouchHelperCallback<T>(
    private val adapter: T
) : ItemTouchHelper.Callback() where T : RecyclerView.Adapter<*>, T : ItemMoveAdapter {
    // T 必须是 RecyclerView.Adapter 并且实现 ItemMoveAdapter 接口

    /**
     * 【核心 1】确定支持的动作：只支持上下拖动
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView, 
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // 允许上下左右拖动
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        // 不允许任何滑动操作 (SWIPE)
        val swipeFlags = 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /**
     * 【核心 2】处理拖动移动事件
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // 调用 Adapter 接口方法来执行数据交换和界面更新
        adapter.onItemMove(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
        return true // 返回 true 表示移动成功
    }

    /**
     * 【核心 3】处理滑动事件：返回空，因为我们只需要拖动
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 不做任何处理
    }

    /**
     * 可选：定义拖动交互的自定义效果
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // 在拖动开始时
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            // 示例：改变拖动中的 Item 的背景颜色，让用户知道它正在被选中
            viewHolder?.itemView?.alpha = 0.8f 
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * 可选：拖动结束后恢复效果
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        // 示例：恢复透明度
        viewHolder.itemView.alpha = 1.0f 
    }
}