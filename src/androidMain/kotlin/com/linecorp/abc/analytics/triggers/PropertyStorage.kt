package com.linecorp.abc.analytics.triggers

import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object PropertyStorage {
    private var mutableMap = mutableMapOf<String, Any?>()

    fun <T> get(owner: Any, propertyName: String): T? {
        @Suppress("UNCHECKED_CAST")
        return mutableMap[key(owner, propertyName)] as? T
    }

    fun <T> get(owner: Any, propertyName: String, factory: () -> T): T {
        @Suppress("UNCHECKED_CAST")
        var value: T? = get(owner, propertyName)
        if (value == null) {
            value = factory.invoke()
            set(owner, propertyName, value)
        }
        return value!!
    }

    fun remove(owner: Any) {
        mutableMap.filter { it.key.contains(owner.hashCode().toString()) }
            .forEach { mutableMap.remove(it.key) }
    }

    fun remove(owner: Any, propertyName: String) {
        mutableMap.remove(key(owner, propertyName))
    }

    fun set(owner: Any, propertyName: String, value: Any?) {
        mutableMap[key(owner, propertyName)] = value
    }

    private fun key(owner: Any, propertyName: String): String {
        return "${owner.hashCode()}::$propertyName"
    }
}