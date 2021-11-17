package com.linecorp.abc.analytics.extensions

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
fun Context.topActivityName(): String? {
    val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    if (am.appTasks.count() > 0) {
        val className = am.appTasks.first().taskInfo.topActivity?.className
        return className?.split(".")?.last()
    }
    return null
}