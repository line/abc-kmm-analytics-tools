
import android.util.Log
import com.linecorp.abc.analytics.ATEventDelegate

class GADelegate: ATEventDelegate {

    private val tag = GADelegate::class.java.toString()

    override fun setup() {
    }

    override fun setUserProperties() {
    }

    override fun send(event: String, params: Map<String, String>) {
        Log.d(tag, "send -> event: $event, params: $params")
    }
}