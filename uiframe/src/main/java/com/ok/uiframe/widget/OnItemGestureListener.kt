package com.ok.uiframe.widget

import android.view.View

interface OnItemGestureListener {
    fun onSingleClick(view: View?, position: Int)
    fun onDoubleClick(view: View?, position: Int)
    fun onItemLongPressLeft(view: View?, position: Int)
    fun onItemLongPressRight(view: View?, position: Int)
    fun onItemLongPressLeftUp(view: View?, position: Int)
    fun onItemLongPressRightUp(view: View?, position: Int)
}