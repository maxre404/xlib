package com.max.uiframe.data

import com.max.uiframe.BuildConfig

data class UserLog(
    var merchantId:String = "501",
    var appVersion:String = "1.1.499",
    var userId:String = "99",
    var isDebug:Boolean = BuildConfig.DEBUG,
    var message:String = ""
)
