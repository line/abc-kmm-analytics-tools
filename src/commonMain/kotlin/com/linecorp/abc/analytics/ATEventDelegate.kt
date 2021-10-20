package com.linecorp.abc.analytics

import com.linecorp.abc.analytics.objects.KeyValueContainer

interface ATEventDelegate {
    fun mapEventKey(event: Event): String {
        return event.value
    }
    fun mapParamKey(container: KeyValueContainer): String {
        return container.key
    }
    fun send(event: String, params: Map<String, String>)
    fun setup()
    fun setUserProperties()
}

internal fun ATEventDelegate.sendAfterMapping(event: Event, params: List<KeyValueContainer>) {
    send(mapEventKey(event), params.associate { mapParamKey(it) to it.value.toString() })
}