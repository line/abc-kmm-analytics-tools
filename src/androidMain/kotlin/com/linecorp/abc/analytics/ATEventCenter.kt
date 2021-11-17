package com.linecorp.abc.analytics

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.linecorp.abc.analytics.interfaces.ATDynamicScreenNameMappable
import com.linecorp.abc.analytics.objects.BaseParam
import com.linecorp.abc.analytics.objects.KeyValueContainer
import com.linecorp.abc.analytics.observers.ActivityLifecycleObserver
import com.linecorp.abc.analytics.observers.ScreenCaptureObserver

actual class ATEventCenter {

    actual companion object {

        // -------------------------------------------------------------------------------------------
        //  Actual
        // -------------------------------------------------------------------------------------------

        actual var configuration = Configuration()
            private set(value) {
                willConfigurationChange()
                field = value
                didConfigurationChange(value)
            }

        actual fun restartDetecting() {
            willConfigurationChange()
            didConfigurationChange(configuration)
        }

        actual fun send(event: Event, params: List<KeyValueContainer>) {
            send(event, params, null)
        }

        actual fun setConfiguration(block: Configuration.() -> Unit) {
            configuration = configuration.apply(block)
        }

        actual fun setUserProperties() {
            configuration.delegates.forEach { it.setUserProperties() }
        }

        // -------------------------------------------------------------------------------------------
        //  Public
        // -------------------------------------------------------------------------------------------

        fun send(event: Event, screenClass: String, extraParams: List<KeyValueContainer> = listOf()) {
            send(event, screenClass, null, extraParams)
        }

        fun send(event: Event, extraParams: List<KeyValueContainer> = listOf(), from: Any?) {
            val screenClass = from?.javaClass?.simpleName ?: configuration.topScreenNameBlock.invoke() ?: return
            val screenName = (from as? ATDynamicScreenNameMappable)?.mapScreenName()
            send(event, screenClass, screenName, extraParams)
        }

        // -------------------------------------------------------------------------------------------
        //  Private
        // -------------------------------------------------------------------------------------------

        private val screenCaptureObserver: ScreenCaptureObserver by lazy {
            ScreenCaptureObserver(Handler(Looper.getMainLooper()))
        }

        private fun beginScreenViewDetecting(context: Context) {
            val application = context.applicationContext as? Application ?: return
            application.registerActivityLifecycleCallbacks(ActivityLifecycleObserver)
        }

        private fun beginScreenCaptureDetecting(context: Context) {
            val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permission != PackageManager.PERMISSION_GRANTED) return
            context.contentResolver.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                screenCaptureObserver)
        }

        private fun send(
            event: Event,
            screenClass: String,
            screenName: String?,
            extraParams: List<KeyValueContainer> = listOf()
        ) {
            val screenName = screenName ?: ATScreenNameMapper.getScreenName(screenClass) ?: return
            val baseParams = listOf(
                BaseParam.ScreenClass(screenClass),
                BaseParam.ScreenName(screenName))
            val params = baseParams + extraParams
            configuration.delegates.forEach {
                it.sendAfterMapping(event, params)
            }
        }

        // -------------------------------------------------------------------------------------------
        //  Private (Property Changed)
        // -------------------------------------------------------------------------------------------

        private fun didConfigurationChange(configuration: Configuration) {
            configuration.delegates.forEach { it.setup() }

            if (configuration.canTrackScreenViewBlock != null) {
                beginScreenViewDetecting(configuration.context)
            }
            if (configuration.canTrackScreenCaptureBlock != null) {
                beginScreenCaptureDetecting(configuration.context)
            }
        }

        private fun willConfigurationChange() {
            configuration.context.contentResolver.unregisterContentObserver(screenCaptureObserver)
            val application = configuration.context.applicationContext as? Application ?: return
            application.unregisterActivityLifecycleCallbacks(ActivityLifecycleObserver)
        }
    }
}