package com.ok.uiframe.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class AdsorbentViews extends androidx.appcompat.widget.AppCompatImageView {
 
 private int maxWidth;
  private int maxHeight;
  private int viewWidth;
  private int viewHeight;
  private float downx;
  private float downy;
  private Context mContext;
  public AdsorbentViews(Context context) {
   this(context, null);
  }
 
  public AdsorbentViews(Context context, AttributeSet attrs) {
   this(context, attrs, 0);
  }
 
  public AdsorbentViews(Context context, AttributeSet attrs, int defStyleAttr) {
   super(context, attrs, defStyleAttr);
   mContext = context;
  }
  @Override
  protected void onDraw(Canvas canvas) {
   super.onDraw(canvas);
   DisplayMetrics outMetrics = new DisplayMetrics();
   WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
   windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
   //屏幕的宽度
   maxWidth = outMetrics.widthPixels;
   //屏幕的高度
   maxHeight = outMetrics.heightPixels;
   /**
   * 控件的宽高
   */
   viewWidth = canvas.getWidth();
   viewHeight = canvas.getHeight();
  }
  @Override
  public boolean onTouchEvent(MotionEvent event) {
   switch (event.getAction()) {
    case MotionEvent.ACTION_DOWN:
     clearAnimation();
     downx = event.getX();
     downy = event.getY();
     return true;
    case MotionEvent.ACTION_MOVE:
     float moveX = event.getRawX() - downx;
     float moveY = event.getRawY() - downy;
     moveX = moveX < 0 ? 0 : (moveX + viewWidth > maxWidth) ? (maxWidth - viewWidth) : moveX;
     moveY = moveY < 0 ? 0 : (moveY + viewHeight) > maxHeight ? (maxHeight - viewHeight) : moveY;
     this.setY(moveY);
     this.setX(moveX);
     return true;
    case MotionEvent.ACTION_UP:
     //做吸附效果
     float centerX = getX() + viewWidth / 2;
     if (centerX > maxWidth/2){
      //靠右吸附
      animate().setInterpolator(new DecelerateInterpolator())
        .setDuration(500)
        .x(maxWidth-viewWidth)
        .y(maxHeight-viewHeight)
        .start();
     }else {
      animate().setInterpolator(new DecelerateInterpolator())
        .setDuration(500)
        .x(0)
        .y(maxHeight-viewHeight)
        .start();
     }
     return true;
    default:
     return super.onTouchEvent(event);
   }
  }
}