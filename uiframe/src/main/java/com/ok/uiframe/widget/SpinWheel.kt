package com.ok.uiframe.widget

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.max.uiframe.R
import com.max.xlib.log.LogFile
import com.ok.uiframe.data.SpinWheelItem
import kotlin.math.cos
import kotlin.math.sin


class SpinWheel : View {
    private var wheelWidth = 0
    private var wheelHeight = 0
    private val dataList = ArrayList<SpinWheelItem>()
    private var paintFill: Paint? = null

    // 边框画笔
    private var paintStroke: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#C4A14F")
        strokeWidth = 10f
    }
    private var paintLine: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }
    private var rectF: RectF? = null
    private var radius = 0f
    private val sectorCount = 8 // 扇形数量
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FE2C55")
        textAlign = Paint.Align.CENTER
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context)
    }

    private fun initView(context: Context) {
        paintText.textSize = context.resources.getDimensionPixelSize(R.dimen.sp_11).toFloat()
        dataList.clear()
        dataList.add(SpinWheelItem().apply {
            content = "真心话1"
        })
        dataList.add(SpinWheelItem().apply {
            content = "大冒险"
        })
        dataList.add(SpinWheelItem().apply {
            content = "叫老公"
        })
        dataList.add(SpinWheelItem().apply {
            content = "112233"
        })
        dataList.add(SpinWheelItem().apply {
            content = "测试"
        })
        dataList.add(SpinWheelItem().apply {
            content = "大冒险"
        })
        dataList.add(SpinWheelItem().apply {
            content = "真心话"
        })
        dataList.add(SpinWheelItem().apply {
            content = "叫老公"
        })

        // 填充扇形画笔
        paintFill = Paint()
        paintFill?.setStyle(Paint.Style.FILL)
        paintFill?.setAntiAlias(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        wheelWidth = MeasureSpec.getSize(widthMeasureSpec)
        wheelHeight = MeasureSpec.getSize(heightMeasureSpec)
        // 确保设置测量结果
        setMeasuredDimension(wheelWidth, wheelHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (dataList.isNotEmpty()) {
            val centerX = wheelWidth / 2f
            val centerY = wheelHeight / 2f
            radius = (Math.min(centerX, centerY) - 20)

            rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
            val sweepAngle = 360f / dataList.size

            val color1 = Color.parseColor("#FFF5DA") // 浅黄色
            val color2 = Color.parseColor("#FFFBEF") // 乳白色
            canvas.save()
            canvas.rotate(270 - (sweepAngle) / 2, centerX, centerY)
            // 1️⃣ 依次绘制 8 片扇形
            var startAngle = 0f
            for (i in 0 until dataList.size) {
                paintFill?.color = if (i % 2 == 0) color1 else color2
                rectF?.let {
                    canvas.drawArc(it, startAngle, sweepAngle, true, paintFill!!)
                }
                // 计算扇形中心点
                val angleRad = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                val textCenterX = centerX + radius * 0.5f * cos(angleRad).toFloat()
                val textCenterY = centerY + radius * 0.5f * sin(angleRad).toFloat()
                // 计算文字绘制起点
                val text = dataList[i].content
                val textWidth = paintText.measureText(text)


                // 计算文字垂直方向的偏移量，使其垂直居中
                val fontMetrics: Paint.FontMetrics = paintText.getFontMetrics()
                val textVerticalOffset =
                    (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent
                val textStartX = textCenterX - textWidth / 2
                val textStartY = textCenterY + textVerticalOffset
                canvas.save()
                // 旋转 + 绘制文字
                canvas.rotate(startAngle - 180 + sweepAngle / 2, textCenterX, textCenterY)
                canvas.drawText(text, textStartX, textStartY, paintText)
                canvas.restore()

                startAngle += sweepAngle
            }

            startAngle = 0f
            for (i in 0 until sectorCount) {
                val angleRad = Math.toRadians(startAngle.toDouble())
                val endX = centerX + radius * cos(angleRad).toFloat()
                val endY = centerY + radius * sin(angleRad).toFloat()
                canvas.drawLine(centerX, centerY, endX, endY, paintLine!!)
                startAngle += sweepAngle
            }

            // 2️⃣ 画金色边框
            canvas.drawCircle(centerX, centerY, radius, paintStroke)

            canvas.restore()
        }
    }

    fun toSpin(degree: Float) {
        rotation = 0f;
        animate().rotation(degree)
            .setDuration(2000)
            .setInterpolator(DecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    LogFile.log("动画执行完毕")
                }
            })
            .start()

    }

}
