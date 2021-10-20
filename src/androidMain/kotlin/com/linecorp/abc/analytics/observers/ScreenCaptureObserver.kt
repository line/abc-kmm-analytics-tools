package com.linecorp.abc.analytics.observers

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import com.linecorp.abc.analytics.ATEventCenter
import com.linecorp.abc.analytics.Event

internal class ScreenCaptureObserver(private val handler: Handler): ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        when(uri) {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI -> captureDidDetect()
        }
    }

    private fun captureDidDetect() {
        val canTrackScreenCapture = ATEventCenter.configuration.canTrackScreenCaptureBlock?.invoke() ?: false
        if (canTrackScreenCapture) {
            ATEventCenter.send(Event.CAPTURE, from = null)
        }
    }
}