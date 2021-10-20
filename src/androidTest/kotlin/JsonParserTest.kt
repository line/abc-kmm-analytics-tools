import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.linecorp.abc.analytics.ATEventCenter
import com.linecorp.abc.analytics.utils.JsonParser
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk=[Build.VERSION_CODES.O_MR1], manifest=Config.NONE)
class JsonParserTest {

    private val parser = JsonParser()

    @Before
    fun setup() {
        ATEventCenter.configuration.context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testParse() {
        val result = parser.parse("ATScreenNameMapper.json")
        assertNotNull { result }
        assertTrue(result is Map<String, String>)
    }
}