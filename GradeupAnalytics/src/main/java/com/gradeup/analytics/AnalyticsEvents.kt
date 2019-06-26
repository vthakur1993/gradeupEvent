package com.gradeup.analytics

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class AnalyticsEvents {

    companion object {

        private lateinit var database: AnalyticsEventsDatabaseHandler
        private lateinit var gson: Gson
        private var isInitialized = false
        private lateinit var cm: ConnectivityManager
        private lateinit var tm: TelephonyManager
        private lateinit var activeNetwork: NetworkInfo

        private lateinit var endpointUrl: String
        private lateinit var analyticsEventViewModel: AnalyticsEventViewModel


        private val CONNECT_TIMEOUT_MILLIS = 2000
        private val READ_TIMEOUT_MILLIS = 10000
        private lateinit var staticMap: HashMap<String, Any>
        private lateinit var context: Context
        //these properties should be set if you need to send
        //some values for each event and thereby reducing redundant code
        fun setStaticProperties(map: HashMap<String, Any>) {
            if (!::staticMap.isInitialized) {
                staticMap = HashMap()
            }
            staticMap.putAll(map)
        }

        fun init(context: Context, endpoint: String) {
            try {
                this.context = context
                database = AnalyticsEventsDatabaseHandler(context)
                gson = Gson()
                endpointUrl = endpoint
                analyticsEventViewModel = AnalyticsEventViewModel()
                isInitialized = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun sendEvent(eventName: String, map: HashMap<String, Any>) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    try {
                        if (!isInitialized) {
                            throw RuntimeException("Gradeup event not initialized")
                        }

                        if (endpointUrl.isEmpty()) {
                            throw RuntimeException("Endpoint not initialized")
                        }


                        map.put("event_name", eventName);
                        map.put("event_timestamp", System.currentTimeMillis())
                        map.put("platform", "ANDROID")
                        map.put("sdk_version", "1.0")

                        if (staticMap.size > 0)
                            map.putAll(staticMap)


                        val toJson = gson.toJson(map)
                        val gradeUpEvent = AnalyticsEventModel(event = toJson)
                        analyticsEventViewModel.addEvent(gradeUpEvent, database)

                        val gradeUpEvents = analyticsEventViewModel.getEvents(database)

                        if (gradeUpEvents.size > 0) {

                            val jsonArray = JsonArray()
                            for (event in gradeUpEvents) {
                                val obj = JsonParser().parse(event.event).getAsJsonObject()
                                jsonArray.add(obj)
                            }

                            val jsonObject = JsonObject()
                            jsonObject.add("events", jsonArray)
                            if (shouldSendEvent()) {
                                if (sendDataToServer(jsonObject.toString())) {
                                    analyticsEventViewModel.deleteGradeUpEventsBeforeId(
                                        gradeUpEvents[gradeUpEvents.size - 1].id!!,
                                        database
                                    )
                                }
                            } else {

                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }

        fun sendDataToServer(dataString: String): Boolean {
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
            return conn.responseCode == 200
        }

        private fun shouldSendEvent(): Boolean {
            try {
                if (context == null) {
                    return false
                }
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var activeNetwork: NetworkInfo? = null
                if (cm != null) {
                    activeNetwork = cm.activeNetworkInfo
                }
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting
            } catch (e: Exception) {
                return false
            }
        }

    }


}
