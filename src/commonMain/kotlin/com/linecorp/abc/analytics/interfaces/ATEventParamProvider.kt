package com.linecorp.abc.analytics.interfaces

import com.linecorp.abc.analytics.Event
import com.linecorp.abc.analytics.objects.KeyValueContainer

typealias ATEventParamProviderFunc = () -> List<KeyValueContainer>

interface ATEventParamProvider {
    fun params(event: Event, source: Any?): List<KeyValueContainer>
}