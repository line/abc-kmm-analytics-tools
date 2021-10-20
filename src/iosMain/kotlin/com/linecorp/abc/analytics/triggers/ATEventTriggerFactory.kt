package com.linecorp.abc.analytics.triggers

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.objc.OBJC_ASSOCIATION_RETAIN_NONATOMIC
import platform.objc.objc_getAssociatedObject
import platform.objc.objc_setAssociatedObject

open class ATEventTriggerFactory {
    @ThreadLocal
    companion object {
        private var eventTriggerKey = "eventTrigger".usePinned { it.addressOf(0) }

        fun create(owner: ATEventTriggerCompatible): ATEventTrigger<ATEventTriggerCompatible> {
            return createEventTrigger(owner)
        }

        fun create(owner: ATEventTriggerUICompatible): ATEventTrigger<ATEventTriggerUICompatible> {
            return createEventTrigger(owner)
        }

        private fun <T: ATEventTriggerCompatible> createEventTrigger(owner: T): ATEventTrigger<T> {
            @Suppress("UNCHECKED_CAST")
            var value = objc_getAssociatedObject(owner, eventTriggerKey) as? ATEventTrigger<T>
            if (value == null) {
                value = ATEventTrigger<T>(owner)
                objc_setAssociatedObject(owner, eventTriggerKey, value, OBJC_ASSOCIATION_RETAIN_NONATOMIC)
            }
            return value
        }
    }
}