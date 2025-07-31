package com.ok.uiframe.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.max.uiframe.R
import com.max.xlib.log.LogFile
import com.ok.uiframe.widget.VideoControlLayout

class VideoTestFragment:Fragment() {

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
        val rv = view?.findViewById<RecyclerView>(R.id.rv)
        rv?.linear()?.setup {
            addType<String>(R.layout.item_view)
            onBind {
                val itemText = findView<TextView>(R.id.item_text)
                itemText.text = getModel<CharSequence?>().toString()
            }
        }?.models = listOf("1","2","3","4","5","6","7","8","9","10")
        view?.findViewById<View>(R.id.button2)?.setOnClickListener {
            LogFile.log("onCreate: 这里时button 的点击事件哦")
        }
        val layoutVideo = view?.findViewById<VideoControlLayout>(R.id.layoutVideo)
        layoutVideo?.exceptViewList(listOf(rv!!))

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