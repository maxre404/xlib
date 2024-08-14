package com.max.uiframe.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class DraggableCustomView extends View {
    private float lastX, lastY;

    public DraggableCustomView(Context context) {
        super(context);
    }
    public DraggableCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // ... other constructors

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Log.d("debug11", "onTouchEvent: x:"+x+" y:"+y +"getLeft:"+getLeft()+" width:"+getWidth());
        ViewParent parent = getParent();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = x - lastX;
                float offsetY = y - lastY;
                // 计算新的位置
                int newLeft = getLeft() + (int) offsetX;
                int newTop = getTop() + (int) offsetY;
                int newRight = getRight() + (int) offsetX;
                int newBottom = getBottom() + (int) offsetY;
                // 获取父布局信息
                if (parent instanceof ViewGroup) {
                    ViewGroup parentView = (ViewGroup) parent;
                    int parentWidth = parentView.getWidth();
                    int parentHeight = parentView.getHeight();
                    // 处理贴边
                    if (newLeft < 0) {
                        newLeft = 0;
                        newRight = getWidth();
                    } else if (newRight > parentWidth) {
                        newLeft = parentWidth - getWidth();
                        newRight = parentWidth;
                    }
                    if (newTop < 0) {
                        newTop = 0;
                        newBottom = getHeight();
                    } else if (newBottom > parentHeight) {
                        newTop = parentHeight - getHeight();
                        newBottom = parentHeight;
                    }
                }
                // 更新 View 位置
                layout(newLeft, newTop, newRight, newBottom);
                break;
            case MotionEvent.ACTION_UP:
                // 可选：处理贴边、动画等
                // 获取父布局信息
                if (parent instanceof ViewGroup) {
                    ViewGroup parentView = (ViewGroup) parent;
                    int parentWidth = parentView.getWidth();

                    // 判断最近边缘
                    int centerX = getLeft() + getWidth() / 2;
                    int targetX;
                    if (centerX < parentWidth / 2) {
                        targetX = 0; // 吸附到左边
                    } else {
                        targetX = parentWidth - getWidth(); // 吸附到右边
                    }
                    // 启动动画
                    ObjectAnimator animator = ObjectAnimator.ofInt(this, "left", getLeft(), targetX);
                    animator.setDuration(300); // 动画时长，可调整
                    animator.start();
                    break;
                }
        }

        return true;
    }
}