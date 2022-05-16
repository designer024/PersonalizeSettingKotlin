package com.leapsy.personalizesettingkotlin.menuHelper

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import com.leapsy.personalizesettingkotlin.GlobalDefine
import com.leapsy.personalizesettingkotlin.R
import java.io.File
import java.io.FileReader
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class GetAllInstalledAppInfo(aContext : Context) : AppCompatActivity(){

    private val mTempList: ArrayList<InstalledItems> = ArrayList<InstalledItems>()

    private val mInstalledItemsList : ArrayList<InstalledItems> = ArrayList()
    fun getInstalledItemsList() : ArrayList<InstalledItems> { return mInstalledItemsList }

    private val mInstalledItemsMap : HashMap<String, InstalledItems> = HashMap()
    fun getInstalledItemsMap() : HashMap<String, InstalledItems> { return mInstalledItemsMap }

    private var mContext : Context = aContext
    private var mPackageManager : PackageManager = aContext.packageManager

    @SuppressLint("UseCompatLoadingForDrawables", "QueryPermissionsNeeded")
    fun getAllAppInstalled() {
        val packagesList: List<ApplicationInfo> = mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        clearAllInstalledAppInfoLists()

        for (applicationInfo in packagesList) {
            var packageInfo : PackageInfo? = null
            try {
                packageInfo = mPackageManager.getPackageInfo(applicationInfo.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            if (packageInfo != null && !applicationInfo.packageName.contains("com.android")) {

                val appLabel : String = mPackageManager.getApplicationLabel(applicationInfo).toString()

                // 不能加入清單: 自己、主選單、天氣頁、教學 leapsy鍵盤
                if (packageInfo.packageName != "com.leapsy.settings" && packageInfo.packageName != "com.leapsy.personalizesettingkotlin" && packageInfo.packageName != "com.leapsy.education" && packageInfo.packageName != "com.leapsy.notifylistenerservice" && packageInfo.packageName != "com.leapsy.listenerglobal" && packageInfo.packageName != "com.leapsy.tutorforinitsetting" && packageInfo.packageName != "com.leapsy.floatinginitsettings" && packageInfo.packageName != "com.EthanLin.CallbackTest" && packageInfo.packageName != "com.leapsy.viciosinitsettings" && packageInfo.packageName != "com.leapsy.commandservice" && packageInfo.packageName != "com.leapsy.voicecommandservice" && packageInfo.packageName != "com.leapsy.xr1.softreboot" && packageInfo.packageName != "com.leapsy.xr1.softoff" && packageInfo.packageName != "acr.browser.barebones" && packageInfo.packageName != "org.chromium.webview_shell" && packageInfo.packageName != "com.cghs.stresstest" && packageInfo.packageName != "com.svox.pico" && packageInfo.packageName != "android.auto_generated_rro__" && packageInfo.packageName != "com.leapsy.autoclickservice" && packageInfo.packageName != "com.leapsy.commandservice" && packageInfo.packageName != "com.DeviceTest" && packageInfo.packageName != "android.ext.shared" && packageInfo.packageName != "android.ext.services" && packageInfo.packageName != "android" && packageInfo.packageName != "android.rockchip.update.service" && packageInfo.packageName != "com.leapsy.leapsyhomeX" && packageInfo.packageName != "com.leapsy.leapsyappsX" && packageInfo.packageName != "com.android.providers.settings" && packageInfo.packageName != "com.android.launcher3" && packageInfo.packageName != "com.leapsy.launcherkeyboard" && packageInfo.packageName != "com.leapsy.mainmenu" && packageInfo.packageName != "com.leapsy.personalizedsetting" && packageInfo.packageName != "com.leapsy.leapsyapps" && packageInfo.packageName != "com.leapsy.home" && packageInfo.packageName != "com.leapsy.leapsytutor") {
                    val item : InstalledItems = InstalledItems(packageInfo.applicationInfo.loadIcon(mPackageManager), appLabel, packageInfo.packageName)
                    mTempList.add(item)
                }
            }
        }

        // android system settings
        val webViewItem : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_web_browser), mContext.resources.getString(R.string.web_browser_app), GlobalDefine.LEAPSY_WEB_BROWSER_PACKAGE_NAME)
        mTempList.add(webViewItem)

        val sortList : ArrayList<ToSort> = ArrayList()
        for (item in mTempList) {
            val toSort = item.getItemName()?.let { ToSort(it, item) }
            if (toSort != null) {
                sortList.add(toSort)
            }
        }

        sortList.sort()

        for (i in sortList.indices) {
            mInstalledItemsList.add(sortList[i].mItem)
            sortList[i].mItem.getItemPackageName()?.let { mInstalledItemsMap.put(it, sortList[i].mItem) }
            //android.util.Log.d("xxxxoox", "${i} 依序有: ${sortList[i].mId}")
        }
        //android.util.Log.d("xxxxoox", "${mInstalledItemsMap.keys}")
    }

    private fun clearAllInstalledAppInfoLists() {
        mTempList.clear()
        mInstalledItemsList.clear()
        mInstalledItemsMap.clear()
    }

    class ToSort(val mId : String, val mItem : InstalledItems) : Comparable<ToSort> {

        override fun compareTo(other: ToSort): Int {

            return if (mId > other.mId) {
                1
            } else if (mId < other.mId) {
                -1
            } else {
                0
            }
        }
    }
}