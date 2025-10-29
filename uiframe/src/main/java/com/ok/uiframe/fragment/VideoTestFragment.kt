package com.ok.uiframe.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.max.uiframe.R
import com.max.xlib.log.LogFile
import com.ok.uiframe.data.BannerMessage
import com.ok.uiframe.widget.BannerManager
import com.ok.uiframe.widget.GestureSupportRecyclerView
import com.ok.uiframe.widget.OnItemGestureListener
import yellow5a5.clearscreenhelper.ClearScreenHelper
import yellow5a5.clearscreenhelper.View.FrameRootView

class VideoTestFragment:Fragment() {
    var mClearScreenHelper: ClearScreenHelper? = null //屏幕控制器
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video_test, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        val param = arguments?.getString("param")
        val button = view?.findViewById<Button>(R.id.button2)
        button?.text = "button:$param"
        val rv = view?.findViewById<GestureSupportRecyclerView>(R.id.rv)
        val sampleClearRootLayout = view?.findViewById<FrameRootView>(R.id.sampleClearRootLayout)
        mClearScreenHelper = ClearScreenHelper(activity, sampleClearRootLayout)
        mClearScreenHelper?.bind(rv,button)
        rv?.linear()?.setup {
            addType<String>(R.layout.item_drag_photo_album)
            onBind {
                val itemText = findView<TextView>(R.id.item_text)
                itemText.text = getModel<CharSequence?>().toString()
                val button = findView<Button>(R.id.button)
                button.setOnClickListener {
                    LogFile.log("这里有触发点击事件")
                }
            }
        }?.models = listOf("1","2","3","4","5","6","7","8","9","10")
        view?.findViewById<View>(R.id.button2)?.setOnClickListener {
            LogFile.log("onCreate: 这里时button 的点击事件哦")
            BannerManager.show(activity!!,BannerMessage("今天签到领100金币", 3000))
        }
        rv?.setOnItemGestureListener(object :OnItemGestureListener{
            override fun onSingleClick(view: View?, position: Int) {
               LogFile.log("单击")
            }

            override fun onDoubleClick(view: View?, position: Int) {
                LogFile.log("双击")
            }

            override fun onItemLongPressLeft(view: View?, position: Int) {
                LogFile.log("长按左边")
            }

            override fun onItemLongPressRight(view: View?, position: Int) {
                LogFile.log("长按右边")
            }

            override fun onItemLongPressLeftUp(view: View?, position: Int) {
                LogFile.log("长按左边结束")
            }

            override fun onItemLongPressRightUp(view: View?, position: Int) {
                LogFile.log("长按右边结束")
            }

        })
//        layoutVideo?.exceptViewList(listOf(rv!!))

    }

    companion object{

        fun getInstance(param: String):VideoTestFragment{
            val fragment = VideoTestFragment()
            fragment.arguments = Bundle().apply {
                putString("param",param)
            }
            return fragment
        }
    }
}