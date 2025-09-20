package com.ok.uiframe.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/** 正在直播中的动画view */
class LiveStreamingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#fffe2c55") // 自定义颜色
    }

    private val barCount = 3
    private val barWidth = 4f // 固定竖杠的宽度
    private val barHeights = FloatArray(barCount) { 0f }

    private var barSpacing = 0f // 动态计算
    private var animator: ValueAnimator? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        barSpacing = (w - barCount * barWidth) / (barCount - 1).toFloat()
        if (barSpacing < 0) barSpacing = 0f // 避免负数
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var startX = 0f
        val centerY = height / 2f

        for (i in 0 until barCount) {
            val currentHeight = barHeights[i]
            val top = centerY - currentHeight / 2
            val bottom = centerY + currentHeight / 2

            canvas.drawRect(
                startX,
                top,
                startX + barWidth,
                bottom,
                paint
            )

            startX += barWidth + barSpacing
        }
    }

    private fun startAnimation() {
        if (animator != null) return
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 600
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()

            addUpdateListener { animation ->
                val fraction = animation.animatedFraction
                for (i in 0 until barCount) {
                    val progress = (fraction + i * 0.2f) % 1.0f
                    barHeights[i] =
                        (height * (0.2f + 0.8f * kotlin.math.abs(kotlin.math.sin(progress * Math.PI)))).toFloat()
                }
                invalidate()
            }

            start()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        cancelAnimation()
        super.onDetachedFromWindow()
    }
    private fun  cancelAnimation(){
        animator?.cancel()
        animator = null
    }
}