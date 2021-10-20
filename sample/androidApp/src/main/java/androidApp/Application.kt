package androidApp

import GADelegate
import com.linecorp.abc.analytics.ATEventCenter
import com.linecorp.abc.analytics.canTrackScreenCapture
import com.linecorp.abc.analytics.register

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        ATEventCenter.setConfiguration {
            canTrackScreenView { true }
            canTrackScreenCapture { true }
            register(GADelegate())
        }
    }
}