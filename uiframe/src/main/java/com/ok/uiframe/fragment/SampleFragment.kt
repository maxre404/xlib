package com.ok.uiframe.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.max.uiframe.R
import com.ok.uiframe.data.HoverHeaderModel

class SampleFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sample, container, false)
        val data: MutableList<Any> = ArrayList()
        data.add(1)
        data.add(HoverHeaderModel())
        for (i in 1..100) {
            data.add("Item $i")
        }
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.linear()?.setup {
            addType<String>(R.layout.item_drag_photo_album)
            addType<HoverHeaderModel>(R.layout.item_xuanfu)
            addType<Int>(R.layout.item_qushi)
            onBind {
                when(itemViewType){
                    R.layout.item_drag_photo_album ->{
//                        val binding = getBinding<ItemViewBinding>()
//                        binding.itemText.text = "this is item:${modelPosition-2}"
                    }
                }
            }

        }?.models = data
//        layoutManager = LinearLayoutManager(context)
//        recyclerView?.setLayoutManager(layoutManager)
//        adapter = SampleAdapter(data)
//        recyclerView?.setAdapter(adapter)

        return view
    }
}
