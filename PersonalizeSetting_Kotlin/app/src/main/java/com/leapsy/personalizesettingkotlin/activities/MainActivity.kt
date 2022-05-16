package com.leapsy.personalizesettingkotlin.activities

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.leapsy.personalizesettingkotlin.GlobalDefine
import com.leapsy.personalizesettingkotlin.R
import com.leapsy.personalizesettingkotlin.jsonHelper.CustomPersonalizedSettingData
import com.leapsy.personalizesettingkotlin.jsonHelper.IGetJsonFileData
import com.leapsy.personalizesettingkotlin.jsonHelper.JsonFileWriteReadManager
import com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.*
import com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.mainPage.PersonalizeMainPageAdapter
import java.util.ArrayList

import com.google.android.material.imageview.ShapeableImageView
import com.leapsy.personalizesettingkotlin.menuHelper.InstalledItems
import com.leapsy.personalizesettingkotlin.menuHelper.MainMenuAppIconUtil
import com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.subPage.CustomMainMenuPageAdapter

class MainActivity : BaseActivity() {
    companion object {
        private const val TAG : String = "MainActivity"
    }

    /**
     * 主頁的recycler view 之 adapter
     */
    private lateinit var mPersonalizationSettingMainPageAdapter : PersonalizeMainPageAdapter
    /**
     * 主頁的recycler view的layout manager
     */
    private lateinit var mMainPageRecyclerViewLayoutManager : LinearLayoutManager

    /**
     * custom設定頁之recycler view 之 adapter
     */
    private lateinit var mPersonalizationCustomMainMenuRecyclerAdapter : CustomMainMenuPageAdapter
    /**
     * custom設定頁的recycler view的layout manager
     */
    private lateinit var mCustomRecyclerViewLayoutManager : LinearLayoutManager

    /**
     * Json檔裡記錄的主菜單APP數量
     */
    private var mJsonRecordDonutNumber : Int? = 0
    private var mJsonRecordDonutDataList : MutableList<CustomPersonalizedSettingData> = ArrayList()

    /**
     * 預設甜甜圈裡的icon們
     */
    private var mDefaultAppIcons : Array<ShapeableImageView>? = null
    /**
     * 自定義甜甜圈裡的圓形icon們
     */
    private var mCustomAppIcons : Array<ShapeableImageView>? = null
    /**
     * 已選擇之已安裝之清單
     */
    private var mSelectedIconList : ArrayList<InstalledItems> = ArrayList()

    /**
     * 當前被focus的tag
     */
    private var mCurFocusedTag : String = ""
    /**
     * 當前被hovered的tag
     */
    private var mCurHoveredTag : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        //setContentView(R.layout.activity_main)
        setContentView(activityMainBinding.root)

        initUi()
        setAdapterCallback()
        setJsonManagerCallback()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()

        setListener()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        onBack()
    }

    private fun initUi() {
        mPersonalizationSettingMainPageAdapter = PersonalizeMainPageAdapter(this@MainActivity)
        mMainPageRecyclerViewLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        activityMainBinding.personalizationSettingGroupRecyclerview.layoutManager = mMainPageRecyclerViewLayoutManager
        activityMainBinding.personalizationSettingGroupRecyclerview.adapter = mPersonalizationSettingMainPageAdapter

        mPersonalizationCustomMainMenuRecyclerAdapter = CustomMainMenuPageAdapter(this@MainActivity, getAllInstalledAppInfo)
        mCustomRecyclerViewLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        activityMainBinding.customMainMenuSelectionAppsRecyclerview.layoutManager = mCustomRecyclerViewLayoutManager
        activityMainBinding.customMainMenuSelectionAppsRecyclerview.adapter = mPersonalizationCustomMainMenuRecyclerAdapter
    }
    private fun setAdapterCallback() {
        mPersonalizationSettingMainPageAdapter.setCallback(object : ISelectTheme{
            override fun onThemeSelected(aThemeMode: Int) {
                android.util.Log.d("xxxxxxx", "theme: ${aThemeMode}")
                // 設定主題模式
                setThemeMode(aThemeMode)
            }
        }, object : ISetMainMenuToDefaultOrCustom{
            override fun onMainMenuSet(aIsMainMenuSetToDefault: Int) {
                android.util.Log.d("xxxxxxx", "is default: ${aIsMainMenuSetToDefault}")
                setMainMenuIsDefaultOrCustom(aIsMainMenuSetToDefault)
            }
        }, object : ISwitchWeatherInfo{
            override fun onSwitchWeatherInfo(aIsShowWeatherInfo: Int) {
                android.util.Log.d("xxxxxxx", "is show weather: ${aIsShowWeatherInfo}")
                // 設定待機頁是否要顯示天氣資訊
                setWeatherInfoShowOrHide(aIsShowWeatherInfo)
            }
        }, object : IGetCurrentHoveredTag{
            override fun getCurHoveredTag(aCurTag: String) {
                android.util.Log.d("xxxxxxx", "cur tag is: ${aCurTag}")
                mCurHoveredTag = aCurTag
            }
        })

        mPersonalizationCustomMainMenuRecyclerAdapter.setISelectIconToMainMenuCallback(object : ISelectIconToMainMenu{
            override fun onSelectedIcon(aSelectedIconList : ArrayList<InstalledItems>, aSelectedOrder: Int) {
                android.util.Log.d("xxxxxxx", "app selected: ${aSelectedIconList.size}")
                if (aSelectedIconList != null && aSelectedIconList.size > 0) {
                    mSelectedIconList = aSelectedIconList
                    setCustomDonutApp(resources.getDimensionPixelOffset(R.dimen.dp_40), aSelectedIconList.size)
                }
            }
        }, object : IGetCurrentHoveredTag{
            override fun getCurHoveredTag(aCurTag : String) {
                android.util.Log.d("xxxxxxx", "cur tag is: ${aCurTag}")
                mCurHoveredTag = aCurTag
            }
        })
    }
    /**
     * set json manager call back
     */
    private fun setJsonManagerCallback() {
        jsonFileWriteReadManager.setGetJsonDataCallback(object : IGetJsonFileData {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onGetJsonData(aIsShowWeather : Int?, aThemeMode : Int?, aIsDefault : Int?, aCustomDataList : MutableList<CustomPersonalizedSettingData>) {
                GlobalDefine.WHICH_WEATHER_INFO = aIsShowWeather!!
                GlobalDefine.WHICH_THEME = aThemeMode!!
                GlobalDefine.WHICH_MENU = aIsDefault!!
                mJsonRecordDonutNumber = aCustomDataList.size
                mJsonRecordDonutDataList = aCustomDataList

                android.util.Log.d("xxxxoox", "天氣:${GlobalDefine.WHICH_WEATHER_INFO}, 主題:${GlobalDefine.WHICH_THEME}, 預設:${GlobalDefine.WHICH_MENU}, size:${mJsonRecordDonutNumber}")
                mPersonalizationSettingMainPageAdapter.setThemeMode(GlobalDefine.WHICH_THEME)
                mPersonalizationCustomMainMenuRecyclerAdapter.setThemeMode(GlobalDefine.WHICH_THEME)
                mPersonalizationSettingMainPageAdapter.setJsonDataToAdapter(GlobalDefine.WHICH_WEATHER_INFO, GlobalDefine.WHICH_THEME, GlobalDefine.WHICH_MENU)
                activityMainBinding.donutOutlineImageView.setBackgroundResource(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) R.mipmap.set_personalizationpage_img_color_menu else R.mipmap.set_personalizationpage_img_wire_menu)
                activityMainBinding.popBackgroundLayout.setBackgroundResource(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) R.mipmap.stste_color_pop_default else R.mipmap.stste_wire_pop_default)

                // 如果有自定其他的APP
                if (mJsonRecordDonutDataList!!.size > 2) {
                    mSelectedIconList.clear()
                    for (i in 0 until mJsonRecordDonutDataList!!.size) {
                        val item : InstalledItems = InstalledItems(getDrawable(R.mipmap.only_ring), mJsonRecordDonutDataList!![i].getAppName(), mJsonRecordDonutDataList!![i].getPackageName())
                        mSelectedIconList.add(item)
                    }
                }

                when (GlobalDefine.WHICH_MENU) {
                    GlobalDefine.CUSTOM_MENU -> {
                        if (aCustomDataList != null) {
                            setCustomDonutAppFromJsonData(resources.getDimensionPixelOffset(R.dimen.dp_40), aCustomDataList)
                            mPersonalizationCustomMainMenuRecyclerAdapter.setJsonRecordAppToAdapterData(aCustomDataList)
                        }
                    }
                    GlobalDefine.DEFAULT_MENU -> {
                        setDefaultDonutApp(GlobalDefine.WHICH_THEME, resources.getDimensionPixelOffset(R.dimen.dp_40), 8)
                    }
                }


                setCurrentPage(GlobalDefine.MainPageMode.MAIN_PAGE)
            }
        })
    }
    private fun setListener() {
        activityMainBinding.confirmCustomButton.setOnClickListener { onConfirmButtonClicked() }
        activityMainBinding.confirmCustomButton.setOnHoverListener(object : View.OnHoverListener{
            override fun onHover(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_HOVER_ENTER -> { mCurHoveredTag = "1_ovalButton" }
                    MotionEvent.ACTION_HOVER_MOVE -> {}
                    MotionEvent.ACTION_HOVER_EXIT -> {}
                }
                return false
            }
        })

        activityMainBinding.cancelCustomButton.setOnClickListener { onCancelButtonClicked() }
        activityMainBinding.cancelCustomButton.setOnHoverListener(object : View.OnHoverListener{
            override fun onHover(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_HOVER_ENTER -> { mCurHoveredTag = "2_ovalButton" }
                    MotionEvent.ACTION_HOVER_MOVE -> {}
                    MotionEvent.ACTION_HOVER_EXIT -> {}
                }
                return false
            }
        })
    }

    /**
     * 設定待機頁是否要顯示天氣資訊
     * @param aIsShow 1. show, -1. hide
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun setWeatherInfoShowOrHide(aIsShow : Int) {
        GlobalDefine.WHICH_WEATHER_INFO = aIsShow
        JsonFileWriteReadManager.updateJsonDataToFile(jsonFilePath, JsonFileWriteReadManager.getJsonFileContent(), GlobalDefine.WEATHER_INFO_KEY, GlobalDefine.WHICH_WEATHER_INFO, GlobalDefine.COLOR_MODE_KEY, GlobalDefine.WHICH_THEME, GlobalDefine.IS_DEFAULT_KEY, GlobalDefine.WHICH_MENU, GlobalDefine.CUSTOM_DATA_KEY, JsonFileWriteReadManager.getJsonCustomPersonalizedSettingData())
        if (mPersonalizationSettingMainPageAdapter.getGetAllPersonalizeSettingItems().getItemsList().size >= 2) {
            mPersonalizationSettingMainPageAdapter.getGetAllPersonalizeSettingItems().getItemsList()[0].setWeatherIsShow(aIsShow)
        }
        mPersonalizationSettingMainPageAdapter.notifyDataSetChanged()
        Toast.makeText(this@MainActivity, resources.getString(R.string.storage_settings), Toast.LENGTH_SHORT).show()
    }
    /**
     * 設定主題模式
     * @param aThemeMode 1.colorful, 2. simple
     */
    private fun setThemeMode(aThemeMode : Int) {
        GlobalDefine.WHICH_THEME = aThemeMode
        JsonFileWriteReadManager.updateJsonDataToFile(jsonFilePath, JsonFileWriteReadManager.getJsonFileContent(), GlobalDefine.WEATHER_INFO_KEY, GlobalDefine.WHICH_WEATHER_INFO, GlobalDefine.COLOR_MODE_KEY, GlobalDefine.WHICH_THEME, GlobalDefine.IS_DEFAULT_KEY, GlobalDefine.WHICH_MENU, GlobalDefine.CUSTOM_DATA_KEY, JsonFileWriteReadManager.getJsonCustomPersonalizedSettingData())
        activityMainBinding.donutOutlineImageView.setBackgroundResource(if (aThemeMode == GlobalDefine.COLORFUL_THEME) R.mipmap.set_personalizationpage_img_color_menu else R.mipmap.set_personalizationpage_img_wire_menu)
        activityMainBinding.popBackgroundLayout.setBackgroundResource(if (aThemeMode == GlobalDefine.COLORFUL_THEME) R.mipmap.stste_color_pop_default else R.mipmap.stste_wire_pop_default)
        mPersonalizationSettingMainPageAdapter.setThemeMode(aThemeMode)
        mPersonalizationCustomMainMenuRecyclerAdapter.setThemeMode(aThemeMode)
        if (mPersonalizationSettingMainPageAdapter.getGetAllPersonalizeSettingItems().getItemsList().size >= 2) {
            mPersonalizationSettingMainPageAdapter.getGetAllPersonalizeSettingItems().getItemsList()[1].setCurMode(aThemeMode)
        }
        when(aThemeMode) {
            GlobalDefine.COLORFUL_THEME -> {
                if (mDefaultAppIcons != null && mDefaultAppIcons!!.isNotEmpty()) {
                    for (i in mDefaultAppIcons!!.indices) {
                        mDefaultAppIcons!![i].background = setDefaultMenuAppIcon.getDefaultAppList()[i].getItemColorfulDrawable()
                    }
                }
                if (mCustomAppIcons != null && mCustomAppIcons!!.isNotEmpty()) {
                    if (mSelectedIconList != null && mSelectedIconList.size > 3) {
                        for (j in 0 until mSelectedIconList.size) {
                            mCustomAppIcons!![j].setImageDrawable(mSelectedIconList[j].getItemPackageName()?.let { getCustomAppIconsImage(it, GlobalDefine.WHICH_THEME) })
                        }
                    }
                }
            }
            GlobalDefine.WIRE_THEME -> {
                if (mDefaultAppIcons != null && mDefaultAppIcons!!.isNotEmpty()) {
                    for (i in mDefaultAppIcons!!.indices) {
                        mDefaultAppIcons!![i].background = setDefaultMenuAppIcon.getDefaultAppList()[i].getItemWireDrawable()
                    }
                }
                if (mCustomAppIcons != null && mCustomAppIcons!!.isNotEmpty()) {
                    if (mSelectedIconList != null && mSelectedIconList.size > 3) {
                        for (j in 0 until mSelectedIconList.size) {
                            mCustomAppIcons!![j].setImageDrawable(mSelectedIconList[j].getItemPackageName()?.let { getCustomAppIconsImage(it, GlobalDefine.WHICH_THEME) })
                        }
                    }
                }
            }
        }

        Toast.makeText(this@MainActivity, resources.getString(R.string.storage_settings), Toast.LENGTH_SHORT).show()
    }
    /**
     * 設定主選單是預設或自訂
     * @param aIsMainMenuSetToDefault 1. default, 2. custom
     */
    private fun setMainMenuIsDefaultOrCustom(aIsMainMenuSetToDefault : Int) {
        when (aIsMainMenuSetToDefault) {
            GlobalDefine.DEFAULT_MENU -> {
                GlobalDefine.WHICH_MENU = aIsMainMenuSetToDefault
                setCurrentPage(GlobalDefine.MainPageMode.MAIN_PAGE)
                setDefaultDonutApp(GlobalDefine.WHICH_THEME, resources.getDimensionPixelOffset(R.dimen.dp_40), 8)
                JsonFileWriteReadManager.updateJsonDataToFile(jsonFilePath, JsonFileWriteReadManager.getJsonFileContent(), GlobalDefine.WEATHER_INFO_KEY, GlobalDefine.WHICH_WEATHER_INFO, GlobalDefine.COLOR_MODE_KEY, GlobalDefine.WHICH_THEME, GlobalDefine.IS_DEFAULT_KEY, GlobalDefine.WHICH_MENU, GlobalDefine.CUSTOM_DATA_KEY, JsonFileWriteReadManager.getJsonCustomPersonalizedSettingData())
                Toast.makeText(this@MainActivity, resources.getString(R.string.storage_settings), Toast.LENGTH_SHORT).show()
            }
            GlobalDefine.CUSTOM_MENU -> {
                setCurrentPage(GlobalDefine.MainPageMode.SETTING_DONUT_PAGE)
                setCustomDonutAppFromJsonData(resources.getDimensionPixelOffset(R.dimen.dp_40), mJsonRecordDonutDataList)
                mPersonalizationCustomMainMenuRecyclerAdapter.setJsonRecordAppToAdapterData(mJsonRecordDonutDataList)
            }
        }
    }

    /**
     * 現在是在哪一頁面
     * @param aCurPage
     */
    private fun setCurrentPage(aCurPage : Int) {
        GlobalDefine.MainPageMode.WhichPage = aCurPage
        setSomeUiGone()

        when (aCurPage) {
            // 主頁
            GlobalDefine.MainPageMode.MAIN_PAGE -> {
                activityMainBinding.mainPageTitleTextView.text = resources.getString(R.string.theme_title)
                activityMainBinding.mainPageLayout.visibility = View.VISIBLE
                activityMainBinding.iconsContainerMainPageLayout.visibility = if (GlobalDefine.WHICH_MENU == GlobalDefine.DEFAULT_MENU) View.VISIBLE else View.GONE
                activityMainBinding.iconsContainerCustomPageLayout.visibility = if (GlobalDefine.WHICH_MENU == GlobalDefine.CUSTOM_MENU) View.VISIBLE else View.GONE
            }
            // 設定甜甜圈頁
            GlobalDefine.MainPageMode.SETTING_DONUT_PAGE -> {
                activityMainBinding.mainPageTitleTextView.text = resources.getString(R.string.main_menu_setting_title)
                activityMainBinding.customSettingPageLayout.visibility = View.VISIBLE
                activityMainBinding.iconsContainerCustomPageLayout.visibility = View.VISIBLE
            }
            // POP頁
            GlobalDefine.MainPageMode.POP_PAGE -> { activityMainBinding.popLayout.visibility = View.VISIBLE }
        }
    }
    /**
     * set some ui invisible
     */
    private fun setSomeUiGone() {
        activityMainBinding.mainPageLayout.visibility = View.GONE
        activityMainBinding.customSettingPageLayout.visibility = View.GONE
        activityMainBinding.popLayout.visibility = View.GONE

        activityMainBinding.iconsContainerMainPageLayout.visibility = View.GONE
        activityMainBinding.iconsContainerCustomPageLayout.visibility = View.GONE
    }

    /**
     * 確認鍵按下時
     */
    private fun onConfirmButtonClicked() {
        GlobalDefine.WHICH_MENU = GlobalDefine.CUSTOM_MENU

        confirmCustomMainMenuSetting(mSelectedIconList)

        activityMainBinding.mainPageLayout.visibility = View.VISIBLE
        activityMainBinding.customSettingPageLayout.visibility = View.GONE

        jsonFileWriteReadManager.readJsonFileData(jsonFilePath)
    }
    /**
     * 按下確認鍵，將所選之APP 儲至json檔
     * @param aSelectedIconList
     */
    private fun confirmCustomMainMenuSetting(aSelectedIconList : ArrayList<InstalledItems>) {
        val mainMenuList : MutableList<CustomPersonalizedSettingData> = ArrayList()
        val custom0 : CustomPersonalizedSettingData = CustomPersonalizedSettingData(resources.getString(R.string.shutdown_app), GlobalDefine.SHUTDOWN_PACKAGE_NAME, 0)
        mainMenuList.add(custom0)

        val custom1 : CustomPersonalizedSettingData = CustomPersonalizedSettingData(resources.getString(R.string.settings_app), GlobalDefine.SETTINGS_PACKAGE_NAME, 1)
        mainMenuList.add(custom1)

        val custom2 : CustomPersonalizedSettingData = CustomPersonalizedSettingData(resources.getString(R.string.programs_app), GlobalDefine.LEAPSY_APPS_PACKAGE_NAME, 2)
        mainMenuList.add(custom2)

        for (i in 3 until aSelectedIconList.size) {
            val customItem : CustomPersonalizedSettingData = CustomPersonalizedSettingData(aSelectedIconList[i].getItemName(), aSelectedIconList[i].getItemPackageName(), i)
            mainMenuList.add(customItem)
        }

        JsonFileWriteReadManager.setJsonCustomPersonalizedSettingData(mainMenuList)
        JsonFileWriteReadManager.updateJsonDataToFile(jsonFilePath, JsonFileWriteReadManager.getJsonFileContent(), GlobalDefine.WEATHER_INFO_KEY, GlobalDefine.WHICH_WEATHER_INFO, GlobalDefine.COLOR_MODE_KEY, GlobalDefine.WHICH_THEME, GlobalDefine.IS_DEFAULT_KEY, GlobalDefine.WHICH_MENU, GlobalDefine.CUSTOM_DATA_KEY, JsonFileWriteReadManager.getJsonCustomPersonalizedSettingData())
        Toast.makeText(this@MainActivity, resources.getString(R.string.storage_settings), Toast.LENGTH_SHORT).show()
    }
    /**
     * 取消鍵按下時
     */
    private fun onCancelButtonClicked() {
        setCurrentPage(GlobalDefine.MainPageMode.MAIN_PAGE)
        mPersonalizationSettingMainPageAdapter.setJsonDataToAdapter(GlobalDefine.WHICH_WEATHER_INFO, GlobalDefine.WHICH_THEME, GlobalDefine.WHICH_MENU)
        mPersonalizationCustomMainMenuRecyclerAdapter.resetSelectedOrder()
        mPersonalizationCustomMainMenuRecyclerAdapter.setJsonRecordAppToAdapterData(mJsonRecordDonutDataList)
        jsonFileWriteReadManager.readJsonFileData(jsonFilePath)
    }

    /**
     * 預設甜甜圈的APP
     * @param aThemeMode 色彩模式
     * @param aDonutRadius 甜甜圈的半徑
     * @param aIconNumber 甜甜圈上APP的數量, 預設就8個
     */
    private fun setDefaultDonutApp(aThemeMode : Int, aDonutRadius : Int, aIconNumber : Int) {
        if (mDefaultAppIcons != null) {
            mDefaultAppIcons = null
        }

        mDefaultAppIcons = Array(aIconNumber) { ShapeableImageView(this@MainActivity) }
        for (i in 0 until aIconNumber) {
            mDefaultAppIcons!![i].background = if (aThemeMode == GlobalDefine.COLORFUL_THEME) setDefaultMenuAppIcon.getDefaultAppList()[i].getItemColorfulDrawable() else setDefaultMenuAppIcon.getDefaultAppList()[i].getItemWireDrawable()
            activityMainBinding.iconsContainerMainPageLayout.addView(mDefaultAppIcons!![i])
            mDefaultAppIcons!![i].layoutParams = MainMenuAppIconUtil.relativeLayoutParams(resources.getDimensionPixelOffset(R.dimen.dp_24), resources.getDimensionPixelOffset(R.dimen.dp_24))
            mDefaultAppIcons!![i].x = MainMenuAppIconUtil.getLauncherIconCoordinates(aDonutRadius.toDouble(), aIconNumber)[i][0].toFloat()
            mDefaultAppIcons!![i].y = MainMenuAppIconUtil.getLauncherIconCoordinates(aDonutRadius.toDouble(), aIconNumber)[i][1].toFloat()
        }
    }
    /**
     * custom甜甜圈的APP from Json
     * @param aDonutRadius 甜甜圈的半徑
     * @param aCustomDataList 甜甜圈上APP的數量, at least 3
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setCustomDonutAppFromJsonData(aDonutRadius : Int, aCustomDataList : MutableList<CustomPersonalizedSettingData>) {
        if (aCustomDataList.size > 2) {
            activityMainBinding.iconsContainerCustomPageLayout.removeAllViews()
            if (mCustomAppIcons != null) {
                mCustomAppIcons = null
            }
            mCustomAppIcons = Array(aCustomDataList.size) { ShapeableImageView(this@MainActivity) }
            for (i in 0 until aCustomDataList.size) {
                activityMainBinding.iconsContainerCustomPageLayout.addView(mCustomAppIcons!![i])
                mCustomAppIcons!![i].layoutParams = MainMenuAppIconUtil.relativeLayoutParams(resources.getDimensionPixelOffset(R.dimen.dp_24), resources.getDimensionPixelOffset(R.dimen.dp_24))
                mCustomAppIcons!![i].x = MainMenuAppIconUtil.getLauncherIconCoordinates(aDonutRadius.toDouble(), aCustomDataList.size)[i][0].toFloat()
                mCustomAppIcons!![i].y = MainMenuAppIconUtil.getLauncherIconCoordinates(aDonutRadius.toDouble(), aCustomDataList.size)[i][1].toFloat()
            }

            mCustomAppIcons!![0].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_shutdown) else getDrawable(R.mipmap.appicon_wire_shutdown))
            mCustomAppIcons!![1].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_settings) else getDrawable(R.mipmap.appicon_wire_settings))
            mCustomAppIcons!![2].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_apps) else getDrawable(R.mipmap.appicon_wire_apps))

            for (j in 3 until aCustomDataList.size) {
                mCustomAppIcons!![j].setImageDrawable(aCustomDataList[j].getPackageName()?.let { getCustomAppIconsImage(it, GlobalDefine.WHICH_THEME) })
                /*when (aCustomDataList[j].getPackageName()) {
                    // 相機
                    GlobalDefine.LEAPSY_CAMERA_PACKAGE_NAME -> { mCustomAppIcons!![j].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_camera) else getDrawable(R.mipmap.appicon_wire_camera)) }
                    // 相簿
                    GlobalDefine.LEAPSY_PHOTOS_PACKAGE_NAME -> { mCustomAppIcons!![j].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_photos) else getDrawable(R.mipmap.appicon_wire_photos)) }
                    // 軟體更新
                    GlobalDefine.LEAPSY_UPDATE_PACKAGE_NAME -> { mCustomAppIcons!![j].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_app_update) else getDrawable(R.mipmap.appicon_wire_app_update)) }
                    // web
                    GlobalDefine.LEAPSY_WEB_BROWSER_PACKAGE_NAME -> { mCustomAppIcons!![j].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_web_browser) else getDrawable(R.mipmap.appicon_wire_web_browser)) }
                    // message
                    GlobalDefine.LEAPSY_MESSAGE_PACKAGE_NAME -> { mCustomAppIcons!![j].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_message) else getDrawable(R.mipmap.appicon_wire_message)) }
                    // QR Code scanner
                    GlobalDefine.LEAPSY_QR_CODE_SCANNER_PACKAGE_NAME -> { mCustomAppIcons!![j].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.qr_code_logo) else getDrawable(R.mipmap.qr_code_logo_wire)) }
                    // 故宫
                    GlobalDefine.LEAPSY_PALACE_MUSEUM_PACKAGE_NAME -> { mCustomAppIcons!![j].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.appicon_color_forbidden_city_ar_guide) else getDrawable(R.mipmap.appicon_wire_forbidden_city_ar_guide)) }
                    // 宇博專家
                    GlobalDefine.LEAPSY_EXPERT_GLASSES_PACKAGE_NAME -> { mCustomAppIcons!![j].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.appicon_color_expert_system_glasses) else getDrawable(R.mipmap.appicon_wire_expert_system_glasses)) }
                    // 環控
                    GlobalDefine.Leapsy_ENVIRONMENT_SYSTEM_PACKAGE_NAME -> { mCustomAppIcons!![j].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.appicon_color_ev_system) else getDrawable(R.mipmap.appicon_wire_ev_system)) }
                    // else
                    else -> { mCustomAppIcons!![j].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getAllInstalledAppInfo.getInstalledItemsMap()[aCustomDataList[j].getPackageName()]?.getItemColorfulDrawable() else getDrawable(R.mipmap.android_white)) }
                }*/
            }
        } else {
            android.util.Log.d(TAG, "custom donut menu 沒有")
        }
    }
    /**
     * custom甜甜圈的APP 正在選取時
     * @param aDonutRadius 甜甜圈的半徑
     * @param aIconNumber 甜甜圈上APP的數量, at least 3
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setCustomDonutApp(aDonutRadius : Int, aIconNumber : Int) {
        if (aIconNumber > 2) {
            activityMainBinding.iconsContainerCustomPageLayout.removeAllViews()
            if (mCustomAppIcons != null) {
                mCustomAppIcons = null
            }
            mCustomAppIcons = Array(aIconNumber) { ShapeableImageView(this@MainActivity) }
            for (i in 0 until aIconNumber) {
                activityMainBinding.iconsContainerCustomPageLayout.addView(mCustomAppIcons!![i])
                mCustomAppIcons!![i].layoutParams = MainMenuAppIconUtil.relativeLayoutParams(resources.getDimensionPixelOffset(R.dimen.dp_24), resources.getDimensionPixelOffset(R.dimen.dp_24))
                mCustomAppIcons!![i].x = MainMenuAppIconUtil.getLauncherIconCoordinates(aDonutRadius.toDouble(), aIconNumber)[i][0].toFloat()
                mCustomAppIcons!![i].y = MainMenuAppIconUtil.getLauncherIconCoordinates(aDonutRadius.toDouble(), aIconNumber)[i][1].toFloat()
            }

            if (mSelectedIconList != null && mSelectedIconList.size == aIconNumber && mSelectedIconList.size > 3) {
                for (j in 0 until mSelectedIconList.size) {
                    mCustomAppIcons!![j].setImageDrawable(mSelectedIconList[j].getItemPackageName()?.let { getCustomAppIconsImage(it, GlobalDefine.WHICH_THEME) })
                }
            }

            mCustomAppIcons!![0].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_shutdown) else getDrawable(R.mipmap.appicon_wire_shutdown))
            mCustomAppIcons!![1].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_settings) else getDrawable(R.mipmap.appicon_wire_settings))
            mCustomAppIcons!![2].setImageDrawable(if (GlobalDefine.WHICH_THEME == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_apps) else getDrawable(R.mipmap.appicon_wire_apps))
        } else {
            android.util.Log.d(TAG, "custom donut menu 沒有")
        }
    }

    /**
     * set image to icon of custom apps
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getCustomAppIconsImage(aPackageName : String, aThemeMode : Int) : Drawable? {
        when (aPackageName) {
            // 相機
            GlobalDefine.LEAPSY_CAMERA_PACKAGE_NAME -> { return if (aThemeMode == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_camera) else getDrawable(R.mipmap.appicon_wire_camera) }
            // 相簿
            GlobalDefine.LEAPSY_PHOTOS_PACKAGE_NAME -> { return if (aThemeMode == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_photos) else getDrawable(R.mipmap.appicon_wire_photos) }
            // 軟體更新
            GlobalDefine.LEAPSY_UPDATE_PACKAGE_NAME -> { return if (aThemeMode == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_app_update) else getDrawable(R.mipmap.appicon_wire_app_update) }
            // web
            GlobalDefine.LEAPSY_WEB_BROWSER_PACKAGE_NAME -> { return if (aThemeMode == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_web_browser) else getDrawable(R.mipmap.appicon_wire_web_browser) }
            // message
            GlobalDefine.LEAPSY_MESSAGE_PACKAGE_NAME -> { return if (aThemeMode == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.app_icon_color_message) else getDrawable(R.mipmap.appicon_wire_message) }
            // QR Code scanner
            GlobalDefine.LEAPSY_QR_CODE_SCANNER_PACKAGE_NAME -> { return if (aThemeMode == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.qr_code_logo) else getDrawable(R.mipmap.qr_code_logo_wire) }
            // 故宫
            GlobalDefine.LEAPSY_PALACE_MUSEUM_PACKAGE_NAME -> { return if (aThemeMode == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.appicon_color_forbidden_city_ar_guide) else getDrawable(R.mipmap.appicon_wire_forbidden_city_ar_guide) }
            // 宇博專家
            GlobalDefine.LEAPSY_EXPERT_GLASSES_PACKAGE_NAME -> { return if (aThemeMode == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.appicon_color_expert_system_glasses) else getDrawable(R.mipmap.appicon_wire_expert_system_glasses) }
            // 環控
            GlobalDefine.Leapsy_ENVIRONMENT_SYSTEM_PACKAGE_NAME -> { return if (aThemeMode == GlobalDefine.COLORFUL_THEME) getDrawable(R.mipmap.appicon_color_ev_system) else getDrawable(R.mipmap.appicon_wire_ev_system) }
            // else
            else -> { return if (aThemeMode == GlobalDefine.COLORFUL_THEME) getAllInstalledAppInfo.getInstalledItemsMap()[aPackageName]?.getItemColorfulDrawable() else getDrawable(R.mipmap.android_white) }
        }
    }

    /**
     * 按下 back 鍵
     */
    private fun onBack() {
        when (GlobalDefine.MainPageMode.WhichPage) {
            GlobalDefine.MainPageMode.MAIN_PAGE -> { finish() }
            GlobalDefine.MainPageMode.SETTING_DONUT_PAGE -> { onCancelButtonClicked() }
            GlobalDefine.MainPageMode.POP_PAGE -> {}
        }
    }
}