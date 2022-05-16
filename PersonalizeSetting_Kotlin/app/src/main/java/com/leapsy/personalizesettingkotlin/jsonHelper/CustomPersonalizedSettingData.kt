package com.leapsy.personalizesettingkotlin.jsonHelper

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CustomPersonalizedSettingData(aAppName: String?, aPackageName: String?, aAppOrder: Int?) {

    @SerializedName("appName")
    @Expose
    private var appName: String? = null

    @SerializedName("packageName")
    @Expose
    private var packageName: String? = null

    @SerializedName("appOrder")
    @Expose
    private var appOrder: Int? = null

    init {
        appName = aAppName
        packageName = aPackageName
        appOrder = aAppOrder
    }


    fun getAppName(): String? {
        return appName
    }

    fun setAppName(aAppName: String?) {
        appName = aAppName
    }

    fun getPackageName(): String? {
        return packageName
    }

    fun setPackageName(aPackageName: String?) {
        packageName = aPackageName
    }

    fun getAppOrder(): Int? {
        return appOrder
    }

    fun setAppIcon(aAppOrder: Int?) {
        appOrder = aAppOrder
    }
}