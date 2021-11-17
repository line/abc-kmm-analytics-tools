package com.linecorp.abc.analytics

import com.linecorp.abc.analytics.objects.KeyValueContainer
import kotlin.native.concurrent.ThreadLocal

enum class Event(val value: String) {
    CLICK("click"),
    EXEC("exec"),
    VIEW("view"),
    CAPTURE("capture");
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect class ATEventCenter {
    @ThreadLocal
    companion object {
        var configuration: Configuration

        fun restartDetecting()
        fun send(event: Event, params: List<KeyValueContainer>)
        fun setConfiguration(block: Configuration.() -> Unit)
        fun setUserProperties()
    }
}