package com.linecorp.abc.analytics.utils

import android.content.Context
import androidx.startup.Initializer
import com.linecorp.abc.analytics.ATEventCenter

@Suppress("UNUSED")
internal class ModuleInitializer: Initializer<Int> {
    override fun create(context: Context): Int {
        ATEventCenter.configuration.context = context
        return 0
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}