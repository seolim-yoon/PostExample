package com.example.postexample.util

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatUtils {
    val now: Long = System.currentTimeMillis()

    val date = Date(now)

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("ko", "KR"))

    val stringTime = dateFormat.format(date)
}