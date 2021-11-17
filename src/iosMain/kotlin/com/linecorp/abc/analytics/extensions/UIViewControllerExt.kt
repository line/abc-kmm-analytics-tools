package com.linecorp.abc.analytics.extensions

import com.linecorp.abc.analytics.ATScreenNameMapper
import com.linecorp.abc.analytics.interfaces.ATDynamicScreenNameMappable
import platform.Foundation.classForCoder
import platform.UIKit.UIViewController

fun UIViewController.className() =
    classForCoder.toString().split(".").last()

fun UIViewController.screenName(screenClass: String) =
    (this as? ATDynamicScreenNameMappable)?.mapScreenName()
        ?: ATScreenNameMapper.getScreenName(screenClass)