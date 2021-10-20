package com.linecorp.abc.analytics.objects

sealed class BaseParam: KeyValueContainer {
    data class ClickSource(override val value: String): BaseParam()
    data class ScreenClass(override val value: String): BaseParam()
    data class ScreenName(override val value: String): BaseParam()
}