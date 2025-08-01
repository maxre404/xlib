package yellow5a5.clearscreenhelper.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import yellow5a5.clearscreenhelper.Constants;
import yellow5a5.clearscreenhelper.IClearEvent;
import yellow5a5.clearscreenhelper.IClearRootView;
import yellow5a5.clearscreenhelper.IPositionCallBack;


/**
 * Created by Yellow5A5 on 16/10/27.
 */

public class FrameRootView extends FrameLayout implements IClearRootView {


    private final int MIN_SCROLL_SIZE = 50;
    private final int LEFT_SIDE_X = 0;
    private  int RIGHT_SIDE_X = getResources().getDisplayMetrics().widthPixels;

    private int mDownX;
    private int mEndX;
    private final ValueAnimator mEndAnimator;

    private boolean isCanSrcoll;
    private boolean isTouchWithAnimRuning;

    private Constants.Orientation mOrientation;

    private IPositionCallBack mIPositionCallBack;
    private IClearEvent mIClearEvent;

    @Override
    public void setIPositionCallBack(IPositionCallBack l) {
        mIPositionCallBack = l;
    }

    @Override
    public void setIClearEvent(IClearEvent l) {
        mIClearEvent = l;
    }

    @Override
    public int getCurrentWidth() {
        return RIGHT_SIDE_X;
    }

    public FrameRootView(Context context) {
        this(context, null);
    }

    public FrameRootView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameRootView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mEndAnimator = ValueAnimator.ofFloat(0, 1.0f).setDuration(200);
        mEndAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float factor = (float) valueAnimator.getAnimatedValue();
                int diffX = mEndX - mDownX;
                mIPositionCallBack.onPositionChange((int) (mDownX + diffX * factor), 0);
            }
        });
        mEndAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOrientation.equals(Constants.Orientation.RIGHT) && mEndX == RIGHT_SIDE_X) {
                    mIClearEvent.onClearEnd();
                    mOrientation = Constants.Orientation.LEFT;
                } else if (mOrientation.equals(Constants.Orientation.LEFT) && mEndX == LEFT_SIDE_X) {
                    mIClearEvent.onRecovery();
                    mOrientation = Constants.Orientation.RIGHT;
                }
                mDownX = mEndX;
                isCanSrcoll = false;
            }
        });
    }

    @Override
    public void setClearSide(Constants.Orientation orientation) {
        mOrientation = orientation;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        int offsetX = x - mDownX;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                if (isGreaterThanMinSize(mDownX, x) && isCanSrcoll) {
                    mIPositionCallBack.onPositionChange(getPositionChangeX(offsetX), 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:
                if (isGreaterThanMinSize(mDownX, x) && isCanSrcoll) {
                    mDownX = getPositionChangeX(offsetX);
                    fixPostion(offsetX);
                    mEndAnimator.start();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void cancelClear(int x) {
        if (!mEndAnimator.isRunning()) {
            mDownX = getPositionChangeX(x);
            fixPostion(x);
            mEndAnimator.start();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isTouchWithAnimRuning = mEndAnimator.isRunning();
                mDownX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isGreaterThanMinSize(mDownX, x) && !isTouchWithAnimRuning) {
                    isCanSrcoll = true;
                    return true;
                }
        }
        return super.onInterceptTouchEvent(event);
    }

    private int getPositionChangeX(int offsetX) {
        int absOffsetX = Math.abs(offsetX);
        if (mOrientation.equals(Constants.Orientation.RIGHT)) {
            return absOffsetX - MIN_SCROLL_SIZE;
        } else {
            return RIGHT_SIDE_X - (absOffsetX - MIN_SCROLL_SIZE);
        }
    }

    private void fixPostion(int offsetX) {
        int absOffsetX = Math.abs(offsetX);
        if (mOrientation.equals(Constants.Orientation.RIGHT) && absOffsetX > RIGHT_SIDE_X / 3) {
            mEndX = RIGHT_SIDE_X;
        } else if (mOrientation.equals(Constants.Orientation.LEFT) && (absOffsetX > RIGHT_SIDE_X / 3)) {
            mEndX = LEFT_SIDE_X;
        }
    }

    public boolean isGreaterThanMinSize(int x1, int x2) {
        if (mOrientation.equals(Constants.Orientation.RIGHT)) {
            return x2 - x1 > MIN_SCROLL_SIZE;
        } else {
            return x1 - x2 > MIN_SCROLL_SIZE;
        }
    }
    public void onOrientationChanged(ImageView exitedView){
       int widthPixels = getResources().getDisplayMetrics().widthPixels;
       if (RIGHT_SIDE_X!=widthPixels){
           RIGHT_SIDE_X = widthPixels;
           if (mOrientation.equals(Constants.Orientation.LEFT)){
               updatePosition(widthPixels);
               exitedView.setVisibility(View.VISIBLE);
           }else if (mOrientation.equals(Constants.Orientation.RIGHT)){
               updatePosition(0);
               exitedView.setVisibility(View.GONE);
           }
       }
    }

    private void updatePosition(int widthPixels){
        mEndX = widthPixels;
        mIPositionCallBack.onPositionChange(widthPixels, 0);
    }
}
