package com.max.xlib.log

data class LogData(
    var time:Long = System.currentTimeMillis(),
    var message:String
)
