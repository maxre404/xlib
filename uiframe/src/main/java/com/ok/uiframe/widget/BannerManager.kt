package com.ok.uiframe.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.TextView
import com.max.uiframe.R
import com.ok.uiframe.App
import com.ok.uiframe.activity.VideoTestActivity
import com.ok.uiframe.data.BannerMessage
import java.util.LinkedList

@SuppressLint("StaticFieldLeak")
object BannerManager {
    private var wm: WindowManager? = null
    private var bannerView: View? = null
    private val queue = LinkedList<BannerMessage>()
    private var isShowing = false

    fun init() {
        wm = App.getInstance()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    fun show(activity: Activity,message: BannerMessage) {
        wm = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        queue.add(message)
        if (!isShowing) {
            showNext(activity)
        }
    }

    private fun showNext(activity: Activity) {
        if (queue.isEmpty()) return
        val msg = queue.poll()
        addBannerView(activity,msg)
    }

    private fun addBannerView(activity: Activity,message: BannerMessage) {
        if (bannerView == null) {
            bannerView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_banner, null)
        }

        // 更新文字
        bannerView?.findViewById<TextView>(R.id.bannerText)?.text = message.text

        // 设置 LayoutParams
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.FIRST_SUB_WINDOW, // 仅应用内
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP

        wm?.addView(bannerView, params)
        isShowing = true

        // 显示动画
        bannerView?.translationY = -bannerView!!.height.toFloat()
        bannerView?.animate()?.translationY(0f)?.setDuration(300)?.start()

        // 自动隐藏
        bannerView?.postDelayed({ hideBanner() }, message.duration)
    }

    private fun hideBanner() {
        bannerView?.animate()?.translationY(-bannerView!!.height.toFloat())?.setDuration(300)
            ?.withEndAction {
                wm?.removeView(bannerView)
                isShowing = false
//                showNext()
            }?.start()
    }
}
