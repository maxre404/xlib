package com.ok.uiframe.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout

/** 此layout会绘制向外扩散的圈 如果旋转第一个子view */
class RippleLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    init {
        setWillNotDraw(false) // 关键！让 FrameLayout 执行 onDraw
    }
    private var radius = 0f
    private var alpha = 255

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        interpolator = LinearInterpolator()
        duration = 700L
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            val fraction = it.animatedValue as Float
            radius = maxRadius * fraction
            alpha = (255 * (1 - fraction)).toInt()
            if (childCount>0){
                // 更新子视图的旋转
                val rotationAngle = 360f * fraction // 每一帧旋转的角度
                val childView = getChildAt(0)
                childView.rotation = rotationAngle
            }
            invalidate()
        }
    }

    private var maxRadius = 0f

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startSpinAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelSpinAnimation()
    }
     fun cancelSpinAnimation(){
        animator.cancel()
    }
    fun startSpinAnimation(){
        post {
            maxRadius = (width.coerceAtMost(height) / 2).toFloat()
            animator.start()
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE){
            startSpinAnimation()
        }else{
            cancelSpinAnimation()
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.alpha = alpha
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }



}