package com.linecorp.abc.analytics.triggers

import com.linecorp.abc.analytics.ATEventCenter
import com.linecorp.abc.analytics.Event
import com.linecorp.abc.analytics.interfaces.ATEventParamProvider
import com.linecorp.abc.analytics.interfaces.ATEventParamProviderFunc
import com.linecorp.abc.analytics.objects.*
import kotlin.native.ref.WeakReference

// Public

fun <T: ATEventTriggerUICompatible> ATEventTrigger<T>.click(source: String): ATEventTrigger<T> {
    val obj = ATEventObject(Event.CLICK, source = source)
    setEventObject(obj)
    return this
}

fun <T: ATEventTriggerUICompatible> ATEventTrigger<T>.click(sourceTransform: EventSourceTransform): ATEventTrigger<T> {
    val obj = ATEventObject(Event.CLICK, sourceTransform = sourceTransform)
    setEventObject(obj)
    return this
}

fun <T: ATEventTriggerCompatible> ATEventTrigger<T>.register(): T {
    return register { _, _ -> listOf() }
}

fun <T: ATEventTriggerCompatible> ATEventTrigger<T>.register(provider: ATEventParamProvider): T {
    val weakProvider = WeakReference<ATEventParamProvider>(provider)
    return register { event, source ->
        weakProvider.value?.params(event, source) ?: listOf()
    }
}

fun <T: ATEventTriggerCompatible> ATEventTrigger<T>.register(provider: ATEventParamProviderFunc): T {
    return register { _, _ -> provider.invoke() }
}

// Private

private fun <T: ATEventTriggerCompatible> ATEventTrigger<T>.register(provider: EventParamProvider): T {
    eventObject?.provider = provider
    when(base) {
        is ATEventTriggerUICompatible ->
            base.registerTrigger { sendClick(base) }
        else ->
            base.registerTrigger { send(base) }
    }
    return base
}

private fun <T: ATEventTriggerCompatible> ATEventTrigger<T>.send(event: Event, params: List<KeyValueContainer>, sender: Any) {
    invokeDebug(event, params, sender)
    ATEventCenter.send(event, params)
}

private fun <T: ATEventTriggerCompatible> ATEventTrigger<T>.send(sender: Any) {
    val eventObject = eventObject ?: return
    val provider = eventObject.provider ?: return
    val params = provider.invoke(eventObject.event, sender)
    send(eventObject.event, params, sender)
}

private fun <T: ATEventTriggerCompatible> ATEventTrigger<T>.sendClick(sender: Any) {
    val eventObject = eventObject ?: return
    val clickSource = eventObject.getEventSource()?.let { BaseParam.ClickSource(it) } ?: return
    val extraParams = eventObject.provider?.invoke(eventObject.event, sender) ?: listOf()
    val params = listOf(clickSource) + extraParams
    send(eventObject.event, params, sender)
}