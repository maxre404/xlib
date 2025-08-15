package com.ok.uiframe.widget

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.max.xlib.log.LogFile.Companion.log

class RecyclerViewGestureListener(
    context: Context?,
    private val recyclerView: RecyclerView,
    private val listener: OnItemGestureListener?
) : RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector

    private val longPressHandler = Handler(Looper.getMainLooper())
    private val longPressRunnable: Runnable? = null
    private var targetChild: View? = null // 记录被按下的子View (itemView)
    private var isLongPressing = false

    init {
        this.gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            // --- 修改点 1：在所有手势回调中加入检查 ---
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (targetChild != null && listener != null && !isLongPressing) {
                    // 只有当触摸点不在任何可点击子View上时，才触发itemView的单击
                    if (!isTouchInsideClickableViewRecursive(targetChild as ViewGroup, e)) {
                        listener.onSingleClick(
                            targetChild,
                            recyclerView.getChildAdapterPosition(targetChild!!)
                        )
                    }
                }
                return true
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                if (targetChild != null && listener != null) {
                    cancelLongPress()
                    if (!isTouchInsideClickableViewRecursive(targetChild as ViewGroup, e)) {
                        listener.onDoubleClick(
                            targetChild,
                            recyclerView.getChildAdapterPosition(targetChild!!)
                        )
                    }
                }
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                // 1. 找到触摸点下的 itemView
                val targetChild = recyclerView.findChildViewUnder(e.x, e.y)
                if (targetChild != null && listener != null) {
                    // 2. 检查是否触摸在可点击的子View上
                    if (!isTouchInsideClickableViewRecursive(targetChild as ViewGroup, e)) {
                        // 3. 根据坐标判断左右并回调
                        val position = recyclerView.getChildAdapterPosition(targetChild)
                        val itemX = e.x - targetChild.getLeft()
                        isLongPressing = true
                        if (itemX < targetChild.getWidth() / 2) {
                            listener.onItemLongPressLeft(targetChild, position)
                        } else {
                            listener.onItemLongPressRight(targetChild, position)
                        }
                    }
                }
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        targetChild = rv.findChildViewUnder(e.x, e.y)
        gestureDetector.onTouchEvent(e) // GestureDetector 仍然需要分析所有事件
        when (e.action) {
            MotionEvent.ACTION_DOWN -> isLongPressing = false
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isLongPressing && listener != null && targetChild != null) {
                    val position = rv.getChildAdapterPosition(targetChild!!)
                    if (position != RecyclerView.NO_POSITION) { // 防止 targetChild 已经被回收
                        val rvMiddle = rv.width / 2f
                        val isLeftSide = e.x < rvMiddle

                        if (isLeftSide) {
                            listener.onItemLongPressLeftUp(targetChild, position)
                        } else {
                            listener.onItemLongPressRightUp(targetChild, position)
                        }
                    }
                }
                // 清理状态
                isLongPressing = false
                targetChild = null
            }
        }
        return false // 依然返回false，让事件可以继续传递
    }

    /**
     * 【核心】使用递归深度检查触摸点下方是否有任何可点击的 View
     * @param parent 要检查的 ViewGroup (通常是 itemView)
     * @param event  原始的触摸事件 (坐标是相对于 RecyclerView 的)
     * @return 如果触摸点落在任何一个可点击的子孙 View 上，返回 true
     */
    private fun isTouchInsideClickableViewRecursive(
        parent: ViewGroup,
        event: MotionEvent
    ): Boolean {
        // 原始事件坐标是相对于 RecyclerView 的，我们需要相对于 parent 的坐标
        val itemX = event.x - parent.left
        val itemY = event.y - parent.top

        // 从后向前遍历子 View，这与绘制顺序和事件分发顺序更一致
        for (i in parent.childCount - 1 downTo 0) {
            val child = parent.getChildAt(i)

            // 检查子 View 是否可见，并且触摸点是否在子 View 的范围内
            if (child.visibility == View.VISIBLE) {
                val childRect = Rect()
                child.getHitRect(childRect)
                if (childRect.contains(itemX.toInt(), itemY.toInt())) {
                    // 如果触摸点在子 View 范围内，检查该子 View 是否可点击
                    if (child.isClickable) {
                        return true
                    }

                    // 如果该子 View 是一个 ViewGroup，递归检查它的内部
                    if (child is ViewGroup) {
                        // 为了递归，我们需要创建一个新的 MotionEvent，其坐标是相对于这个 child ViewGroup 的
                        val childEvent = MotionEvent.obtain(event)
                        childEvent.offsetLocation(
                            (-parent.left - child.getLeft()).toFloat(),
                            (-parent.top - child.getTop()).toFloat()
                        )

                        val consumed = isTouchInsideClickableViewRecursive(child, childEvent)
                        childEvent.recycle() // 及时回收 MotionEvent
                        if (consumed) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    // onTouchEvent, onRequestDisallowInterceptTouchEvent, cancelLongPress 等方法保持不变...
    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    private fun cancelLongPress() {
        if (longPressRunnable != null) {
            longPressHandler.removeCallbacks(longPressRunnable)
        }
    }
}