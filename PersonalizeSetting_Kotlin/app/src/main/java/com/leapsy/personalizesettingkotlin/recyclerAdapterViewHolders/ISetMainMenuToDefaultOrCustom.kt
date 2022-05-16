package com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders

/**
 * 點選主菜單的interface
 */
interface ISetMainMenuToDefaultOrCustom {
    /**
     * 主菜單選擇
     * @param aIsMainMenuSetToDefault 1: 默認的, 2: 自作主張的
     */
    fun onMainMenuSet(aIsMainMenuSetToDefault : Int)
}