package com.linecorp.abc.analytics.utils

import platform.Foundation.NSClassFromString
import platform.UIKit.*
import platform.objc.class_getInstanceMethod
import platform.objc.method_exchangeImplementations
import platform.objc.sel_registerName

class UIViewControllerUtil {
    @ThreadLocal
    companion object {
        fun topMost(): UIViewController? {
            val windows = UIApplication.sharedApplication.windows.map { it as UIWindow }
            val rootVC = windows
                .first { it.rootViewController != null && it.isKeyWindow() }
                .rootViewController
                ?: return null
            return topMost(rootVC)
        }

        private fun topMost(viewController: UIViewController?): UIViewController? {
            val presentedVC = viewController?.presentedViewController
            if (presentedVC != null) {
                return topMost(presentedVC)
            }

            val selectedVC = (viewController as? UITabBarController)?.selectedViewController
            if (selectedVC != null) {
                return topMost(selectedVC)
            }

            val visibleVC = (viewController as? UINavigationController)?.visibleViewController
            if (visibleVC != null) {
                return topMost(visibleVC)
            }

            val pageVC = viewController as? UIPageViewController
            val childVCs = pageVC?.viewControllers?.map { it as UIViewController } ?: listOf()
            if (childVCs.count() == 1) {
                return topMost(childVCs.first())
            }

            val subviews = viewController?.view?.subviews?.map { it as UIView } ?: listOf()
            val subVCs = subviews.mapNotNull { it.nextResponder() as? UIViewController }
            if (subVCs.count() > 0) {
                return topMost(subVCs.first())
            }

            return viewController
        }
    }
}