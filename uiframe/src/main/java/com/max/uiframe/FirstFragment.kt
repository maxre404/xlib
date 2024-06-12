package com.max.uiframe

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.drake.spannable.span.CenterImageSpan
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.max.uiframe.data.UserLog
import com.max.uiframe.databinding.FragmentFirstBinding
import java.util.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val db = Firebase.firestore
    var TAG = "test"
    var merchantId = "503"
    var exception = "空指针异常"
    var userId = "9999"
    var str = "不良人1 telah dibatalkan  Manajemen perumahan Manajemen perumahan"
    var moderator = "Manajemen perumahan"
//    var moderator = "Housing management"
    private val whitColor = ForegroundColorSpan(Color.WHITE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        _binding?.textView?.post {
            _binding?.textView?.setText("this is TextView   GoodLucky  HAPPY BANK")
            _binding?.textView?.setAdaptiveText("this is TextView   GoodLucky  HAPPY BANK")
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firbase = FirebaseAnalytics.getInstance(activity!!)
        initSpan()
        firbase.setUserId("99")
        binding.buttonFirst.setOnClickListener {
            startActivity(Intent(activity, NextActivity::class.java))
        }
    }

    private fun initSpan() {
        var ss = SpannableStringBuilder("$str")
        val userRoleBackgroundSpan = CenterImageSpan(activity!!, R.drawable.btn_live_admin)
                .setDrawableSize(-1)
                .setPaddingHorizontal(14.dp)
                .setPaddingVertical(2.dp)
                .setTextSize(12.dp)
                .setTextVisibility()
        ss.setSpan(
            whitColor,
            ss.length - moderator.length,
            ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            userRoleBackgroundSpan,
            ss.length - moderator.length,
            ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textviewFirst?.text = ss
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    val Number.dp
        get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()
}