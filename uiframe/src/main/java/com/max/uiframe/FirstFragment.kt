package com.max.uiframe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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
//            var bundle = Bundle()
//            bundle.putString("id","j234567kl#####")
//            bundle.putString("name","jok:${Random().nextInt(10000)}+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" +
//                    "exception++++++++++++++++++++++++++++" +
//                    "+++++++++++++++++++++jkljkljkljkljkljlk12323")
//            bundle.putString("ttt","tg:${Random().nextInt(1000)}")
//            firbase.logEvent("click2222_event",bundle)
            var userLog = UserLog(merchantId="505",message = "++++++++++++++++++++++\nsadjfhjkdhsfkjhdsakjfhjkadsf\nfjaskdjflksdjfkl\n++++++++++++++++++++++123321")
            db.collection(UserLog::class.java.simpleName)
                .add(userLog)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}  tableName:${UserLog::class.java.simpleName}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}