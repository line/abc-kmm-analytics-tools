package com.linecorp.abc.analytics

import com.linecorp.abc.analytics.extensions.contains
import com.linecorp.abc.analytics.utils.SwizzleMethodManager
import platform.UIKit.UIViewController

private typealias Function = (UIViewController) -> Unit

class ATScreenViewDetector {
    @ThreadLocal
    companion object {
        private var observers: MutableList<ObserverInfo> = mutableListOf()

        fun addObserver(observer: Any, function: Function) {
            if (observers.contains { it.observer === observer }) {
                return
            }
            observers.add(ObserverInfo(observer, function))
            SwizzleMethodManager.swizzleUIViewController()
        }

        fun removeObserver(observer: Any) {
            observers.removeAll { it.observer === observer }
        }

        fun notify(source: UIViewController) {
            observers.forEach { it.function(source) }
        }
    }
}

private data class ObserverInfo(
    val observer: Any,
    val function: Function)