package com.leapsy.personalizesettingkotlin.jsonHelper

import com.leapsy.personalizesettingkotlin.jsonHelper.CustomPersonalizedSettingData

interface IGetJsonFileData {
    fun onGetJsonData(aIsShowWeather : Int?, aThemeMode : Int?, aIsDefault : Int?, aCustomDataList : MutableList<CustomPersonalizedSettingData>)
}