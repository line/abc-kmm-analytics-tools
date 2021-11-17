package com.linecorp.abc.analytics.extensions

import com.linecorp.abc.analytics.objects.BaseParam
import com.linecorp.abc.analytics.objects.KeyValueContainer

internal fun List<KeyValueContainer>.screenClass() =
    firstOrNull { it is BaseParam.ScreenClass }

internal fun List<KeyValueContainer>.screenName() =
    firstOrNull { it is BaseParam.ScreenName }