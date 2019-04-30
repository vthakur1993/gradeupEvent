package con.example.interview.gradeup_events_handler

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

object GradeUp {


    private var database: GradeUpEventDatabase? = null
    private var gson: Gson? = null
    private var isInitialized = false
    private var cm: ConnectivityManager? = null
    private var tm: TelephonyManager? = null
    private var activeNetwork: NetworkInfo? = null

    private val endpointUrl =
        "https://event-ingestion-serivce-dot-udofy-1021.appspot.com/pubsub/publish?token=dodo3120"
    private val BUFFER_SIZE = 256
    private val CONNECT_TIMEOUT_MILLIS = 2000
    private val READ_TIMEOUT_MILLIS = 10000


    fun init(context: Context) {
        database = GradeUpEventDatabase.getDatabase(context)
        gson = Gson()

        cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        activeNetwork = cm!!.activeNetworkInfo

        isInitialized = true
    }

    fun sendEvent(eventName: String, map: HashMap<String, Any>) {
        GlobalScope.launch {
            if (!isInitialized) {
                throw RuntimeException("Gradeup event not initialized")
            }

            try {
                map.put("event_name", eventName)
                val toJson = gson!!.toJson(map)
                val gradeUpEvent = GradeUpEvent(event = toJson)
                database!!.gradeUpEventDao().insert(gradeUpEvent)

                val gradeUpEvents = database!!.gradeUpEventDao().getGradeUpEvents()

                if (gradeUpEvents.size > 5 || shouldSendEvent()) {

                    val jsonArray = JsonArray()
                    for (event in gradeUpEvents) {
                        val obj = JsonParser().parse(event.event).getAsJsonObject()
                        jsonArray.add(obj)
                    }

                    val jsonObject = JsonObject()
                    jsonObject.add("events", jsonArray)
                    if (sendDataToServer(jsonObject.toString())) {
                        database!!.gradeUpEventDao()
                            .deleteGradeUpEventsBeforeId(gradeUpEvents.get(gradeUpEvents.size - 1).id!!)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun sendDataToServer(dataString: String): Boolean {
        val endpoint = URL(endpointUrl)
        val conn = endpoint.openConnection() as HttpURLConnection
        conn.readTimeout = READ_TIMEOUT_MILLIS
        conn.connectTimeout = CONNECT_TIMEOUT_MILLIS
        conn.doOutput = true
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf8")

        var postStream: OutputStream? = null
        try {
            postStream = conn.outputStream
            postStream!!.write(dataString.toByteArray())
        } finally {
            if (postStream != null) {
                try {
                    postStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    // ignore, in case we've already thrown
                }

            }
        }
        return conn.responseCode == 200
    }

    private fun shouldSendEvent(): Boolean {
        when (activeNetwork?.type) {
            ConnectivityManager.TYPE_WIFI -> return true
            ConnectivityManager.TYPE_MOBILE -> {
                when (tm!!.networkType) {
                    TelephonyManager.NETWORK_TYPE_LTE or TelephonyManager.NETWORK_TYPE_HSPAP ->
                        return true

                    else -> return false
                }
            }
        }
        return false
    }

}
