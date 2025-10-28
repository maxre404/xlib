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
    // 缩放比例，例如放大 5%
    private val DRAG_SCALE = 1.05f
    // 判断该位置的 item 是否可拖动
    private fun isPositionDraggable(position: Int): Boolean {
        // 示例：前两个禁止拖动
        return position < 6
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val position = viewHolder.bindingAdapterPosition
        if (position == RecyclerView.NO_POSITION) {
            return makeMovementFlags(0, 0)
        }

        // 如果该位置不可拖动，禁用所有拖拽动作
        return if (isPositionDraggable(position)) {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            makeMovementFlags(dragFlags, 0)
        } else {
            makeMovementFlags(0, 0)
        }
    }

    /**
     * 核心：处理拖动事件
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPos = viewHolder.bindingAdapterPosition
        val toPos = target.bindingAdapterPosition

        // 如果目标位置是不可拖动的，就不允许移动
        if (!isPositionDraggable(toPos)) {
            // 返回 false 告诉系统“我没移动”，系统会自动恢复原位（回弹）
            return false
        }

        // 如果自己就是禁止拖动项，也不处理
        if (!isPositionDraggable(fromPos)) {
            return false
        }

        // 执行真正的交换逻辑
        adapter.onItemMove(fromPos, toPos)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 不处理滑动
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.apply {
                alpha = 0.8f // 保持半透明
                scaleX = 1.3f // 水平放大
                scaleY = DRAG_SCALE // 垂直放大
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
//        viewHolder.itemView.alpha = 1.0f
        // 恢复 ItemView 的原始状态
        viewHolder.itemView.apply {
            alpha = 1.0f  // 恢复不透明
            scaleX = 1.0f // 恢复原始大小
            scaleY = 1.0f // 恢复原始大小
        }
    }
}