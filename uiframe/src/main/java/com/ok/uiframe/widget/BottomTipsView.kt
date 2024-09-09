package com.ok.uiframe.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import com.max.uiframe.R

class BottomTipsView : RelativeLayout {
    private var animationEnter: Animation? = null
    private var animationOut: Animation? = null
    private val handler = Handler(Looper.getMainLooper())

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

    private fun initView(context: Context) {
        if (isInEditMode) {
            return
        }
        LayoutInflater.from(context).inflate(R.layout.view_bottom_tips, this)
        animationEnter = AnimationUtils.loadAnimation(getContext(), R.anim.anim_left_in)
        animationEnter?.interpolator = LinearInterpolator()
        animationOut = AnimationUtils.loadAnimation(getContext(), R.anim.anim_right_out)
        animationOut?.interpolator = LinearInterpolator()
        animationOut?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                visibility = GONE
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        })
    }


    fun showTips() {
        visibility = VISIBLE
        startAnimation(animationEnter)
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ startAnimation(animationOut) }, 5000)
    }
}
