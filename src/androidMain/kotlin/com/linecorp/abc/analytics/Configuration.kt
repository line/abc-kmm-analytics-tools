package com.linecorp.abc.analytics

import android.app.Activity
import android.content.Context
import com.linecorp.abc.analytics.extensions.topActivityName

actual class Configuration {
    internal actual var canTrackScreenCaptureBlock: (() -> Boolean)? = null
    internal actual val delegates: MutableList<ATEventDelegate> = mutableListOf()

    internal var canTrackScreenViewBlock: ((Activity) -> Boolean)? = null
        private set
    internal var topScreenClassBlock: () -> String? = { context.topActivityName() }
        private set

    internal lateinit var context: Context

    fun canTrackScreenView(block: (Activity) -> Boolean) {
        canTrackScreenViewBlock = block
    }

    fun topScreenClass(block: () -> String?) {
        topScreenClassBlock = block
    }
}