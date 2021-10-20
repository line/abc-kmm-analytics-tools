import com.linecorp.abc.analytics.objects.KeyValueContainer

enum class ClickSource(val value: String) {
    Hello("hello")
}

sealed class EventParam: KeyValueContainer {
    data class UserName(override val value: String): EventParam()
    data class ViewTime(override val value: Int): EventParam()
}