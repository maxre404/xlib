package com.ok.uiframe.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.view.GestureDetectorCompat
import kotlin.math.abs

class TouchDelegateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var downX = 0f
    private var downY = 0f
    private var isDragging = false

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            // 单击
            performClick()
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            // 双击
            Log.d("Gesture", "Double Tap")
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            // 长按
            Log.d("Gesture", "Long Press")
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true // 必须返回 true 才能继续接收后续事件
        }
    }
    private val gestureDetector = GestureDetectorCompat(context, this.gestureListener)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                isDragging = false
                parent.requestDisallowInterceptTouchEvent(true) // 阻止父级 RecyclerView 抢事件
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = abs(event.x - downX)
                val dy = abs(event.y - downY)
                if (!isDragging && (dx > touchSlop || dy > touchSlop)) {
                    // 超过滑动阈值，交给父 View（RecyclerView）
                    isDragging = true
                    parent.requestDisallowInterceptTouchEvent(false) // 允许 RecyclerView 接管事件
                    return false
                }
            }
        }

        return true // 自己消费点击/长按
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
