package com.max.uiframe.widget

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout


class DraggableView : ConstraintLayout {
    private var dX = 0f
    private var dY = 0f
    private var parentWidth = 0
    private var parentHeight = 0

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        // 初始化任何需要的资源
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 获取父布局的宽度和高度
        post {
            val parent = parent as ViewGroup
            parentWidth = parent.width
            parentHeight = parent.height
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录初始位置
                dX = x - event.rawX
                dY = y - event.rawY
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                // 拖动时更新位置
                var newX = event.rawX + dX
                var newY = event.rawY + dY

                // 限制左右边界
                if (newX < 0) {
                    newX = 0f
                } else if (newX > parentWidth - width) {
                    newX = (parentWidth - width).toFloat()
                }

                // 限制上下边界
                if (newY < 0) {
                    newY = 0f
                } else if (newY > parentHeight - height) {
                    newY = (parentHeight - height).toFloat()
                }
                x = newX
                y = newY
                return true
            }

            MotionEvent.ACTION_UP -> {
                // 判断吸附到最近的边缘
                val centerX = x + width / 2
                val targetX = if (centerX < parentWidth / 2) {
                    0f // 吸附到左边
                } else {
                    (parentWidth - width).toFloat() // 吸附到右边
                }
                // 使用动画吸附到边缘
                val animatorX = ObjectAnimator.ofFloat(this, "x", x, targetX)
                animatorX.setDuration(300)
                animatorX.start()
                return true
            }

            else -> return super.onTouchEvent(event)
        }
    }
}
