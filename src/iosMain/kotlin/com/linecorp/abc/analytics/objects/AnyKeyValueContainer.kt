package com.linecorp.abc.analytics.objects

import com.linecorp.abc.analytics.extensions.camelToSnakeCase

open class AnyKeyValueContainer<T: Any>(override val value: T) : KeyValueContainer {
    override val key: String
        get() = this::class.simpleName?.camelToSnakeCase()?.lowercase() ?: ""
}