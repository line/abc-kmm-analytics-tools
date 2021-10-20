package com.linecorp.abc.analytics.triggers

interface ATEventTriggerCompatible {
    fun registerTrigger(invoke: () -> Unit)
}

interface ATEventTriggerUICompatible: ATEventTriggerCompatible {
}