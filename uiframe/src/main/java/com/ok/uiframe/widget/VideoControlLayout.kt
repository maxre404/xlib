package com.ok.uiframe.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import com.max.uiframe.R
import kotlin.math.abs

class VideoControlLayout : ConstraintLayout {
    private lateinit var gestureDetector: GestureDetectorCompat
    val TAG = "debug11"
    private var mViewList = listOf<View>()
    private var downX = 0f
    private var downY = 0f
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
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

    fun exceptViewList(viewList: List<View>) {
        mViewList = viewList
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(context: Context) {
        if (isInEditMode) {
            return
        }
        LayoutInflater.from(context).inflate(R.layout.layout_video_control, this)
        gestureDetector =
            GestureDetectorCompat(this.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent?): Boolean {
                    return true
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val tapX = e.x
                    if (tapX < width / 2) {
                        Log.d(TAG, "双击屏幕左边")
                    } else {
                        Log.d(TAG, "双击屏幕右边")
                    }
                    return true
                }

                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    Log.d(TAG, "单击事件")
                    return true
                }
            })

    }

//    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
//        event?.let {
//            Log.d(TAG, "dispatchTouchEvent: +++++++")
//            gestureDetector.onTouchEvent(event)
//        }
//        return super.dispatchTouchEvent(event)
//    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            gestureDetector.onTouchEvent(event)
        }
        if (event?.action == MotionEvent.ACTION_DOWN) {
            mViewList.forEach { view ->
                val isInSide = isTouchInsideView(view, event)
                if (isInSide && view.visibility == View.VISIBLE) {
                    Log.d(TAG, "不拦截事件哦")
                    return false
                }
            }
        }
        return true
    }

    private fun isTouchInsideView(view: View, event: MotionEvent): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]

        return event.rawX >= x &&
                event.rawX <= x + view.width &&
                event.rawY >= y &&
                event.rawY <= y + view.height
    }
}
