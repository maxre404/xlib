package com.ok.uiframe.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class GestureSupportRecyclerView : RecyclerView {
    private var startX = 0f
    private var startY = 0f
    private var gestureListener: OnItemTouchListener? = null


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
                // 请求父控件不要拦截触摸事件，确保后续的MOVE事件可以被接收到
                parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                val endX = ev.x
                val endY = ev.y
                val distanceX = abs((endX - startX).toDouble()).toFloat()
                val distanceY = abs((endY - startY).toDouble()).toFloat()

                // 1. 左右滑动交由父控件处理
                if (distanceX > distanceY) {
                    // 水平滑动，请求父控件拦截事件
                    parent.requestDisallowInterceptTouchEvent(false)
                    return false // 不再处理后续事件
                } else {
                    // 垂直滑动
                    // 2. 滑动到顶部和底部时交由父控件处理
                    if (isAtTop && endY > startY) { // 在顶部且继续向下滑动
                        parent.requestDisallowInterceptTouchEvent(false)
                    } else if (isAtBottom && endY < startY) { // 在底部且继续向上滑动
                        parent.requestDisallowInterceptTouchEvent(false)
                    } else {
                        // 其他情况，RecyclerView 自己处理滑动
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->                 // 事件结束时，恢复父控件的默认拦截行为
                parent.requestDisallowInterceptTouchEvent(false)
        }
        return super.dispatchTouchEvent(ev)
    }

    private val isAtTop: Boolean
        /**
         * 判断是否滑动到了顶部
         *
         * @return boolean
         */
        get() = !canScrollVertically(-1)

    private val isAtBottom: Boolean
        /**
         * 判断是否滑动到了底部
         *
         * @return boolean
         */
        get() = !canScrollVertically(1)


    // ========== 2. 设置手势监听的公开方法 ==========
    fun setOnItemGestureListener(listener: OnItemGestureListener?) {
        // 先移除旧的监听器，防止重复添加
        if (this.gestureListener != null) {
            this.removeOnItemTouchListener(gestureListener!!)
        }

        if (listener != null) {
            this.gestureListener = RecyclerViewGestureListener(context, this, listener)
            this.addOnItemTouchListener(this.gestureListener!!)
        }
    }
}