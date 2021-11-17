package com.linecorp.abc.analytics.objects

import com.linecorp.abc.analytics.extensions.camelToSnakeCase

interface KeyValueContainer {
    val key: String
        get() = this::class.simpleName?.camelToSnakeCase()?.lowercase() ?: ""
    val value: Any
}