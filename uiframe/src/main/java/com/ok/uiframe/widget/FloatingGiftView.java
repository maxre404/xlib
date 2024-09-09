package com.ok.uiframe.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.max.uiframe.R;

public class FloatingGiftView extends ConstraintLayout {
    private static String tag = "debug11";
    private float lastX;
    private float lastY;
    public FloatingGiftView(Context context) {
        super(context);
        initView(context);
    }

    public FloatingGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FloatingGiftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        if (isInEditMode()){
            return;
        }
        LayoutInflater.from(context).inflate(R.layout.layout_floating_gift, this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewGroup parent = (ViewGroup) getParent();  // 将父容器转换为 ViewGroup
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();
        Log.d(tag, "父容器的宽:"+parentWidth + "父容器的高:"+parentHeight);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getX() - lastX;
                float deltaY = event.getY() - lastY;
                float newTranslationX = getTranslationX() + deltaX;
                float newTranslationY = getTranslationY() + deltaY;
                // 获取View的宽高
                int viewWidth = getWidth();
                int viewHeight = getHeight();
                // 边界检测
                if (newTranslationX < 0) {
                    newTranslationX = 0;
                } else if (newTranslationX + viewWidth > parentWidth) {
                    newTranslationX = parentWidth - viewWidth;
                }
                if (newTranslationY < 0) {
                    newTranslationY = 0;
                } else if (newTranslationY + viewHeight > parentHeight) {
                    newTranslationY = parentHeight - viewHeight;
                }
                // 设置新的位置
                setTranslationX(newTranslationX);
                setTranslationY(newTranslationY);
                lastX = event.getX();
                lastY = event.getY();
                break;
        }
        return true;
    }

}
