package com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.mainPage

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leapsy.personalizesettingkotlin.GlobalDefine
import com.leapsy.personalizesettingkotlin.R
import com.leapsy.personalizesettingkotlin.databinding.MemberPersonalizeSettingItemBinding
import com.leapsy.personalizesettingkotlin.databinding.MemberToggleWeatherInfoItemBinding
import com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.IGetCurrentHoveredTag
import com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.ISelectTheme
import com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.ISetMainMenuToDefaultOrCustom
import com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.ISwitchWeatherInfo
import com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.data.GetAllPersonalizeSettingItems
import com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.data.PersonalizedSettingItem

class PersonalizeMainPageAdapter(aContext : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mContext : Context = aContext

    private val mGetAllPersonalizeSettingItems : GetAllPersonalizeSettingItems = GetAllPersonalizeSettingItems(aContext)
    fun getGetAllPersonalizeSettingItems() : GetAllPersonalizeSettingItems { return mGetAllPersonalizeSettingItems }

    init {
        mGetAllPersonalizeSettingItems.setAllItems()
    }

    private var mISelectThemeCallback : ISelectTheme? = null
    private var mISetMainMenuToDefaultOrCustom : ISetMainMenuToDefaultOrCustom? = null
    private var mISwitchWeatherInfoCallback : ISwitchWeatherInfo? = null
    private var mIGetCurrentHoveredTag : IGetCurrentHoveredTag? = null

    fun setCallback(aISelectThemeCallback : ISelectTheme?, aISetMainMenuToDefaultOrCustom : ISetMainMenuToDefaultOrCustom?, aISwitchWeatherInfoCallback : ISwitchWeatherInfo?, aIGetCurrentHoveredTag : IGetCurrentHoveredTag?) {
        mISelectThemeCallback = aISelectThemeCallback
        mISetMainMenuToDefaultOrCustom = aISetMainMenuToDefaultOrCustom
        mISwitchWeatherInfoCallback = aISwitchWeatherInfoCallback
        mIGetCurrentHoveredTag = aIGetCurrentHoveredTag
    }

    private var mThemeMode : Int = GlobalDefine.COLORFUL_THEME

    override fun getItemViewType(position : Int) : Int {
        return mGetAllPersonalizeSettingItems.getItemsList()[position].aItemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val wBinding = MemberToggleWeatherInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return WeatherInfoViewHolder(wBinding)
        } else {
            val mBinding = MemberPersonalizeSettingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomMenuThemeViewHolder(mBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item : PersonalizedSettingItem = mGetAllPersonalizeSettingItems.getItemsList()[position]

        when (holder) {
            // 天氣資訊
            is WeatherInfoViewHolder -> {
                holder.weatherBinding.switchWeatherInfoButton.tag = "0_mainItem"
                holder.weatherBinding.switchWeatherInfoButton.clearFocus()
                holder.weatherBinding.switchWeatherInfoButton.setBackgroundResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.drawable.recyclerview_item_640x88_colorful_selector else R.drawable.recyclerview_item_640x88_wire_selector)
                holder.weatherBinding.switchWeatherInfoButton.setOnClickListener {
                    item.setWeatherIsShow(-(item.getWeatherIsShow()))
                    mISwitchWeatherInfoCallback?.onSwitchWeatherInfo(item.getWeatherIsShow())
                    notifyItemChanged(position)
                }
                holder.weatherBinding.switchWeatherInfoButton.setOnHoverListener(object : View.OnHoverListener{
                    override fun onHover(v: View?, event: MotionEvent?): Boolean {
                        when (event?.action) {
                            MotionEvent.ACTION_HOVER_ENTER -> {}
                            MotionEvent.ACTION_HOVER_MOVE -> {}
                            MotionEvent.ACTION_HOVER_EXIT -> {}
                        }
                        return false
                    }
                })

                if (item.getWeatherIsShow() == 1) {
                    holder.weatherBinding.switchButtonThumbnailOffImageView.visibility = View.GONE
                    holder.weatherBinding.switchButtonThumbnailOnImageView.visibility = View.VISIBLE
                    holder.weatherBinding.switchButtonBackImageView.setBackgroundResource(R.drawable.switch_on_default)
                } else {
                    holder.weatherBinding.switchButtonThumbnailOffImageView.visibility = View.VISIBLE
                    holder.weatherBinding.switchButtonThumbnailOnImageView.visibility = View.GONE
                    holder.weatherBinding.switchButtonBackImageView.setBackgroundResource(R.drawable.switch_off_default)
                }
            }

            // 主題 或 主選單
            is CustomMenuThemeViewHolder -> {
                holder.mainPersonalizePageBinding.subtitleTextView.text = item.aItemTitle
                holder.mainPersonalizePageBinding.itemLine1TitleTextView.text = item.aItemSubtitle1
                holder.mainPersonalizePageBinding.itemLine2TitleTextView.text = item.aItemSubtitle2
                holder.mainPersonalizePageBinding.itemLine1Button.setBackgroundResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.drawable.recyclerview_item_640x88_colorful_selector else R.drawable.recyclerview_item_640x88_wire_selector)
                holder.mainPersonalizePageBinding.itemLine2Button.setBackgroundResource(if (mThemeMode == GlobalDefine.COLORFUL_THEME) R.drawable.recyclerview_item_640x88_colorful_selector else R.drawable.recyclerview_item_640x88_wire_selector)
                when (position) {
                    // 炫彩 或 簡約
                    1 -> {
                        holder.mainPersonalizePageBinding.itemLine1IconImageView.setImageResource(R.mipmap.system_set_icon_color)
                        holder.mainPersonalizePageBinding.itemLine2IconImageView.setImageResource(R.mipmap.system_set_icon_wire)
                        if (item.getCurMode() == GlobalDefine.COLORFUL_THEME) {
                            holder.mainPersonalizePageBinding.itemLine1StateImageView.visibility = View.GONE
                            holder.mainPersonalizePageBinding.itemLine2StateImageView.visibility = View.VISIBLE
                            holder.mainPersonalizePageBinding.itemLine1StateTextView.text = mContext.resources.getString(R.string.applied)
                            holder.mainPersonalizePageBinding.itemLine2StateTextView.text = ""
                        } else {
                            holder.mainPersonalizePageBinding.itemLine1StateImageView.visibility = View.VISIBLE
                            holder.mainPersonalizePageBinding.itemLine2StateImageView.visibility = View.GONE
                            holder.mainPersonalizePageBinding.itemLine1StateTextView.text = ""
                            holder.mainPersonalizePageBinding.itemLine2StateTextView.text = mContext.resources.getString(R.string.applied)
                        }
                        holder.mainPersonalizePageBinding.itemLine1Button.tag = "1_mainItem"
                        holder.mainPersonalizePageBinding.itemLine2Button.tag = "2_mainItem"
                        holder.mainPersonalizePageBinding.itemLine1Button.setOnClickListener {
                            mISelectThemeCallback?.onThemeSelected(GlobalDefine.COLORFUL_THEME)
                            item.setCurMode(GlobalDefine.COLORFUL_THEME)
                            notifyItemChanged(position)
                        }
                        holder.mainPersonalizePageBinding.itemLine2Button.setOnClickListener {
                            mISelectThemeCallback?.onThemeSelected(GlobalDefine.WIRE_THEME)
                            item.setCurMode(GlobalDefine.WIRE_THEME)
                            notifyItemChanged(position)
                        }
                        holder.mainPersonalizePageBinding.itemLine1Button.setOnHoverListener(object : View.OnHoverListener{
                            override fun onHover(v: View?, event: MotionEvent?): Boolean {
                                when (event?.action) {
                                    MotionEvent.ACTION_HOVER_ENTER -> {}
                                    MotionEvent.ACTION_HOVER_MOVE -> {}
                                    MotionEvent.ACTION_HOVER_EXIT -> {}
                                }
                                return false
                            }
                        })
                        holder.mainPersonalizePageBinding.itemLine2Button.setOnHoverListener(object : View.OnHoverListener{
                            override fun onHover(v: View?, event: MotionEvent?): Boolean {
                                when (event?.action) {
                                    MotionEvent.ACTION_HOVER_ENTER -> {}
                                    MotionEvent.ACTION_HOVER_MOVE -> {}
                                    MotionEvent.ACTION_HOVER_EXIT -> {}
                                }
                                return false
                            }
                        })
                    }

                    // 預設 或 自訂
                    2 -> {
                        holder.mainPersonalizePageBinding.itemLine1IconImageView.setImageResource(R.mipmap.radio_btn_selected)
                        holder.mainPersonalizePageBinding.itemLine2IconImageView.setImageResource(R.mipmap.radio_btn_default)
                        if (item.getIsDefault() == GlobalDefine.DEFAULT_MENU) {
                            holder.mainPersonalizePageBinding.itemLine1IconImageView.setImageResource(R.mipmap.radio_btn_selected)
                            holder.mainPersonalizePageBinding.itemLine2IconImageView.setImageResource(R.mipmap.radio_btn_default)
                            holder.mainPersonalizePageBinding.itemLine1StateImageView.visibility = View.GONE
                            holder.mainPersonalizePageBinding.itemLine2StateImageView.visibility = View.VISIBLE
                            holder.mainPersonalizePageBinding.itemLine1StateTextView.text = mContext.resources.getString(R.string.applied)
                            holder.mainPersonalizePageBinding.itemLine2StateTextView.text = ""
                        } else {
                            holder.mainPersonalizePageBinding.itemLine1IconImageView.setImageResource(R.mipmap.radio_btn_default)
                            holder.mainPersonalizePageBinding.itemLine2IconImageView.setImageResource(R.mipmap.radio_btn_selected)
                            holder.mainPersonalizePageBinding.itemLine1StateImageView.visibility = View.VISIBLE
                            holder.mainPersonalizePageBinding.itemLine2StateImageView.visibility = View.GONE
                            holder.mainPersonalizePageBinding.itemLine1StateTextView.text = ""
                            holder.mainPersonalizePageBinding.itemLine2StateTextView.text = mContext.resources.getString(R.string.applied)
                        }
                        holder.mainPersonalizePageBinding.itemLine1Button.tag = "3_mainItem"
                        holder.mainPersonalizePageBinding.itemLine2Button.tag = "4_mainItem"
                        holder.mainPersonalizePageBinding.itemLine1Button.setOnClickListener {
                            mISetMainMenuToDefaultOrCustom?.onMainMenuSet(GlobalDefine.DEFAULT_MENU)
                            item.setIsDefault(GlobalDefine.DEFAULT_MENU)
                            notifyItemChanged(position)
                        }
                        holder.mainPersonalizePageBinding.itemLine2Button.setOnClickListener {
                            mISetMainMenuToDefaultOrCustom?.onMainMenuSet(GlobalDefine.CUSTOM_MENU)
                            item.setIsDefault(GlobalDefine.CUSTOM_MENU)
                            notifyItemChanged(position)
                        }
                        holder.mainPersonalizePageBinding.itemLine1Button.setOnHoverListener(object : View.OnHoverListener{
                            override fun onHover(v: View?, event: MotionEvent?): Boolean {
                                when (event?.action) {
                                    MotionEvent.ACTION_HOVER_ENTER -> {}
                                    MotionEvent.ACTION_HOVER_MOVE -> {}
                                    MotionEvent.ACTION_HOVER_EXIT -> {}
                                }
                                return false
                            }
                        })
                        holder.mainPersonalizePageBinding.itemLine2Button.setOnHoverListener(object : View.OnHoverListener{
                            override fun onHover(v: View?, event: MotionEvent?): Boolean {
                                when (event?.action) {
                                    MotionEvent.ACTION_HOVER_ENTER -> {}
                                    MotionEvent.ACTION_HOVER_MOVE -> {}
                                    MotionEvent.ACTION_HOVER_EXIT -> {}
                                }
                                return false
                            }
                        })
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mGetAllPersonalizeSettingItems.getItemsList().size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setJsonDataToAdapter(aWeatherIsShow : Int, aTheme : Int, aIsDefault : Int) {
        if (mGetAllPersonalizeSettingItems.getItemsList().size >= 3) {
            for (item in mGetAllPersonalizeSettingItems.getItemsList()) {
                item.setWeatherIsShow(aWeatherIsShow)
                item.setCurMode(aTheme)
                item.setIsDefault(aIsDefault)
            }
        }
        notifyDataSetChanged()
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