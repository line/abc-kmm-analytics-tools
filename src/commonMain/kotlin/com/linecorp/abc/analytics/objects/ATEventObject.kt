package com.linecorp.abc.analytics.objects

import com.linecorp.abc.analytics.Event

internal typealias EventSourceTransform = () -> String?
internal typealias EventParamProvider = (Event, Any?) -> List<KeyValueContainer>

data class ATEventObject(
    val event: Event,
    val source: String? = null,
    val sourceTransform: EventSourceTransform? = null)
{
    var provider: EventParamProvider? = null

    fun getEventSource(): String? {
        return source ?: sourceTransform?.invoke()
    }
}