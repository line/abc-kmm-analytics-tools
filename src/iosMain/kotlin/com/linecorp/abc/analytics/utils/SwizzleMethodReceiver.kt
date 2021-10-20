package com.linecorp.abc.analytics.utils

import com.linecorp.abc.analytics.ATScreenViewDetector
import platform.UIKit.UIViewController

@Suppress("UNUSED", "CAST_NEVER_SUCCEEDS")
class SwizzleMethodReceiver {
    @Suppress("UNUSED_PARAMETER")
    fun viewWillAppear(animated: Boolean) {
        ATScreenViewDetector.notify(this as UIViewController)
    }
}