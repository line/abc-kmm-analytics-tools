package com.linecorp.abc.analytics.extensions

import com.linecorp.abc.analytics.ATScreenNameMapper
import com.linecorp.abc.analytics.interfaces.ATDynamicPageMappable
import platform.Foundation.classForCoder
import platform.UIKit.UIViewController

fun UIViewController.className(): String {
    return classForCoder.toString().split(".").last()
}

fun UIViewController.pageName(): String {
    return (this as? ATDynamicPageMappable)?.pageName
        ?: ATScreenNameMapper.getScreenName(className())
        ?: ""
}