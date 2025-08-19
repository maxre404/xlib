package com.ok.uiframe.data

data class BannerMessage(
    val text: String,
    val duration: Long = 3000L, // 停留 3 秒
    val priority: Int = 0       // 可用于优先级排序
)
