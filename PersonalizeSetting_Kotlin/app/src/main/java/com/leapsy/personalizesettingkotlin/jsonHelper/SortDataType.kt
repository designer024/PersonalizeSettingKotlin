package com.leapsy.personalizesettingkotlin.jsonHelper

import com.google.gson.Gson
import org.json.JSONObject
import java.lang.Exception

class SortDataType {
    companion object {
        fun <T> returnType(aJsonString: String?, aClassOfT: Class<T>?): T? {
            var jsonObject: JSONObject? = null
            return try {
                jsonObject = JSONObject(aJsonString)
                val gson = Gson()
                gson.fromJson(jsonObject.toString(), aClassOfT)
            } catch (e: Exception) {
                android.util.Log.d("@@@@@@@@@@", e.message!!)
                null
            }
        }
    }
}