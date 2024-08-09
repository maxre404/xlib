package com.max.uiframe.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.util.Pair
import android.view.View
import com.max.uiframe.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 印度彩 1分winGo 走势图
 */
class OneWinGoTrendChartView : View {
    constructor(context: Context?) : super(context){
        initData(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initData(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        initData(context)
    }
    private val tag = OneWinGoTrendChartView::class.java.simpleName
    /*item 高度*/
    private var itemHeight = 0

    /*期数距离左边距*/
    private var issueMarginLeft = 0

    /*第一个球距离左边距*/
    private var ballMarginLeft = 0
    /*整合第一个球距离右边的边距*/
    private var zhengHeMarginRight = 0
    /*球体的半径 */
    private var ballRadius = 0
    /*球体之间的间距*/
    private var ballSpace = 0
    private val linePath = Path()
    private val linePaint = Paint().apply {
        color = Color.parseColor("#fffe3c54")
        style = Paint.Style.STROKE
        strokeWidth = resources.getDimensionPixelSize(R.dimen.dp_1).toFloat()
    }

    private val blockWhitePaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
    }
   private val blockGrayPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#fff7f8fc")
    }
    private val issuePaint = Paint().apply {
        isAntiAlias = true
        Color.parseColor("#ff212121")
        textSize = resources.getDimensionPixelSize(R.dimen.sp_11).toFloat()
    }
    private val ballNumberPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#ffaaaaaa")
        textSize = resources.getDimensionPixelSize(R.dimen.sp_12).toFloat()
    }
    private val winNumberRedPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#fffe3c54")
        textSize = resources.getDimensionPixelSize(R.dimen.sp_12).toFloat()
        typeface = Typeface.DEFAULT_BOLD
    }
    private val winNumberGreenPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#ff4add7f")
        textSize = resources.getDimensionPixelSize(R.dimen.sp_12).toFloat()
        typeface = Typeface.DEFAULT_BOLD
    }
    private val zhengHePaint = Paint().apply {
        isAntiAlias = true
    }
    private val zhengHeStrPaint = Paint().apply {
        isAntiAlias = true
        textSize = resources.getDimensionPixelSize(R.dimen.sp_12).toFloat()
        typeface = Typeface.DEFAULT_BOLD
        color = Color.WHITE
    }
    private val orangeColor = Color.parseColor("#FFFFAA57")
    private val greenColor = Color.parseColor("#ff28D867")
    private val redColor = Color.parseColor("#FFFE3C54")
    private val blueColor = Color.parseColor("#FF5FABFF")
    private val unselectBall: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_chips_1mwingo_unselect)
    private val winNumber0: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_chips_1mwingo_select_0)
    private val winNumber5: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_chips_1mwingo_select_5)
    private val winNumberRed: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_chips_1mwingo_select_red)
    private val winNumberGreen: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_chips_1mwingo_select_green)

    private fun initData(context: Context?) {
        issueMarginLeft = context?.resources?.getDimensionPixelSize(R.dimen.dp_15) ?: 0
        itemHeight = context?.resources?.getDimensionPixelSize(R.dimen.dp_40) ?: 0
        ballMarginLeft = context?.resources?.getDimensionPixelSize(R.dimen.dp_109) ?: 0
        zhengHeMarginRight = context?.resources?.getDimensionPixelSize(R.dimen.dp_54) ?: 0
        ballRadius = context?.resources?.getDimensionPixelSize(R.dimen.dp_9) ?: 0
        ballSpace =  context?.resources?.getDimensionPixelSize(R.dimen.dp_2) ?: 0
    }

    private inner class Ball {
        var isWin: Boolean = false
        /*球心的坐标*/
        var location:Point = Point(0,0)
        var number:String = ""
        /*开奖号码*/
        var winNumber:String = ""
    }

    private inner class TrendChartItem {
        /*期数*/
        var issue: String? = ""
        var betNumbers: ArrayList<Ball> = ArrayList()
        var zhengHeNumbers: ArrayList<Ball> = ArrayList()
    }

    private val trendChartList: ArrayList<TrendChartItem> = ArrayList()


    /**
     *
     * <Pair<String?, String?>   第一个为期数 第二个为开奖号码
     *
     */
    fun setTrendChartData(data: ArrayList<Pair<String?, String?>>) {
        Log.d(tag, "setTrendChartData: 开始设置值哦 itemHeight:$itemHeight")
        trendChartList.clear()
        GlobalScope.launch(Dispatchers.IO) {
            data.forEachIndexed { index, pair ->
                val issue = pair.first
                val winNumber = pair.second
                val item = TrendChartItem()
                item.issue = issue
                val ballList = ArrayList<Ball>()
                val zhengHeList = ArrayList<Ball>()
                val startY = (index*itemHeight)
                for (i in 0..9) {
                    val ball = Ball()
                    ball.isWin = i.toString() == winNumber
                    ball.number = i.toString()
                    ball.winNumber = winNumber?:""
                    val centerX = ballMarginLeft+(i*(ballRadius * 2 + ballSpace)) +ballRadius
                    ball.location = Point(centerX, startY+(itemHeight/2))
                    ballList.add(ball)
                }
                for (i in 0 until 2){
                    val centerX = (i*(ballRadius * 2 + ballSpace)) +ballRadius
                    val ball = Ball()
                    ball.location = Point(centerX, startY+(itemHeight/2))
                    ball.winNumber = winNumber?:""
                    when(i){
                        0 -> {
                            ball.number = getBigOrSmallStr(winNumber)
                        }
                        1 -> {
                            ball.number = getOddOrEvenStr(winNumber)
                        }
                    }
                    zhengHeList.add(ball)
                }
                item.betNumbers = ballList
                item.zhengHeNumbers = zhengHeList
                trendChartList.add(item)
            }


            withContext(Dispatchers.Main) {
                requestLayout()
            }
        }
    }

    private fun getBigOrSmallStr(winNumber: String?):String{
        //todo 多语言处理
        return if ((winNumber?.toInt()?:0)>=5){
            "B"
        }else{
            "S"
        }
    }
    private fun getOddOrEvenStr(winNumber: String?):String{
        //todo 多语言处理
        return if ((winNumber?.toInt()?:0)%2 == 0){
            "E"
        }else{
            "O"
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = itemHeight * trendChartList.size
        setMeasuredDimension(width, height)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBlock(canvas)
        drawRow(canvas)
        drawLine(canvas)
        drawWinBall(canvas)
    }

    private fun drawWinBall(canvas: Canvas?) {
        trendChartList.forEachIndexed { _, trendChartItem ->
            trendChartItem.betNumbers.forEachIndexed { _, ball ->
                if (ball.isWin){
                    val ballCenterX = ball.location.x.toFloat()
                    val ballCenterY = ball.location.y.toFloat()
                    when(ball.number.toInt()){
                        0 -> canvas?.drawBitmap(winNumber0,ballCenterX-ballRadius,ballCenterY-ballRadius,null)
                        5 -> canvas?.drawBitmap(winNumber5,ballCenterX-ballRadius,ballCenterY-ballRadius,null)
                        in 1..4 , in 6..9  -> {
                            if (ball.number.toInt() % 2 == 0){
                                canvas?.drawBitmap(winNumberRed,ballCenterX-ballRadius,ballCenterY-ballRadius,null)
                                // 获取文字宽度
                                val textWidth = winNumberRedPaint.measureText(ball.number)
                                // 计算文字的基线位置，使其垂直居中
                                val textHeight = winNumberRedPaint.fontMetrics.descent - winNumberRedPaint.fontMetrics.ascent
                                val textOffsetY = (textHeight / 2) - winNumberRedPaint.fontMetrics.descent
                                canvas?.drawText(ball.number, ballCenterX - textWidth / 2, ballCenterY + textOffsetY+1, winNumberRedPaint)
                            }else{
                                canvas?.drawBitmap(winNumberGreen,ballCenterX-ballRadius,ballCenterY-ballRadius,null)
                                // 获取文字宽度
                                val textWidth = winNumberGreenPaint.measureText(ball.number)
                                // 计算文字的基线位置，使其垂直居中
                                val textHeight = winNumberGreenPaint.fontMetrics.descent - winNumberGreenPaint.fontMetrics.ascent
                                val textOffsetY = (textHeight / 2) - winNumberGreenPaint.fontMetrics.descent
                                canvas?.drawText(ball.number, ballCenterX - textWidth / 2, ballCenterY + textOffsetY+1, winNumberGreenPaint)
                            }
                        }

                    }

                }

            }
        }
    }

    private fun drawRow(canvas: Canvas?) {
        trendChartList.forEachIndexed { index, trendChartItem ->
            val centerY = index*itemHeight+(itemHeight/2)
            drawIssue(trendChartItem, canvas, centerY)
            drawUniversalBall(trendChartItem, canvas)
            drawZhengHeBall(trendChartItem, canvas)
        }
    }

    private fun drawZhengHeBall(
        trendChartItem: TrendChartItem,
        canvas: Canvas?,
    ) {
        trendChartItem.zhengHeNumbers.forEachIndexed { index, ball ->
            val ballCenterX = (width-zhengHeMarginRight)+ball.location.x.toFloat()
            val ballCenterY = ball.location.y.toFloat()
            if (index == 0) {
                if (ball.winNumber.toInt() >= 5) {
                    zhengHePaint.setColor(orangeColor)
                } else {
                    zhengHePaint.setColor(blueColor)
                }
            } else {
                if (ball.winNumber.toInt() % 2 == 0) {
                    zhengHePaint.setColor(redColor)
                } else {
                    zhengHePaint.setColor(greenColor)
                }
            }
            canvas?.drawCircle(ballCenterX,ballCenterY,ballRadius.toFloat(),zhengHePaint)

            val textWidth = zhengHeStrPaint.measureText(ball.number)
            val textHeight = zhengHeStrPaint.fontMetrics.descent - zhengHeStrPaint.fontMetrics.ascent
            val textOffsetY = (textHeight / 2) - zhengHeStrPaint.fontMetrics.descent
            canvas?.drawText(ball.number, ballCenterX - textWidth / 2, ballCenterY + textOffsetY, zhengHeStrPaint)
        }

    }

    /*绘制通用球体 未开奖的*/
    private fun drawUniversalBall(
        trendChartItem: TrendChartItem,
        canvas: Canvas?,
    ) {
        trendChartItem.betNumbers.forEachIndexed { _, ball ->

            if (!ball.isWin){
                val ballCenterX = ball.location.x.toFloat()
                val ballCenterY = ball.location.y.toFloat()
                canvas?.drawBitmap(unselectBall,ballCenterX-ballRadius,ballCenterY-ballRadius,null)
                // 获取文字宽度
                val textWidth = ballNumberPaint.measureText(ball.number)

                // 计算文字的基线位置，使其垂直居中
                val textHeight = ballNumberPaint.fontMetrics.descent - ballNumberPaint.fontMetrics.ascent
                val textOffsetY = (textHeight / 2) - ballNumberPaint.fontMetrics.descent
                canvas?.drawText(ball.number, ballCenterX - textWidth / 2, ballCenterY + textOffsetY, ballNumberPaint)
            }
        }
    }

    private fun drawIssue(
        trendChartItem: TrendChartItem,
        canvas: Canvas?,
        centerY: Int
    ) {
        val issue = trendChartItem.issue
        val textHeight = issuePaint.fontMetrics.descent - issuePaint.fontMetrics.ascent
        val textOffsetY = (textHeight / 2) - issuePaint.fontMetrics.descent
        canvas?.drawText(issue ?: "", issueMarginLeft.toFloat(), centerY + textOffsetY, issuePaint)
    }

    private fun drawLine(canvas: Canvas?) {
        linePath.reset()
        trendChartList.forEachIndexed { index, trendChartItem ->
           val winBall = trendChartItem.betNumbers.find { it.isWin }
            val ballCenterX = winBall?.location?.x?.toFloat()?:0f
            val ballCenterY = winBall?.location?.y?.toFloat()?:0f
            if (0==index){
                linePath.moveTo(ballCenterX,ballCenterY)
            }else{
                linePath.lineTo(ballCenterX,ballCenterY)
            }
        }
        canvas?.drawPath(linePath,linePaint)

    }

    /*绘制底色背景*/
    private fun drawBlock(canvas: Canvas?) {

        var startY = 0
        trendChartList.forEachIndexed { index, _ ->
            val blockRect = Rect(0, startY, width, startY + itemHeight)
            canvas?.drawRect(blockRect,if (index%2 == 0) blockWhitePaint else blockGrayPaint)
            startY += itemHeight
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unselectBall.recycle()
        winNumber0.recycle()
        winNumber5.recycle()
        winNumberRed.recycle()
        winNumberGreen.recycle()
    }

}
