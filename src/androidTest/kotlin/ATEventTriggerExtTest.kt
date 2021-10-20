
import android.os.Build
import android.view.View
import androidx.test.core.app.ApplicationProvider
import com.linecorp.abc.analytics.ATEventCenter
import com.linecorp.abc.analytics.Event
import com.linecorp.abc.analytics.extensions.contains
import com.linecorp.abc.analytics.objects.BaseParam
import com.linecorp.abc.analytics.objects.KeyValueContainer
import com.linecorp.abc.analytics.triggers.view.click
import com.linecorp.abc.analytics.triggers.view.eventTrigger
import com.linecorp.abc.analytics.triggers.view.register
import com.linecorp.abc.analytics.triggers.view.send
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

sealed class Param: KeyValueContainer {
    data class UserName(override val value: String): Param()
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk=[Build.VERSION_CODES.O_MR1], manifest=Config.NONE)
class ATEventTriggerExtTest {

    @Before
    fun setup() {
        ATEventCenter.configuration.context = ApplicationProvider.getApplicationContext()
    }

    @After
    fun tearDown() {
        print("tearDown")
    }

    @Test
    fun testGetEventTrigger() {
        val view = mock(View::class.java)
        assertNotNull(view.eventTrigger)
        assertEquals(view.eventTrigger, view.eventTrigger)
    }

    @Test
    fun testRegister() {
        val view = mock(View::class.java)
        var event: Event? = null
        var params: List<KeyValueContainer>? = null

        view.eventTrigger
            .click("view")
            .debug { e, p, _ ->
                event = e; params = p;
            }.register {
                listOf(Param.UserName("steve"))
            }

        view.eventTrigger.send()

        assertEquals("click", event?.value)
        assertTrue { (params?.count() ?: 0) > 0 }
        assertTrue { params?.contains { it == Param.UserName("steve") } == true }
        assertTrue { params?.contains { it == BaseParam.ClickSource("view") } == true }
    }
}