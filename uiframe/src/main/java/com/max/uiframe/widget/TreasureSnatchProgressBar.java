package com.max.uiframe.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.max.uiframe.R;
import com.max.uiframe.util.QMUIDisplayHelper;

public class TreasureSnatchProgressBar extends View {
    private float progressBarHeight =0f;
    private float cursorHeight;
    private float cursorWidth;
    private float radius = 0f;

    private boolean isExpand = true;//是否是展开时的布局 如果为false就只有一个进度条
    private Paint progressBgPaint = new Paint();//绘制进度条的底部
    private double progress = 0;
    private RectF progressBackGroundRectF;

    public TreasureSnatchProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TreasureSnatchProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        initPaint(context);
        initAttr(context, attrs);
        progressBackGroundRectF = new RectF(0f,0f,getWidth(),cursorHeight);
    }

    private void initPaint(Context context) {
        progressBgPaint.setColor(Color.parseColor("#80000000"));
        progressBgPaint.setAntiAlias(false);
        progressBgPaint.setStyle(Paint.Style.FILL);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TreasureSnatchProgressBar);
        cursorHeight = typedArray.getDimension(R.styleable.TreasureSnatchProgressBar_cursorHeight,0);
        cursorWidth = typedArray.getDimension(R.styleable.TreasureSnatchProgressBar_cursorWidth, 50);
        progressBarHeight = cursorHeight*0.75f;
        radius = QMUIDisplayHelper.dp2px(context,100);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getWidth(),(int) cursorHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(progressBackGroundRectF,radius,radius,progressBgPaint);

    }

    public void setProgress(double progress){
        this.progress = progress;
    }
}
