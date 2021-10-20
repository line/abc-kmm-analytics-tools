package com.linecorp.abc.analytics

expect class Configuration {
    internal var canTrackScreenCaptureBlock: (() -> Boolean)?
    internal val delegates: MutableList<ATEventDelegate>
}

fun Configuration.canTrackScreenCapture(block: () -> Boolean) {
    canTrackScreenCaptureBlock = block
}

fun Configuration.register(delegate: ATEventDelegate) {
    delegates.add(delegate)
}