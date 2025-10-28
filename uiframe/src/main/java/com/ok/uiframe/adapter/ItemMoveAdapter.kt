package com.ok.uiframe.adapter

interface ItemMoveAdapter {
    /**
     * 在数据源中交换两个位置的 Item，并通知 RecyclerView 刷新
     * @param fromPosition 拖动开始的位置
     * @param toPosition 拖动到的目标位置
     */
    fun onItemMove(fromPosition: Int, toPosition: Int)
}