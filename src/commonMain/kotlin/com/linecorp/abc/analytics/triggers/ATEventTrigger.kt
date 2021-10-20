package com.linecorp.abc.analytics.triggers

import com.linecorp.abc.analytics.Event
import com.linecorp.abc.analytics.objects.ATEventObject
import com.linecorp.abc.analytics.objects.KeyValueContainer

internal typealias DebugFunc = (Event, List<KeyValueContainer>, Any?) -> Unit

class ATEventTrigger<Base>(val base: Base) {
    internal var isRegistered: Boolean = false
        get() = eventObject?.provider != null

    internal var eventObject: ATEventObject? = null
        private set

    private var debugFunc: DebugFunc? = null

    fun debug(func: DebugFunc): ATEventTrigger<Base> {
        debugFunc = func
        return this
    }

    fun setEventObject(eventObject: ATEventObject) {
        this.eventObject = eventObject
    }

    internal fun invokeDebug(event: Event, params: List<KeyValueContainer>, source: Any?) {
        debugFunc?.invoke(event, params, source)
    }
}