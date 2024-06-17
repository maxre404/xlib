package com.max.uiframe.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.View;

import androidx.annotation.Nullable;

import com.max.uiframe.R;
import com.max.uiframe.TreasureSnatchProgressState;
import com.max.uiframe.util.QMUIDisplayHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TreasureSnatchProgressBar extends View {
    /*当isExpand = true时 进度条的高度*/
    private float progressBarHeight =0f;
    /*当isExpand = false时 进度条的高度 设计图当前默认为3dp*/
    private float progressBarFoldHeight = 0f;
    private float cursorTextSize;
    /*当前金额字体大小*/
    private float currentAmountTextSize;
    private float radius = 0f;
    private float failBlockRadius = 0f;
    private boolean isExpand = true;//是否是展开时的布局 如果为false就只有一个进度条
    private final Paint progressBgPaint = new Paint();//绘制进度条的底部
    private final Paint progressPaint = new Paint();//绘制进度条
    private final Paint failBlockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//绘制失败矩形
    private final Paint failBlockStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//绘制失败矩形描边
    private final  Paint failBlockTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//绘制失败矩形文字
    private Size failBlockSize;//失败矩形大小 默认为 64dp*24dp

    private Paint cursorFillPaint = new Paint();
    private Paint cursorStrokePaint;

    private Paint cursonTextPaint;
    private Paint currentAmountPaint;
    private double prizePool = 100;//奖池金额
    private double currentAmount = 80;
    private int currentAmountMargin = 0;//当前金额到游标指示器的距离 目前设计图是4dp
    private int progressToTopMargin = 0;//进度条到顶部距离
    private final RectF progressBackGroundRectF = new RectF();
    private final RectF progressRectF = new RectF();
    private final RectF cursorRectF = new RectF();
    private final RectF failBlockRectF = new RectF();
    double progressRealWidth = 0;
    double currentProgressWidth = 0;
    private static final String TAG = "debug11";
    private Bitmap indicatorBitmap;
    private Bitmap goldBitMap;
    int generalColor = Color.parseColor("#fffe2c55");
    int progressFailedColor = Color.parseColor("#ffaeaeae");

    int progressYellowColor = Color.parseColor("#ffffc93e");
    int progressGreenColor = Color.parseColor("#ff45ba3b");
    int progressRedColor = Color.parseColor("#fffe2c55");
    /**失败时游标填充颜色*/
    int cursorFailedColor = Color.parseColor("#ff9a9a9a");
    /**当前金额失败颜色*/
    int currentAmountFailedColor = Color.parseColor("#b2ffffff");

    private TreasureSnatchProgressState progressState = TreasureSnatchProgressState.COLLECTING;

    public TreasureSnatchProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TreasureSnatchProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        progressBarFoldHeight = QMUIDisplayHelper.dp2px(context,3);
        currentAmountMargin = QMUIDisplayHelper.dp2px(context,4);
        failBlockRadius = QMUIDisplayHelper.dp2px(context,2);
        initAttr(context, attrs);
        initPaint(context);
    }

    private void initPaint(Context context) {
        progressBgPaint.setColor(Color.parseColor("#80000000"));
        progressBgPaint.setAntiAlias(true);
        progressBgPaint.setStyle(Paint.Style.FILL);

        progressPaint.setColor(generalColor);
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.FILL);

        cursorFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cursorFillPaint.setColor(generalColor); // 设置内容颜色

        cursorStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cursorStrokePaint.setColor(Color.WHITE); // 设置描边颜色
        cursorStrokePaint.setStyle(Paint.Style.STROKE);
        cursorStrokePaint.setStrokeWidth(QMUIDisplayHelper.dp2px(context,1)); // 设置描边宽度

        cursonTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cursonTextPaint.setColor(Color.WHITE); // 设置描边颜色
        cursonTextPaint.setTextSize(cursorTextSize);

        currentAmountPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentAmountPaint.setTextSize(currentAmountTextSize);

    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TreasureSnatchProgressBar);
       float cursorHeight = typedArray.getDimension(R.styleable.TreasureSnatchProgressBar_failBlockHeight,0);
        float cursorWidth = typedArray.getDimension(R.styleable.TreasureSnatchProgressBar_failBlockWidth, 0);
        failBlockSize = new Size((int) cursorWidth, (int)cursorHeight);
        cursorTextSize = typedArray.getDimension(R.styleable.TreasureSnatchProgressBar_cursorTextSize,0f);
        currentAmountTextSize = typedArray.getDimension(R.styleable.TreasureSnatchProgressBar_currentAmountTextSize,0f);
        radius = QMUIDisplayHelper.dp2px(context,9);
        int imageResId = typedArray.getResourceId(R.styleable.TreasureSnatchProgressBar_indicatorSrc, -1);
        int imageGoldResId = typedArray.getResourceId(R.styleable.TreasureSnatchProgressBar_oneToWinGoldSrc, -1);
        if (-1 != imageResId){
            indicatorBitmap = BitmapFactory.decodeResource(getResources(), imageResId);
        }
        if (-1 != imageGoldResId){
            goldBitMap = BitmapFactory.decodeResource(getResources(), imageGoldResId);
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*
        * 预计自定义view高度 如果isExpand = true 高度是 进度条高度+进度条到顶部的距离+指示器高度+当前金额与指示器的边距+当前金额字体高度
        * 如果isExpand = false 高度是=progressBarFoldHeight
        * */
        if (isExpand) {
            progressBarHeight =  failBlockSize.getHeight() /(float)2;
            progressToTopMargin = (int) ((failBlockSize.getHeight() - progressBarHeight) / 2);
            float heightEstimate = progressBarHeight + progressToTopMargin;
            if (null != indicatorBitmap) {
                heightEstimate += indicatorBitmap.getHeight() + currentAmountMargin;
            }
            if (0 != currentAmountTextSize) {
                heightEstimate += currentAmountTextSize;
            }
            setMeasuredDimension(widthMeasureSpec, (int) heightEstimate);
        } else {
            setMeasuredDimension(widthMeasureSpec, (int) progressBarFoldHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateData();
        drawBottom(canvas);
        drawProgress(canvas, currentProgressWidth);
        if (isExpand) {
            drawCursor(canvas, currentProgressWidth);
            if (progressState == TreasureSnatchProgressState.COLLECT_FAIL) {
                drawFailBlock(canvas);
            }
        }
    }
    /** 动态计算数据 */
    private void calculateData() {
        progressRealWidth = getWidth();
        currentProgressWidth = progressRealWidth * getPerCent();
        if (currentProgressWidth < 0) {
            currentProgressWidth = 0;
        }
        if (currentProgressWidth > progressRealWidth) {
            currentProgressWidth = progressRealWidth;
        }
    }

    /** 绘制中间失败矩形 */
    private void drawFailBlock(Canvas canvas) {
        failBlockPaint.setStyle(Paint.Style.FILL);
        failBlockPaint.setColor(cursorFailedColor);
        float failLeft = (float) getWidth() /2-(float)failBlockSize.getWidth()/2;
        failBlockRectF.set(failLeft,0,failLeft+failBlockSize.getWidth(),failBlockSize.getHeight());
        canvas.drawRoundRect(failBlockRectF, failBlockRadius, failBlockRadius, failBlockPaint);
        failBlockStrokePaint.setColor(Color.WHITE);
        failBlockStrokePaint.setStyle(Paint.Style.STROKE);
        failBlockStrokePaint.setStrokeWidth(QMUIDisplayHelper.dp2px(getContext(),1)); // 设置描边宽度
        canvas.drawRoundRect(failBlockRectF, failBlockRadius, failBlockRadius, failBlockStrokePaint);
        failBlockTextPaint.setStyle(Paint.Style.FILL);
        failBlockTextPaint.setColor(Color.WHITE);
        failBlockTextPaint.setTextSize(QMUIDisplayHelper.sp2px(getContext(),12));
        float textHeight = failBlockTextPaint.descent() - failBlockTextPaint.ascent();
        float textOffset = (textHeight / 2) - failBlockTextPaint.descent();
        float centerX = failBlockRectF.centerX();
        float centerY = failBlockRectF.centerY();
        // TODO: 13/06/2024 不太确定这里是否需要多语言
        String perCentStr = "Fail";
        float textWidth = failBlockTextPaint.measureText(perCentStr);
        canvas.drawText(perCentStr, centerX-textWidth/2, centerY + textOffset, failBlockTextPaint);
    }

    /** 绘制游标*/
    private void drawCursor(Canvas canvas, double currentProgressWidth) {
//        cursorRectF.set((float) currentProgressWidth,0f+ progressToTopMargin,(float) (currentProgressWidth+cursorWidth), progressToTopMargin +cursorHeight);
//        cursorFillPaint.setColor(getCursorFillColor());
//        canvas.drawRoundRect(cursorRectF, radius, radius, cursorFillPaint);
//        // 绘制描边的圆角矩形
//        canvas.drawRoundRect(cursorRectF, radius, radius, cursorStrokePaint);
//        //绘制游标中心文字
//        float textHeight = cursonTextPaint.descent() - cursonTextPaint.ascent();
//        float textOffset = (textHeight / 2) - cursonTextPaint.descent();
//        float centerX = cursorRectF.centerX();
//        float centerY = cursorRectF.centerY();
//        String perCentStr = getPerCentStr()+"%";
//        float textWidth = cursonTextPaint.measureText(perCentStr);
//        canvas.drawText(perCentStr, centerX-textWidth/2, centerY + textOffset, cursonTextPaint);
//        drawIndicator(canvas);
//        drawCurrentAmount(canvas);

    }

    private void drawCurrentAmount(Canvas canvas) {
        currentAmountPaint.setColor(getCurrentAmountColor());
        //绘制游标中心文字
        float textHeight = currentAmountPaint.descent() - currentAmountPaint.ascent();
        float textOffset = (textHeight / 2) - currentAmountPaint.descent();
        float centerX = progressRectF.centerX();
        float progressBottom = progressRectF.bottom;
        float centerY = progressBottom+currentAmountMargin+textHeight;
        if (null!=indicatorBitmap){
            centerY+=indicatorBitmap.getHeight();
        }
        String currentStr = currentAmount+"";
        float textWidth = currentAmountPaint.measureText(currentStr);
        float textX = centerX-textWidth/2;
        float textY = centerY-textOffset;
        int goldMargin = QMUIDisplayHelper.dp2px(getContext(),2);
        if (textX-goldBitMap.getWidth()-goldMargin<0){
            //防止文字左边越界
            textX = goldBitMap.getWidth()+goldMargin;
        }
        canvas.drawText(currentStr,textX,textY,currentAmountPaint);
        canvas.drawBitmap(goldBitMap,textX-goldBitMap.getWidth()-goldMargin,getHeight()-goldBitMap.getHeight(),null);
    }

    /*绘制指示器*/
    private void drawIndicator(Canvas canvas){
        if (null!=indicatorBitmap){
            float rightX = progressRectF.right;
            float indicatorX = rightX - (float) indicatorBitmap.getWidth();
            canvas.drawBitmap(indicatorBitmap,indicatorX,progressRectF.bottom,null);
        }
    }

    /**绘制进度条*/
    private void drawProgress(Canvas canvas, double currentProgressWidth) {
        float top = progressToTopMargin;
        if (isExpand){
            progressRectF.set(0f,top, (float) currentProgressWidth, top+progressBarHeight);
        }else{
            progressRectF.set(0f,0f, (float) currentProgressWidth, progressBarFoldHeight);
        }
        progressPaint.setColor(getProgressColor());
        canvas.drawRoundRect(progressRectF,radius,radius,progressPaint);
        if (isExpand){
            float textHeight = cursonTextPaint.descent() - cursonTextPaint.ascent();
            float textOffset = (textHeight / 2) - cursonTextPaint.descent();
            float centerX = progressRectF.centerX();
            float centerY = progressRectF.centerY();
            String perCentStr = getPerCentStr()+"%";
            float textWidth = cursonTextPaint.measureText(perCentStr);
            float textX = centerX-textWidth/2;
            if (textX<0){//防止文字左边越界
                textX = 0;
            }
            canvas.drawText(perCentStr, textX, centerY + textOffset, cursonTextPaint);
            drawIndicator(canvas);
            drawCurrentAmount(canvas);
        }
    }

    /** 绘制进度条底部区域 */
    private void drawBottom(Canvas canvas) {
        float top = progressToTopMargin;
        if (isExpand){
            progressBackGroundRectF.set(0f,top,getWidth(),top+progressBarHeight);
        }else{
            progressBackGroundRectF.set(0f,0f,getWidth(),progressBarFoldHeight);
        }
        canvas.drawRoundRect(progressBackGroundRectF,radius,radius,progressBgPaint);
    }

    public void setCurrentAmount(double currentAmount){
        this.currentAmount = currentAmount;
        invalidate();
    }
    private double getPerCent(){
        return this.currentAmount / prizePool;
    }
    /** 获取百分比字符串 */
    private String getPerCentStr(){
        return BigDecimal.valueOf(getPerCent()).multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.DOWN)+"";
    }

    public void setPrizePool(double prizePool) {
        this.prizePool = prizePool;
    }

    /*更新进度状态*/
    public void updateProgressState(TreasureSnatchProgressState progressState) {
        this.progressState = progressState;
        invalidate();
    }

    private int getProgressColor() {
        switch (progressState) {
            case COLLECT_SUCCESS:
                return progressGreenColor;
            case COLLECT_EXCEED:
                return progressRedColor;
            case COLLECT_FAIL:
                return progressFailedColor;
            default:
                return progressYellowColor;
        }
    }
    private int getCursorFillColor() {
        switch (progressState) {
            case COLLECT_SUCCESS:
                return progressGreenColor;
            case COLLECT_EXCEED:
                return progressRedColor;
            case COLLECT_FAIL:
                return cursorFailedColor;
            default:
                return progressYellowColor;
        }
    }
    private int getCurrentAmountColor() {
        switch (progressState) {
            case COLLECT_SUCCESS:
                return progressGreenColor;
            case COLLECT_EXCEED:
                return progressRedColor;
            case COLLECT_FAIL:
                return currentAmountFailedColor;
            default:
                return progressYellowColor;
        }
    }
    /**展开或折叠重新计算高度以及绘制*/
    public void setExpand(boolean expand) {
        isExpand = expand;
        requestLayout();
    }

}
