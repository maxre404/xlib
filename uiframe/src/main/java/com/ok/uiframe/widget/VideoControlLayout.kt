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
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import com.max.uiframe.R
import com.max.xlib.log.LogFile
import kotlin.math.abs

class VideoControlLayout : ConstraintLayout {
    private lateinit var gestureDetector: GestureDetectorCompat
    val TAG = "xlib"
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
                        showToast("双击屏幕左边")
                    } else {
                        Log.d(TAG, "双击屏幕右边")
                        showToast("双击屏幕右边")
                    }
                    return true
                }

                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    Log.d(TAG, "单击事件")
                    showToast("单机屏幕")
                    return true
                }

                override fun onLongPress(e: MotionEvent?) {
                    super.onLongPress(e)
                    LogFile.log("长按事件")
                    val tapX = e?.x?:0f
                    if (tapX < width / 2) {
                        Log.d(TAG, "双击屏幕左边")
                        showToast("长按屏幕左边")
                    } else {
                        Log.d(TAG, "双击屏幕右边")
                        showToast("长按屏幕右边")
                    }
                }
            })

    }
    fun showToast(message:String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            gestureDetector.onTouchEvent(event)
            mViewList.forEach { view ->
                val isInSide = isTouchInsideView(view, event)
                if (isInSide && view.visibility == View.VISIBLE) {
                    view.dispatchTouchEvent(event)
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
