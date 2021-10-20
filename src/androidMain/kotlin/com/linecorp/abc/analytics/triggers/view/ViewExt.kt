package com.linecorp.abc.analytics.triggers.view

import android.view.View
import com.linecorp.abc.analytics.triggers.ATEventTrigger
import com.linecorp.abc.analytics.triggers.PropertyStorage

val <T: View> T.eventTrigger: ATEventTrigger<T>
    get() = PropertyStorage.get(this, "eventTrigger") {
        manageStoredProperties { PropertyStorage.remove(eventTrigger) }
        ATEventTrigger(this)
    }

private fun <T: View> T.manageStoredProperties(destroyed: () -> Unit) {
    lateinit var listener: View.OnAttachStateChangeListener
    listener = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
        }
        override fun onViewDetachedFromWindow(v: View) {
            this@manageStoredProperties.removeOnAttachStateChangeListener(listener)
            PropertyStorage.remove(this@manageStoredProperties)
            destroyed.invoke()
        }
    }
    addOnAttachStateChangeListener(listener)
}