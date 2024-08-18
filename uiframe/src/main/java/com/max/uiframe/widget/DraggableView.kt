package com.max.uiframe.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageView;

public class DraggableEdgeView extends AppCompatImageView {

    private float dX, dY;
    private int parentWidth, parentHeight;

    public DraggableEdgeView(Context context) {
        super(context);
        init();
    }

    public DraggableEdgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DraggableEdgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 初始化任何需要的资源
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 获取父布局的宽度和高度
        post(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) getParent();
                if (parent != null) {
                    parentWidth = parent.getWidth();
                    parentHeight = parent.getHeight();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录初始位置
                dX = getX() - event.getRawX();
                dY = getY() - event.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                // 拖动时更新位置
                float newX = event.getRawX() + dX;
                float newY = event.getRawY() + dY;

                // 限制左右边界
                if (newX < 0) {
                    newX = 0;
                } else if (newX > parentWidth - getWidth()) {
                    newX = parentWidth - getWidth();
                }

                // 限制上下边界
                if (newY < 0) {
                    newY = 0;
                } else if (newY > parentHeight - getHeight()) {
                    newY = parentHeight - getHeight();
                }
                setX(newX);
                setY(newY);
                return true;

            case MotionEvent.ACTION_UP:
                // 判断吸附到最近的边缘
                float centerX = getX() + getWidth() / 2;
                float targetX;
                if (centerX < parentWidth / 2) {
                    targetX = 0; // 吸附到左边
                } else {
                    targetX = parentWidth - getWidth(); // 吸附到右边
                }
                // 使用动画吸附到边缘
                ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "x", getX(), targetX);
                animatorX.setDuration(300);
                animatorX.start();
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }
}
