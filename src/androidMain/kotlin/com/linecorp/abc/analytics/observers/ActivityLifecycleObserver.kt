package com.linecorp.abc.analytics.observers

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.linecorp.abc.analytics.ATEventCenter
import com.linecorp.abc.analytics.Event
import com.linecorp.abc.analytics.interfaces.ATEventParamProvider

internal object ActivityLifecycleObserver : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(p0: Activity) {}
    override fun onActivityStarted(p0: Activity) {}
    override fun onActivityDestroyed(p0: Activity) {}
    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
    override fun onActivityStopped(p0: Activity) {}
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {}

    override fun onActivityResumed(p0: Activity) {
        val canTrackScreenView = ATEventCenter.configuration.canTrackScreenViewBlock?.invoke(p0) ?: false
        if (canTrackScreenView) {
            val provider = p0 as? ATEventParamProvider
            val params = provider?.params(Event.VIEW, p0) ?: listOf()
            ATEventCenter.send(Event.VIEW, params, p0)
        }
    }
}