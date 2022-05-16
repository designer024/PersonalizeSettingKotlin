package com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.data

data class PersonalizedSettingItem(val aItemType : Int, val aItemTitle : String, val aItemSubtitle1 : String, val aItemSubtitle2 : String) {
    /**
     * 天氣資訊是否顯示
     */
    private var mWeatherIsShow : Int = 1
    /**
     * 天氣資訊是否顯示
     */
    fun getWeatherIsShow() : Int { return mWeatherIsShow }
    /**
     * set天氣資訊是否顯示
     */
    fun setWeatherIsShow(aItem : Int) { mWeatherIsShow = aItem }

    /**
     * 現在是什麼主題
     */
    private var mCurMode : Int = 1
    /**
     * 現在是什麼主題
     */
    fun getCurMode() : Int { return mCurMode }
    /**
     * set現在是什麼主題
     */
    fun setCurMode(aMode : Int) { mCurMode = aMode }

    /**
     * 主菜單是否為預設
     */
    private var mIsDefault : Int = 1
    /**
     * 主菜單是否為預設
     */
    fun getIsDefault(): Int { return mIsDefault }
    /**
     * set主菜單是否為預設
     */
    fun setIsDefault(aIsDefault: Int) { mIsDefault = aIsDefault }
}
