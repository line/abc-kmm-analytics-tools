package com.linecorp.abc.analytics.triggers.view

import com.linecorp.abc.analytics.ATEventCenter
import com.linecorp.abc.analytics.Event
import com.linecorp.abc.analytics.interfaces.ATEventParamProvider
import com.linecorp.abc.analytics.interfaces.ATEventParamProviderFunc
import com.linecorp.abc.analytics.objects.*
import com.linecorp.abc.analytics.triggers.ATEventTrigger

// Public

fun <T> ATEventTrigger<T>.click(source: String): ATEventTrigger<T> {
    val obj = ATEventObject(Event.CLICK, source = source)
    setEventObject(obj)
    return this
}

fun <T> ATEventTrigger<T>.click(sourceTransform: EventSourceTransform): ATEventTrigger<T> {
    val obj = ATEventObject(Event.CLICK, sourceTransform = sourceTransform)
    setEventObject(obj)
    return this
}

fun <T> ATEventTrigger<T>.register(): T {
    return register { _, _ -> listOf() }
}

fun <T> ATEventTrigger<T>.register(provider: ATEventParamProvider): T {
    return register { event, source -> provider.params(event, source) }
}

fun <T> ATEventTrigger<T>.register(provider: ATEventParamProviderFunc): T {
    return register { _, _ -> provider.invoke() }
}

fun <T> ATEventTrigger<T>.send() {
    val eventObject = eventObject ?: return
    val provider = eventObject.provider ?: return
    val clickSource = eventObject.getEventSource()?.let {
        BaseParam.ClickSource(it)
    } ?: return
    val params = listOf(clickSource) + provider.invoke(eventObject.event, base)
    invokeDebug(eventObject.event, params, base)
    ATEventCenter.send(eventObject.event, params)
}

// Private

private fun <T> ATEventTrigger<T>.register(provider: EventParamProvider): T {
    eventObject?.provider = provider
    return base
}