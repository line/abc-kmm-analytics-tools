package androidApp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.linecorp.abc.analytics.ATEventCenter
import com.linecorp.abc.analytics.Event
import com.linecorp.abc.analytics.triggers.view.click
import com.linecorp.abc.analytics.triggers.view.eventTrigger
import com.linecorp.abc.analytics.triggers.view.register
import com.linecorp.abc.analytics.triggers.view.send
import sample.androidApp.R
import java.util.*

fun <T: View> T.setOnClickListener(l: View.OnClickListener?) {
    print("setOnClickListener")
}

fun <T: View> T.registerTrigger(invoke: () -> Unit) {
    print("setOnClickListener")

}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            0)
            ATEventCenter.restartDetecting()
        }

        val button = findViewById<Button>(R.id.button)
        button.eventTrigger
            .click("hello button")
            .register { listOf(EventParam.UserName("steve")) }
        button.setOnClickListener { v ->
            v.eventTrigger.send()
        }
    }
}