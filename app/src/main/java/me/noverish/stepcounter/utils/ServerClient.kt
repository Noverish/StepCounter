package me.noverish.stepcounter.utils

import okhttp3.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*


object ServerClient {

    private val client = OkHttpClient()
    private val URL = "http://52.78.219.223:9000"

    fun send(lat: Double, lng: Double, step: Int) {
        val JSON = MediaType.parse("application/json; charset=utf-8")

        val latStr = "%.6f".format(lat)
        val lngStr = "%.6f".format(lng)
        val content = "$latStr,$lngStr,$step"

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDateandTime = sdf.format(Date())
        val time = currentDateandTime

        val body = RequestBody.create(JSON, "{\"content\":\"$content\", \"time\":\"$time\"}")
        val request = Request.Builder()
            .url(URL)
            .post(body)
            .build()
        val response = client.newCall(request).execute()
        val string = response.body()!!.string()

        println("ServerClient:send - $string")
    }

    fun receive(): ArrayList<String> {
        val request = Request.Builder()
            .url(URL)
            .build()

        val response = client.newCall(request).execute()
        val string = response.body()!!.string()

        val jsonArray = JSONArray(string)
        val array = ArrayList<String>()

        for (i in 0..(jsonArray.length() - 1)) {
            array.add(jsonArray.getString(i))
        }

        array.sort()

        return array
    }
}