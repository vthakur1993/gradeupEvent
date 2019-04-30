package con.example.interview.kotlintesting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import con.example.interview.gradeup_events_handler.GradeUp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GradeUp.init(this)

        button.setOnClickListener {
            val map = HashMap<String, Any>()
            map.put("parameter 1", 1)
            map.put("parameter 2", "2")
            GradeUp.sendEvent("Testing", map)
        }

    }
}
