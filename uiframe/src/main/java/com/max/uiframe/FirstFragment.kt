package com.max.uiframe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import com.max.uiframe.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        firbase.setUserId("99")
        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//            throw java.lang.NullPointerException("空指针异常")
            var bundle = Bundle()
            bundle.putString("id","jkl#####")
            firbase.logEvent("click2222_event",bundle)
            FirebaseCrashlytics.getInstance().recordException(java.lang.NullPointerException("9999999999999999992 哈哈"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}