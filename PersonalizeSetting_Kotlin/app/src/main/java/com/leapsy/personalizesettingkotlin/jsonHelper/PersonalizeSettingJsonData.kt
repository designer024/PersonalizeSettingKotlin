package com.leapsy.personalizesettingkotlin.jsonHelper

import com.leapsy.personalizesettingkotlin.jsonHelper.CustomPersonalizedSettingData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PersonalizeSettingJsonData {
    @SerializedName("weather_info")
    @Expose
    private var weatherInfo = 1

    @SerializedName("today_weather_id")
    @Expose
    private var todayWeatherId = 0

    @SerializedName("color_mode")
    @Expose
    private var colorMode = 1

    @SerializedName("is_default")
    @Expose
    private var isDefault = 1

    @SerializedName("custom_data")
    @Expose
    private var customData: MutableList<CustomPersonalizedSettingData>? = null

    fun getWeatherInfo(): Int {
        return weatherInfo
    }

    fun setWeatherInfo(aWeatherInfo: Int) {
        weatherInfo = aWeatherInfo
    }

    fun getTodayWeatherId(): Int {
        return todayWeatherId
    }

    fun setTodayWeatherId(aTodayWeatherId: Int) {
        todayWeatherId = aTodayWeatherId
    }

    fun getColorMode(): Int {
        return colorMode
    }

    fun setColorMode(aColorMode: Int) {
        colorMode = aColorMode
    }

    fun getIsDefault(): Int {
        return isDefault
    }

    fun setIsDefault(aIsDefault: Int) {
        isDefault = aIsDefault
    }

    fun getCustomData(): MutableList<CustomPersonalizedSettingData>? {
        return customData
    }

    fun setCustomData(aCustomData: MutableList<CustomPersonalizedSettingData>?) {
        customData = aCustomData
    }
}