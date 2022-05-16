package com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders

/**
 * 點選主題的interface
 */
interface ISelectTheme {
    /**
     * 主題選擇
     * @param aThemeMode 選擇的語言Id
     */
    fun onThemeSelected(aThemeMode : Int)
}