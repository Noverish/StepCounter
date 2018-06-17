package me.noverish.stepcounter.utils

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import kotlinx.android.synthetic.main.activity_record.*
import me.noverish.stepcounter.R.id.recycler_view

object SharedPreferenceManager {
    private val PREF_NAME = "default"
    private val RECORD_KEY = "records"

    fun putRecords(activity: Activity, lat: Double, lng: Double, step: Int) {
        val pref = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val string = pref.getString(RECORD_KEY, "")

        val latStr = "%.8f".format(lat)
        val lngStr = "%.8f".format(lng)

//        println("putRecords - string : $string")

        val newString = "$string lat : $latStr, lng : $lngStr, step : $step\n"

//        println("putRecords - newString : $newString")

        with(pref.edit()) {
            putString(RECORD_KEY, newString)
            apply()
        }
    }

    fun getRecords(activity: Activity): List<String> {



        val pref = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val tmp = pref.getString(RECORD_KEY, "")
        val tmp2 = tmp.split("\n")

        println("getRecords - tmp : $tmp")
//        println("getRecords - tmp2 : $tmp2")

        return tmp2
    }

    fun clearRecords(activity: Activity) {
        val pref = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        with(pref.edit()) {
            putString(RECORD_KEY, "")
            apply()
        }
    }
}