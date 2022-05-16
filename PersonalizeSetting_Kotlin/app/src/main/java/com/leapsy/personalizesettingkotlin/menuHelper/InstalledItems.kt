package com.leapsy.personalizesettingkotlin.menuHelper

import android.graphics.drawable.Drawable

class InstalledItems {
    /**
     * App colorful 圖示
     */
    private var mItemColorfulDrawable : Drawable? = null
    /**
     * App wire 圖示
     */
    private var mItemWireDrawable : Drawable? = null
    /**
     * App名稱
     */
    private var mItemName : String? = null
    /**
     * Package Name
     */
    private var mItemPackageName : String? = null

    constructor(aColorfulDrawable : Drawable?, aItemName : String?, aItemPackageName : String?) {
        mItemColorfulDrawable = aColorfulDrawable
        mItemName = aItemName
        mItemPackageName = aItemPackageName
    }
    constructor(aColorfulDrawable : Drawable?, aWireDrawable : Drawable?, aItemName : String?, aItemPackageName : String?) {
        mItemColorfulDrawable = aColorfulDrawable
        mItemWireDrawable = aWireDrawable
        mItemName = aItemName
        mItemPackageName = aItemPackageName
    }

    /**
     * App colorful 圖示
     */
    fun getItemColorfulDrawable(): Drawable? { return mItemColorfulDrawable }
    /**
     * App wire 圖示
     */
    fun getItemWireDrawable(): Drawable? { return mItemWireDrawable }
    /**
     * App名稱
     */
    fun getItemName(): String? { return mItemName }
    /**
     * Package Name
     */
    fun getItemPackageName(): String? { return mItemPackageName }

    /**
     * 選擇順序
     */
    private var mSelectedOrder : Int = -1
    /**
     * 選擇順序
     */
    fun getSelectedOrder() : Int { return mSelectedOrder }
    /**
     * 設置選擇順序
     */
    fun setSelectedOrder(aOrder : Int) { mSelectedOrder = aOrder }
}