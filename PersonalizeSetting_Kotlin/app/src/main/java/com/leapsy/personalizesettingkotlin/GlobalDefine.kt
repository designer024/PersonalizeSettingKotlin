package com.leapsy.personalizesettingkotlin

import android.os.Environment
import java.io.File

class GlobalDefine {
    companion object {
        /**
         * 當前是天氣是否有被開啟, 1 = show, -1 = hide
         */
        var WHICH_WEATHER_INFO : Int = 1

        /**
         * 當前是啥主題 1 = colorful, 2 = wired
         */
        var WHICH_THEME : Int = 1
        /**
         * 1.炫彩模式
         */
        const val COLORFUL_THEME : Int = 1
        /**
         * 2.簡約模式
         */
        const val WIRE_THEME : Int = 2

        /**
         * 當前主菜單是啥設定, 1 = default, 2 = custom
         */
        var WHICH_MENU : Int = 1
        /**
         * 1. 默認的
         */
        const val DEFAULT_MENU : Int = 1
        /**
         * 2. 自作主張的
         */
        const val CUSTOM_MENU : Int = 2

        val PERSONAL_SETTING_INIT_JSON_CONTENT : String = String.format(
            "%s%s%d%s%d%s%d%s%d%s%s%s%s%s%s%s%s%d%s%s%s%s%s%s%s%d%s%s%s%s%s%s%s%d%s%s%s",
            "{\n",
            "\"weather_info\":", 1,
            ",\n\"today_weather_id\":", 800,
            ",\n\"color_mode\":", 1,
            ",\n\"is_default\":", 1,
            ",\n\"custom_data\":", "[\n",
            "{\n",
            "\"appName\":", "\"關機\",\n",
            "\"packageName\":", "\"com.leapsy.shutdown\",\n",
            "\"appOrder\":", 0,
            "\n},",
            "{\n",
            "\"appName\":", "\"設定\",\n",
            "\"packageName\":", "\"com.leapsy.settings\",\n",
            "\"appOrder\":", 1,
            "\n},",
            "{\n",
            "\"appName\":", "\"應用程式\",\n",
            "\"packageName\":", "\"com.leapsy.leapsyapps\",\n",
            "\"appOrder\":", 2,
            "\n}",
            "\n]",
            "\n}")


        val SDCARD_ROOT_PATH = String.format("%s%s", Environment.getExternalStorageDirectory().path, File.separator)
        const val EXTERNAL_STORAGE_PATH = "/storage/emulated/0/"

        val BASE_PUBLIC_DOWNLOAD_PATH = String.format("%s%s%s", "/storage/emulated/0/", Environment.DIRECTORY_DOWNLOADS, File.separator)

        const val PERSONALIZED_SETTING_FOLDER : String  = ".PersonalizedSetting"

        const val PERSONALIZED_SETTING_JSON_FILE_NAME : String  = "personalizedSetting.json"

        const val WEATHER_INFO_KEY : String = "weather_info"
        const val TODAY_WEATHER_ID_KEY : String = "today_weather_id"
        const val COLOR_MODE_KEY : String = "color_mode"
        const val IS_DEFAULT_KEY : String = "is_default"

        const val CUSTOM_DATA_KEY : String = "custom_data"
        const val APP_NAME_KEY : String = "appName"
        const val PACKAGE_NAME_KEY : String = "packageName"
        const val APP_ICON_KEY : String = "appIcon"


        /**
         * Leapsy Home
         */
        const val LEAPSY_HOME_PACKAGE_NAME = "com.leapsy.home"

        /**
         * Vici OS Settings package name
         */
        const val LEAPSY_SETTINGS_PACKAGE_NAME = "com.leapsy.settings"
        /**
         * 關機 real package name
         */
        const val SHUTDOWN_REAL_PACKAGE_NAME = "com.leapsy.xr1.softoff"
        /**
         * 關機
         */
        const val SHUTDOWN_PACKAGE_NAME = "com.leapsy.shutdown"
        /**
         * 重關機的package name
         */
        const val REBOOT_PACKAGE_NAME = "com.leapsy.xr1.softreboot"
        /**
         * 設定  com.android.settings
         */
        const val SETTINGS_PACKAGE_NAME = "com.leapsy.settings"
        /**
         * 程式集
         */
        const val LEAPSY_APPS_PACKAGE_NAME = "com.leapsy.leapsyapps"
        /**
         * 相機
         */
        const val LEAPSY_CAMERA_PACKAGE_NAME = "com.leapsy.leapsycamera"
        /**
         * 相簿
         */
        const val LEAPSY_PHOTOS_PACKAGE_NAME = "com.leapsy.album"
        /**
         * 軟體更新
         */
        const val LEAPSY_UPDATE_PACKAGE_NAME = "com.leapsy.appupdatecenter"
        /**
         * Web Browser
         *
         * mobi.mgeek.TunnyBrowser Dolphin browser
         * acr.browser.barebones  Light browser
         */
        const val LEAPSY_WEB_BROWSER_PACKAGE_NAME = "acr.browser.barebones"
        /**
         * 通知
         */
        const val LEAPSY_MESSAGE_PACKAGE_NAME = "com.leapsy.message"
        /**
         * 宇博專家
         */
        const val LEAPSY_EXPERT_GLASSES_PACKAGE_NAME = "com.leapsy.experterglass"
        /**
         * 環控
         */
        const val Leapsy_ENVIRONMENT_SYSTEM_PACKAGE_NAME = "com.Leapsy.EnvironmentSystem"
        /**
         * 故宫 SE
         */
        const val LEAPSY_PALACE_MUSEUM_PACKAGE_NAME = "com.leapsy.PalaceMuseumSE"
        /**
         * 教學
         */
        const val LEAPSY_TUTOR_PACKAGE_NAME = "com.leapsy.leapsytutor"
        /**
         * QR Code 掃描
         */
        const val LEAPSY_QR_CODE_SCANNER_PACKAGE_NAME = "com.leapsy.qrcodescan"
        /**
         * 南科眼鏡
         */
        const val LEAPSY_FH1_PACKAGE_NAME = "com.Leapsy.FHOneHelmet"
        /**
         * 教育訓練
         */
        const val LEAPSY_EDU_PACKAGE_NAME = "com.leapsy.education_and_training"
        /**
         * 教育訓練 new
         */
        const val LEAPSY_EDU_TRAINING = "com.leapsy.education"
    }

    /**
     * 現在是在哪個頁面
     */
    class MainPageMode {
        companion object {
            /**
             * 儲存當前模式
             */
            var WhichPage : Int = 1
            /**
             * 現在在主頁
             */
            const val MAIN_PAGE : Int = 1
            /**
             * 現在在設定甜甜圈頁
             */
            const val SETTING_DONUT_PAGE : Int = 2
            /**
             * POP
             */
            const val POP_PAGE : Int = 11
        }
    }
}