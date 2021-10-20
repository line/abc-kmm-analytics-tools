package com.linecorp.abc.analytics

import com.linecorp.abc.analytics.utils.UIViewControllerUtil
import platform.UIKit.UIViewController

actual class Configuration {
    internal actual var canTrackScreenCaptureBlock: (() -> Boolean)? = null
    internal actual val delegates: MutableList<ATEventDelegate> = mutableListOf()

    internal var canTrackScreenViewBlock: ((UIViewController) -> Boolean)? = null
        private set
    internal var mapScreenClassBlock: ((UIViewController) -> String)? = null
        private set
    internal var topViewControllerBlock: () -> UIViewController? = { UIViewControllerUtil.topMost() }
        private set

    fun canTrackScreenView(block: (UIViewController) -> Boolean) {
        canTrackScreenViewBlock = block
    }

    fun mapScreenClass(block: (UIViewController) -> String) {
        mapScreenClassBlock = block
    }

    fun topViewController(block: () -> UIViewController?) {
        topViewControllerBlock = block
    }
}