package com.linecorp.abc.analytics

import com.linecorp.abc.analytics.extensions.className
import com.linecorp.abc.analytics.extensions.screenName
import com.linecorp.abc.analytics.interfaces.ATEventParamProvider
import com.linecorp.abc.analytics.objects.BaseParam
import com.linecorp.abc.analytics.objects.KeyValueContainer
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationUserDidTakeScreenshotNotification
import platform.UIKit.UIViewController

actual class ATEventCenter {

    @ThreadLocal
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
        //  Private
        // -------------------------------------------------------------------------------------------

        private fun beginScreenCaptureDetecting() {
            NSNotificationCenter.defaultCenter.addObserverForName(
                UIApplicationUserDidTakeScreenshotNotification,
                null,
                null)
            {
                val canTrackScreenCapture = configuration.canTrackScreenCaptureBlock?.invoke() ?: false
                if (canTrackScreenCapture) {
                    send(Event.CAPTURE, from = null)
                }
            }
        }

        private fun beginScreenViewDetecting() {
            ATScreenViewDetector.addObserver(this) {
                val canTrackScreenView = configuration.canTrackScreenViewBlock?.invoke(it) ?: false
                if (canTrackScreenView) {
                    val provider = it as? ATEventParamProvider
                    val params = provider?.params(Event.VIEW, it) ?: listOf()
                    send(Event.VIEW, params, it)
                }
            }
        }

        private fun send(event: Event, extraParams: List<KeyValueContainer> = listOf(), from: UIViewController?) {
            val vc = from ?: configuration.topViewControllerBlock.invoke()
            vc?.let {
                val screenClass = configuration.mapScreenClassBlock?.invoke(vc) ?: vc.className()
                val screenName = it.screenName(screenClass) ?: return
                val baseParams = listOf(
                    BaseParam.ScreenClass(screenClass),
                    BaseParam.ScreenName(screenName))
                var params = baseParams + extraParams
                sendAfterMapping(event, params)
            } ?: sendAfterMapping(event, extraParams)
        }

        private fun sendAfterMapping(event: Event, params: List<KeyValueContainer>) {
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
                beginScreenViewDetecting()
            }
            if (configuration.canTrackScreenCaptureBlock != null) {
                beginScreenCaptureDetecting()
            }
        }

        private fun willConfigurationChange() {
            ATScreenViewDetector.removeObserver(this)
            NSNotificationCenter.defaultCenter.removeObserver(this)
        }
    }
}