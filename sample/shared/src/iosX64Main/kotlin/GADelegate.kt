
//import cocoapods.FirebaseAnalytics.FIRAnalytics
import com.linecorp.abc.analytics.ATEventDelegate
import platform.Foundation.NSLog

class GADelegate: ATEventDelegate {
    override fun setup() {
//        FIRAnalytics.initialize()
    }

    override fun setUserProperties() {
//        FIRAnalytics.setUserID("userId")
    }

    override fun send(event: String, params: Map<String, String>) {
        val casted = params as Map<Any?, *>
        NSLog("GADelegate:this:send -> event: $event, params: $casted")
//        FIRAnalytics.logEventWithName(event, casted)
    }
}