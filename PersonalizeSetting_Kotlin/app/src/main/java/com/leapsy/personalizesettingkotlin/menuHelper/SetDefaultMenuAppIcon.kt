package com.leapsy.personalizesettingkotlin.menuHelper

import android.annotation.SuppressLint
import android.content.Context
import com.leapsy.personalizesettingkotlin.GlobalDefine
import com.leapsy.personalizesettingkotlin.R
import java.util.ArrayList

class SetDefaultMenuAppIcon(aContext : Context) {
    private var mContext : Context = aContext

    private var mDefaultAppList : ArrayList<InstalledItems> = ArrayList()
    fun getDefaultAppList() : ArrayList<InstalledItems> { return mDefaultAppList }

    /**
     * 設定8個預設主菜單app
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setAllDefaultApp() {
        mDefaultAppList.clear()

        val shutdownItem : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_shutdown), mContext.getDrawable(R.mipmap.appicon_wire_shutdown), mContext.resources.getString(R.string.shutdown_app), GlobalDefine.SHUTDOWN_PACKAGE_NAME)
        mDefaultAppList.add(shutdownItem)

        val settingsItem : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_settings), mContext.getDrawable(R.mipmap.appicon_wire_settings), mContext.resources.getString(R.string.settings_app), GlobalDefine.SETTINGS_PACKAGE_NAME)
        mDefaultAppList.add(settingsItem)

        val appsItem : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_apps), mContext.getDrawable(R.mipmap.appicon_wire_apps), mContext.resources.getString(R.string.programs_app), GlobalDefine.LEAPSY_APPS_PACKAGE_NAME)
        mDefaultAppList.add(appsItem)

        val cameraItem : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_camera), mContext.getDrawable(R.mipmap.appicon_wire_camera), mContext.resources.getString(R.string.camera_app), GlobalDefine.LEAPSY_CAMERA_PACKAGE_NAME)
        mDefaultAppList.add(cameraItem)

        val albumItem : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_photos), mContext.getDrawable(R.mipmap.appicon_wire_photos), mContext.resources.getString(R.string.album_app), GlobalDefine.LEAPSY_PHOTOS_PACKAGE_NAME)
        mDefaultAppList.add(albumItem)

        val appManagerItem : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_app_update), mContext.getDrawable(R.mipmap.appicon_wire_app_update), mContext.resources.getString(R.string.update_app), GlobalDefine.LEAPSY_UPDATE_PACKAGE_NAME)
        mDefaultAppList.add(appManagerItem)

        val webItem : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_web_browser), mContext.getDrawable(R.mipmap.appicon_wire_web_browser), mContext.resources.getString(R.string.web_browser_app), GlobalDefine.LEAPSY_WEB_BROWSER_PACKAGE_NAME)
        mDefaultAppList.add(webItem)

        val messageItem : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_message), mContext.getDrawable(R.mipmap.appicon_wire_message), mContext.resources.getString(R.string.messages_app), GlobalDefine.LEAPSY_MESSAGE_PACKAGE_NAME)
        mDefaultAppList.add(messageItem)
    }
}