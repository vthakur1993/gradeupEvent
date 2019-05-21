package con.example.interview.simple_events_handler

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.util.Log
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

class SimpleEvents {

    companion object {

        private lateinit var database: SimpleEventsDatabaseHandler
        private lateinit var gson: Gson
        private var isInitialized = false
        private lateinit var cm: ConnectivityManager
        private lateinit var tm: TelephonyManager
        private lateinit var activeNetwork: NetworkInfo

        private lateinit var endpointUrl: String
        private lateinit var simpleEventViewModel: SimpleEventViewModel


        private val CONNECT_TIMEOUT_MILLIS = 2000
        private val READ_TIMEOUT_MILLIS = 10000
        private lateinit var staticMap: HashMap<String, Any>

        //these properties should be set if you need to send
        //some values for each event and thereby reducing redundant code
        fun setStaticProperties(map: HashMap<String, Any>) {
            if (!::staticMap.isInitialized) {
                staticMap = HashMap()
            }
            staticMap.putAll(map)
        }

        fun init(context: Context, endpoint: String) {
            database = SimpleEventsDatabaseHandler(context)
            gson = Gson()
            endpointUrl = endpoint
            simpleEventViewModel = SimpleEventViewModel()

            cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            activeNetwork = cm.activeNetworkInfo

            isInitialized = true
        }


        fun sendEvent(eventName: String, map: HashMap<String, Any>) {
            GlobalScope.launch {

                if (!isInitialized) {
                    throw RuntimeException("Gradeup event not initialized")
                }

                if (endpointUrl.isEmpty()) {
                    throw RuntimeException("Endpoint not initialized")
                }

                try {
                    map.put("event_name", eventName)

                    if (staticMap.size > 0)
                        map.putAll(staticMap)

                    val toJson = gson.toJson(map)
                    val gradeUpEvent = SimpleEventModel(event = toJson)
                    simpleEventViewModel.addEvent(gradeUpEvent, database)

                    val gradeUpEvents = simpleEventViewModel.getEvents(database)

                    if (gradeUpEvents.size > 5 || shouldSendEvent()) {

                        val jsonArray = JsonArray()
                        for (event in gradeUpEvents) {
                            val obj = JsonParser().parse(event.event).getAsJsonObject()
                            jsonArray.add(obj)
                        }

                        val jsonObject = JsonObject()
                        jsonObject.add("events", jsonArray)
                        if (sendDataToServer(jsonObject.toString())) {
                            simpleEventViewModel.deleteGradeUpEventsBeforeId(
                                gradeUpEvents[gradeUpEvents.size - 1].id!!,
                                database
                            )
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
                    }

                }
            }
            Log.e("response code is ", " :: " + conn.responseCode)
            return conn.responseCode == 200
        }

        private fun shouldSendEvent(): Boolean {
            when (activeNetwork.type) {
                ConnectivityManager.TYPE_WIFI -> return true
                ConnectivityManager.TYPE_MOBILE -> {
                    when (tm.networkType) {
                        TelephonyManager.NETWORK_TYPE_LTE or TelephonyManager.NETWORK_TYPE_HSPAP ->
                            return true

                        else -> return false
                    }
                }
            }
            return false
        }


    }


}
