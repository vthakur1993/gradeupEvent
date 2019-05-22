package con.example.interview.kotlintesting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.stetho.Stetho
import com.gradeup.analytics.AnalyticsEvents
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Stetho.initializeWithDefaults(this)
        AnalyticsEvents.init(this, "https://event-ingestion-serivce-dot-udofy-1021.appspot.com/pubsub/publish?token=dodo3120")
        val map = HashMap<String, Any>()
        map.put("user_properties",AppAnalytics.getInstance().userProperty)
        map.put("device",AppAnalytics.getInstance().deviceInfo)
        AnalyticsEvents.setStaticProperties(map)

        button.setOnClickListener {
            val map = HashMap<String, Any>()
            map.put("post_id", "1")
            map.put("parameter 2", "2")
            map.put("parameter 3", "2")
            map.put("parameter 4", "2")
            map.put("parameter 5", "2")

            val eventmap= HashMap<String,Any>()
            eventmap.put("event_param",map);
            AnalyticsEvents.sendEvent("TapPost", eventmap)
        }


    }

}
