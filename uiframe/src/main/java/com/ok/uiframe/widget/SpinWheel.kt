package com.ok.uiframe.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
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
    private var arcPaint: Paint? = null //绘制扇形画笔
    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null) // 关闭硬件加速，保证阴影生效
    }
    // 边框画笔
    private var paintStroke: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#c2b07e") // 圆圈颜色
        style = Paint.Style.STROKE // 填充模式
        setShadowLayer(10f, 0f, 0f, Color.argb(100, 0, 0, 0)) // 设置阴影
    }
    private var paintLine: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }
    private var rectF: RectF? = null
    private var radius = 0f
    private val sectorCount = 8 // 扇形数量
    private val paintText = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FE2C55")
        textAlign = Paint.Align.CENTER
    }

    private val color1 = Color.parseColor("#FFFBEF")
    private val color2 = Color.parseColor("#FFF5DA")
    private val color3 = Color.parseColor("#FFF2CD")

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
    private fun initView(context: Context) {
        paintText.textSize = context.resources.getDimensionPixelSize(R.dimen.sp_11).toFloat()
        dataList.clear()
        dataList.add(SpinWheelItem().apply {
            content = "真心话1这是哪里哦哈卡"
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
//        dataList.add(SpinWheelItem().apply {
//            content = "叫老公"
//        })

        // 填充扇形画笔
        arcPaint = Paint()
        arcPaint?.style = Paint.Style.FILL
        arcPaint?.isAntiAlias = true
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
            radius = centerX.coerceAtMost(centerY)
            if (null==rectF){
                rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
            }

            val sweepAngle = 360f / dataList.size
            canvas.save()
            canvas.rotate(270 - (sweepAngle) / 2, centerX, centerY)
            // 依次绘制 扇形
            var startAngle = 0f
            for (i in 0 until dataList.size) {
                arcPaint?.color = getColor(i)
                rectF?.let {
                    canvas.drawArc(it, startAngle, sweepAngle, true, arcPaint!!)
                }
                // 计算扇形中心点
                val angleRad = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                val textCenterX = centerX + radius * 0.4f * cos(angleRad).toFloat()
                val textCenterY = centerY + radius * 0.4f * sin(angleRad).toFloat()


                // 计算文本最大宽度
                val textMaxWidth = (radius * 0.58f).toInt()


                // **使用 StaticLayout 控制最大 2 行 + 省略号**
                val staticLayout = StaticLayout.Builder.obtain(dataList[i].content, 0, dataList[i].content.length, paintText,
                    textMaxWidth
                )
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setMaxLines(2) // 限制最多 2 行
                    .setEllipsize(TextUtils.TruncateAt.END) // 超过 2 行时才省略
                    .setLineSpacing(0f, 1f)
                    .setIncludePad(false)
                    .build()
                // 计算文本高度
                val textHeight = staticLayout.height.toFloat()


                // 计算起始位置，使文本居中
                val textStartX = textCenterX - textMaxWidth / 2f
                val textStartY = textCenterY - textHeight / 2f

                canvas.save()

                // 旋转文本，使其与扇形对齐
                canvas.rotate(startAngle + sweepAngle / 2-180, textCenterX, textCenterY)
                canvas.translate(textStartX, textStartY)
                staticLayout.draw(canvas)
                canvas.restore()
                startAngle += sweepAngle
            }
            drawLineAndCircle(centerX, centerY, canvas, sweepAngle)
        }
    }

    //绘制扇形线条以及外圆
    private fun drawLineAndCircle(
        centerX: Float,
        centerY: Float,
        canvas: Canvas,
        sweepAngle: Float
    ) {
        var startAngle = 0f
        for (i in 0 until sectorCount) {
            val angleRad = Math.toRadians(startAngle.toDouble())
            val endX = centerX + radius * cos(angleRad).toFloat()
            val endY = centerY + radius * sin(angleRad).toFloat()
            canvas.drawLine(centerX, centerY, endX, endY, paintLine!!)
            startAngle += sweepAngle
        }
        canvas.drawCircle(centerX, centerY, radius, paintStroke)
        canvas.restore()
    }
    public fun getWheelList():ArrayList<SpinWheelItem>{
        return dataList
    }

    private fun getColor(index:Int):Int{
//        偶数时双色交替：#FFFBEF / #FFF5DA
//        奇数时三色循环交替：#FFFBEF / #FFF5DA / #FFF2CD（7个时，第七个为#FFF5DA）
        return if (dataList.size % 2 == 0) {//扇形区域为偶数
            if (index%2==0)color1 else color2
        } else {//扇形个数为奇数
            if (dataList.size == 7 && index == 6) {
                color2
            }else{
                when(index%3){
                    0-> color1
                    1-> color2
                    else-> color3
                }
            }

        }
    }

    fun toSpinAnimation(degree: Float) {
        rotation = 0f;
        animate().rotation(degree)
            .setDuration(2000)
            .setInterpolator(DecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    LogFile.log("动画执行完毕")
                }

                override fun onAnimationCancel(animation: Animator?) {
                    super.onAnimationCancel(animation)
                }

                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                }
            })
            .start()

    }

}
