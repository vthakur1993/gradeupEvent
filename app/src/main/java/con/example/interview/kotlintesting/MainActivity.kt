package con.example.interview.kotlintesting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.stetho.Stetho
import con.example.interview.simple_events_handler.SimpleEvents
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Stetho.initializeWithDefaults(this)
        SimpleEvents.init(this, "https://event-ingestion-serivce-dot-udofy-1021.appspot.com/pubsub/publish?token=dodo3120")
        val map = HashMap<String, Any>()
        map.put("parameter permanent ", 1)
        map.put("parameter permanent", "2")
        SimpleEvents.setStaticProperties(map)

        button.setOnClickListener {
            val map = HashMap<String, Any>()
            map.put("parameter 1", 1)
            map.put("parameter 2", "2")
            SimpleEvents.sendEvent("Testing", map)
        }

    }
}
