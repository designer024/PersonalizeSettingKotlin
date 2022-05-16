package com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.data

import android.content.Context
import com.leapsy.personalizesettingkotlin.R
import java.util.ArrayList

class GetAllPersonalizeSettingItems(aContext : Context) {
    private var mContext : Context = aContext

    private var mItemsList : ArrayList<PersonalizedSettingItem> = ArrayList()
    fun getItemsList() : ArrayList<PersonalizedSettingItem> { return mItemsList }

    fun setAllItems() {
        mItemsList.clear()

        val weatherItem : PersonalizedSettingItem = PersonalizedSettingItem(1, "", "", "")
        mItemsList.add(weatherItem)

        val themeItem : PersonalizedSettingItem = PersonalizedSettingItem(2, mContext.resources.getString(R.string.device_theme), mContext.resources.getString(R.string.colorful_theme), mContext.resources.getString(R.string.wire_theme))
        mItemsList.add(themeItem)

        val menuItem : PersonalizedSettingItem = PersonalizedSettingItem(2, mContext.resources.getString(R.string.menu), mContext.resources.getString(R.string.default_theme), mContext.resources.getString(R.string.custom_theme))
        mItemsList.add(menuItem)
    }
}