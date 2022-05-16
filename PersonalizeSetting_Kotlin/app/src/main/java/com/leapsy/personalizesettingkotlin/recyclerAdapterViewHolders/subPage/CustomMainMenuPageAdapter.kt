package com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.subPage

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leapsy.personalizesettingkotlin.GlobalDefine
import com.leapsy.personalizesettingkotlin.R
import com.leapsy.personalizesettingkotlin.databinding.MemberCustomMainMenuListItemBinding
import com.leapsy.personalizesettingkotlin.jsonHelper.CustomPersonalizedSettingData
import com.leapsy.personalizesettingkotlin.menuHelper.GetAllInstalledAppInfo
import com.leapsy.personalizesettingkotlin.menuHelper.InstalledItems
import com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.IGetCurrentHoveredTag
import com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.ISelectIconToMainMenu
import java.lang.String
import java.util.ArrayList

class CustomMainMenuPageAdapter(aContext : Context, aGetAllInstalledAppInfo : GetAllInstalledAppInfo) : RecyclerView.Adapter<CustomMainMenuViewHolder>() {
    private var mContext : Context = aContext
    private var mGetAllInstalledAppInfo : GetAllInstalledAppInfo = aGetAllInstalledAppInfo

    /**
     * 目前已選擇了多少個icon, 初始值為3, 分別是關機 設定 程式集
     */
    private var mCurSelectedAmountIcon : Int = 0

    private var selected : IntArray? = null

    /**
     * 儲存已選取的icon ArrayList
     */
    private var mSelectedIconList : ArrayList<InstalledItems> = ArrayList()
    /**
     * 儲存已選取的icon ArrayList
     */
    fun getSelectedIconList() : ArrayList<InstalledItems> { return mSelectedIconList }

    private var mThemeMode : Int = GlobalDefine.COLORFUL_THEME

    private var mISelectIconToMainMenuCallback : ISelectIconToMainMenu? = null
    private var mIGetCurrentHoveredTag : IGetCurrentHoveredTag? = null
    /**
     * set callback
     * @param aISelectIconToMainMenuCallback
     */
    fun setISelectIconToMainMenuCallback(aISelectIconToMainMenuCallback : ISelectIconToMainMenu?, aIGetCurrentHoveredTag : IGetCurrentHoveredTag?) {
        mISelectIconToMainMenuCallback = aISelectIconToMainMenuCallback
        mIGetCurrentHoveredTag = aIGetCurrentHoveredTag
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomMainMenuViewHolder {
        val customMainMenuBinding = MemberCustomMainMenuListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomMainMenuViewHolder(customMainMenuBinding)
    }

    override fun onBindViewHolder(holder: CustomMainMenuViewHolder, position: Int) {
        val item : InstalledItems = mGetAllInstalledAppInfo.getInstalledItemsList()[position]

        holder.mainMenuBinding.itemThumbnailImageView.setBackgroundColor(0x00000000)
        when (item.getItemPackageName()) {
            // 相機
            GlobalDefine.LEAPSY_CAMERA_PACKAGE_NAME -> { holder.mainMenuBinding.itemThumbnailImageView.setImageResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.mipmap.app_icon_color_camera else R.mipmap.appicon_wire_camera) }
            // 相簿
            GlobalDefine.LEAPSY_PHOTOS_PACKAGE_NAME -> { holder.mainMenuBinding.itemThumbnailImageView.setImageResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.mipmap.app_icon_color_photos else R.mipmap.appicon_wire_photos) }
            // 軟體更新
            GlobalDefine.LEAPSY_UPDATE_PACKAGE_NAME -> { holder.mainMenuBinding.itemThumbnailImageView.setImageResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.mipmap.app_icon_color_app_update else R.mipmap.appicon_wire_app_update) }
            // 網頁
            GlobalDefine.LEAPSY_WEB_BROWSER_PACKAGE_NAME -> { holder.mainMenuBinding.itemThumbnailImageView.setImageResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.mipmap.app_icon_color_web_browser else R.mipmap.appicon_wire_web_browser) }
            // message
            GlobalDefine.LEAPSY_MESSAGE_PACKAGE_NAME -> { holder.mainMenuBinding.itemThumbnailImageView.setImageResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.mipmap.app_icon_color_message else R.mipmap.appicon_wire_message) }
            // 宇博專家
            GlobalDefine.LEAPSY_EXPERT_GLASSES_PACKAGE_NAME -> { holder.mainMenuBinding.itemThumbnailImageView.setImageResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.mipmap.appicon_color_expert_system_glasses else R.mipmap.appicon_wire_expert_system_glasses) }
            // 環控
            GlobalDefine.Leapsy_ENVIRONMENT_SYSTEM_PACKAGE_NAME -> { holder.mainMenuBinding.itemThumbnailImageView.setImageResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.mipmap.appicon_color_ev_system else R.mipmap.appicon_wire_ev_system) }
            // 故宫
            GlobalDefine.LEAPSY_PALACE_MUSEUM_PACKAGE_NAME -> { holder.mainMenuBinding.itemThumbnailImageView.setImageResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.mipmap.appicon_color_forbidden_city_ar_guide else R.mipmap.appicon_wire_forbidden_city_ar_guide) }
            // QR Code 掃描
            GlobalDefine.LEAPSY_QR_CODE_SCANNER_PACKAGE_NAME -> { holder.mainMenuBinding.itemThumbnailImageView.setImageResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.mipmap.qr_code_logo else R.mipmap.qr_code_logo_wire) }
            // else
            else -> {
                if (mThemeMode == GlobalDefine.COLORFUL_THEME) {
                    holder.mainMenuBinding.itemThumbnailImageView.setImageDrawable(item.getItemColorfulDrawable())
                } else {
                    holder.mainMenuBinding.itemThumbnailImageView.setImageResource(R.mipmap.android_white)
                }
            }
        }
        holder.mainMenuBinding.itemNameTextView.text = item.getItemName()
        holder.mainMenuBinding.customMainMenuSelectedTextView.text = if (item.getSelectedOrder() >= 0) String.valueOf(item.getSelectedOrder()) else ""

        holder.mainMenuBinding.customMainMenuSelectedButton.clearFocus()
        holder.mainMenuBinding.customMainMenuSelectedButton.tag = "${position}_subItem"
        holder.mainMenuBinding.customMainMenuSelectedButton.setBackgroundResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.drawable.recyclerview_item_640x88_colorful_selector else R.drawable.recyclerview_item_640x88_wire_selector)
        holder.mainMenuBinding.customMainMenuSelectedButton.setOnClickListener {
            if (item.getSelectedOrder() < 0 && mCurSelectedAmountIcon <= 4) {
                mCurSelectedAmountIcon += 1
                item.setSelectedOrder(mCurSelectedAmountIcon)
                notifyItemChanged(position)
            } else if (item.getSelectedOrder() > 0 && mCurSelectedAmountIcon >= 1) {
                mCurSelectedAmountIcon -= 1
                cancelSelectItem(item.getSelectedOrder())
            }
            selectedListToDonutView()

            if (mSelectedIconList.size >= 3) {
                mISelectIconToMainMenuCallback?.onSelectedIcon(mSelectedIconList, mCurSelectedAmountIcon)
            }
        }
        holder.mainMenuBinding.customMainMenuSelectedButton.setOnHoverListener(object : View.OnHoverListener{
            override fun onHover(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_HOVER_ENTER -> { mIGetCurrentHoveredTag?.getCurHoveredTag(holder.mainMenuBinding.customMainMenuSelectedButton.tag.toString()) }
                    MotionEvent.ACTION_HOVER_MOVE -> {}
                    MotionEvent.ACTION_HOVER_EXIT -> {}
                }
                return false
            }
        })
    }

    override fun getItemCount() : Int { return mGetAllInstalledAppInfo.getInstalledItemsList().size }

    /**
     * 取消選擇
     * @param aCancelOrder 已選擇的序號
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun cancelSelectItem(aCancelOrder : Int) {
        for (i in 0 until mGetAllInstalledAppInfo.getInstalledItemsList().size) {
            if (mGetAllInstalledAppInfo.getInstalledItemsList()[i].getSelectedOrder() == aCancelOrder) {
                mGetAllInstalledAppInfo.getInstalledItemsList()[i].setSelectedOrder(-1)
            } else if (mGetAllInstalledAppInfo.getInstalledItemsList()[i].getSelectedOrder() > aCancelOrder){
                mGetAllInstalledAppInfo.getInstalledItemsList()[i].setSelectedOrder(mGetAllInstalledAppInfo.getInstalledItemsList()[i].getSelectedOrder() - 1)
            }
        }
        notifyDataSetChanged()
    }

    /**
     * 將選到的圖示顯示於甜甜圈中
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun selectedListToDonutView() {
        mSelectedIconList.clear()
        //關機
        val shutIcon : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_shutdown), mContext.resources.getString(R.string.shutdown_app), GlobalDefine.SHUTDOWN_PACKAGE_NAME)
        mSelectedIconList.add(shutIcon)

        //設定
        val settingIcon : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_settings), mContext.resources.getString(R.string.settings_app), GlobalDefine.SETTINGS_PACKAGE_NAME)
        mSelectedIconList.add(settingIcon)

        //程式集
        val programsIcon : InstalledItems = InstalledItems(mContext.getDrawable(R.mipmap.app_icon_color_apps), mContext.resources.getString(R.string.programs_app), GlobalDefine.LEAPSY_APPS_PACKAGE_NAME)
        mSelectedIconList.add(programsIcon)

        if (mGetAllInstalledAppInfo.getInstalledItemsList().size > 0) {
            for (i in 0 until  mGetAllInstalledAppInfo.getInstalledItemsList().size) {
                if (mGetAllInstalledAppInfo.getInstalledItemsList()[i].getSelectedOrder() > 0) {
                    mSelectedIconList.add(mGetAllInstalledAppInfo.getInstalledItemsList()[i])
                }
            }
        }
    }

    /**
     * 全部已選的順序歸零
     */
    @SuppressLint("NotifyDataSetChanged")
    fun resetSelectedOrder() {
        mCurSelectedAmountIcon = 0

        for (item in mGetAllInstalledAppInfo.getInstalledItemsList()) {
            item.setSelectedOrder(-1)
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setJsonRecordAppToAdapterData(aJsonRecordDataList : MutableList<CustomPersonalizedSettingData>) {
        if (aJsonRecordDataList.size > 2) {
            if (selected != null) {
                selected = null
            }

            selected = IntArray(aJsonRecordDataList.size - 3)
            for (i in 3 until aJsonRecordDataList.size) {
                //android.util.Log.d("xxxxxxxx", "i = ${i} ha: ${aJsonRecordDataList[i].getPackageName()}")
                for (j in 0 until mGetAllInstalledAppInfo.getInstalledItemsList().size) {
                    //android.util.Log.d("xxxxxxxx", "j = ${j} ha: ${aJsonRecordDataList[i].getPackageName()}")
                    if (aJsonRecordDataList[i].getPackageName() == mGetAllInstalledAppInfo.getInstalledItemsList()[j].getItemPackageName()) {
                        selected!![i - 3] = j
                        android.util.Log.d("xxxxxxxxx@", "ha: ${aJsonRecordDataList[i].getPackageName()}, j = ${j}")

                        mGetAllInstalledAppInfo.getInstalledItemsList()[j].setSelectedOrder(i - 2)
                        mCurSelectedAmountIcon = i - 2
                    }
                }
            }

            notifyDataSetChanged()
        }
    }

    /**
     * set theme mode
     * @param aMode 1. colorful, 2. wire
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setThemeMode(aMode : Int) {
        mThemeMode = aMode
        notifyDataSetChanged()
    }
}