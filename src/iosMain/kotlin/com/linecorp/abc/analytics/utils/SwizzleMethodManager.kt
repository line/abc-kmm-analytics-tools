package com.linecorp.abc.analytics.utils

import platform.Foundation.NSClassFromString
import platform.objc.*

@ThreadLocal
object SwizzleMethodManager {
    private var isViewControllerSwizzled = false

    internal fun swizzleUIViewController() {
        if (!isViewControllerSwizzled) {
            val from = class_getInstanceMethod(
                NSClassFromString("UIViewController"),
                sel_registerName("viewWillAppear:"))
            val to = class_getInstanceMethod(
                NSClassFromString("SharedSwizzleMethodReceiver"),
                sel_registerName("viewWillAppearAnimated:"))
            method_exchangeImplementations(from, to)
            isViewControllerSwizzled = true
        }
    }
}