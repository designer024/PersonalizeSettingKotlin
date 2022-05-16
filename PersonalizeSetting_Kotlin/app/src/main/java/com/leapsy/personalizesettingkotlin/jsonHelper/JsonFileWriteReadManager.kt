package com.leapsy.personalizesettingkotlin.jsonHelper

import android.text.TextUtils
import com.leapsy.personalizesettingkotlin.GlobalDefine
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder

class JsonFileWriteReadManager {

    private var mIGetJsonFileDataCallback: IGetJsonFileData? = null
    /**
     * set callback
     * @param aIGetJsonFileData
     */
    fun setGetJsonDataCallback(aIGetJsonFileData: IGetJsonFileData?) {
        mIGetJsonFileDataCallback = aIGetJsonFileData
    }

    @Synchronized
    fun createNewJsonFile(aJsonFolderPath : String, aJsonFilePath : String) {
        val jsonFolder = File(aJsonFolderPath)
        if (!jsonFolder.exists()) {
            jsonFolder.mkdir()
            android.util.Log.d("@@@readJsonFileData", "${aJsonFolderPath} 已建立 !!")
        } else {
            android.util.Log.d("@@@readJsonFileData", "${aJsonFolderPath} 已存在")
        }

        // init json file content
        val initJsonData : String = GlobalDefine.PERSONAL_SETTING_INIT_JSON_CONTENT

        val jsonFile = File(aJsonFilePath)
        var fileWriter: FileWriter? = null
        var bufferedWriter: BufferedWriter? = null

        if (!jsonFile.exists()) {
            try {
                jsonFile.createNewFile()
                fileWriter = FileWriter(jsonFile.absoluteFile)
                bufferedWriter = BufferedWriter(fileWriter)
                bufferedWriter.write(initJsonData)
                bufferedWriter.close()
            } catch (e: IOException) {
                e.printStackTrace()
                android.util.Log.d("@@@readJsonFileData", "error: " + e.message)
            }
        } else {
            android.util.Log.d("@@@readJsonFileData", "json already exist! ")
        }
    }

    /**
     * 讀取Json 檔案內容
     * @param aJsonFilePath Json 檔案路徑
     */
    fun readJsonFileData(aJsonFilePath : String) {
        val jsonFile = File(aJsonFilePath)
        val stringBuilder = StringBuilder()
        var fileReader : FileReader? = null
        var bufferedReader : BufferedReader? = null

        if (jsonFile.exists()) {
            try {
                fileReader = FileReader(jsonFile.absolutePath)
                bufferedReader = BufferedReader(fileReader)

                var line: String? = ""

                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }

                mJsonFileContent = stringBuilder.toString()
                android.util.Log.d("@@@readJsonFileData", "json file 內容是: ${mJsonFileContent}")
                bufferedReader.close()

                if (!TextUtils.isEmpty(mJsonFileContent)) {
                    val personalizeSettingJsonData = SortDataType.returnType(mJsonFileContent, PersonalizeSettingJsonData::class.java)
                    android.util.Log.d("@@@readJsonFileData", "預設: ${personalizeSettingJsonData?.getIsDefault()}, 天氣: ${personalizeSettingJsonData?.getWeatherInfo()}, 今天天氣ID: ${personalizeSettingJsonData?.getTodayWeatherId()}, data size: ${personalizeSettingJsonData?.getCustomData()?.size}")
                    if (personalizeSettingJsonData != null) {
                        mJsonCustomPersonalizedSettingData = personalizeSettingJsonData.getCustomData()!!
                    }

                    mIGetJsonFileDataCallback?.onGetJsonData(personalizeSettingJsonData?.getWeatherInfo(), personalizeSettingJsonData?.getColorMode(), personalizeSettingJsonData?.getIsDefault(), mJsonCustomPersonalizedSettingData)
                }

            } catch (e : IOException) {
                e.printStackTrace()
                android.util.Log.d("@@@readJsonFileData", "read json IOException: ${e.message}")
            }
        } else {
            android.util.Log.d("@@@readJsonFileData", "無此檔案!!!")
        }
    }

    companion object {
        /**
         * Json file 內容
         */
        private var mJsonFileContent : String = ""
        /**
         * Json file 內容
         */
        fun getJsonFileContent() : String { return mJsonFileContent }

        private var mJsonCustomPersonalizedSettingData : MutableList<CustomPersonalizedSettingData> = ArrayList()
        fun getJsonCustomPersonalizedSettingData() : MutableList<CustomPersonalizedSettingData> { return mJsonCustomPersonalizedSettingData }
        fun setJsonCustomPersonalizedSettingData(aJsonCustomPersonalizedSettingData : MutableList<CustomPersonalizedSettingData>) { mJsonCustomPersonalizedSettingData = aJsonCustomPersonalizedSettingData }

        /**
         * 更新json data
         * @param aJsonFilePath json file path
         * @param aJsonData json data string
         * @param aWeatherSwitchKey weather key
         * @param aThemeKey theme Key
         * @param aIsDefaultKey is default Key
         * @param aDataListKey data list Key
         */
        @Synchronized
        fun updateJsonDataToFile(aJsonFilePath : String, aJsonData : String, aWeatherSwitchKey : String, aWeatherValue : Int?, aThemeKey : String, aThemeValue : Int?, aIsDefaultKey : String, aIsDefaultValue : Int?, aDataListKey : String, aListValue : List<CustomPersonalizedSettingData>) {
            val jsonFile = File(aJsonFilePath)
            if (jsonFile.exists()) {
                if (!TextUtils.isEmpty(aJsonData)) {
                    try {
                        val dataDetail : JSONObject = JSONObject(aJsonData)
                        val isWeatherKeyExisting : Boolean = dataDetail.has(aWeatherSwitchKey)
                        val isThemeKeyExisting : Boolean = dataDetail.has(aThemeKey)
                        val isDefaultKeyExisting : Boolean = dataDetail.has(aIsDefaultKey)
                        val isDataListKeyExisting : Boolean = dataDetail.has(aDataListKey)

                        if (isWeatherKeyExisting && isThemeKeyExisting && isDefaultKeyExisting && isDataListKeyExisting) {
                            dataDetail.put(aWeatherSwitchKey, aWeatherValue)
                            dataDetail.put(aThemeKey, aThemeValue)
                            dataDetail.put(aIsDefaultKey, aIsDefaultValue)

                            val existList : JSONArray = dataDetail.getJSONArray(aDataListKey)
                            // 先清除list
                            while (existList.length() > 0) {
                                existList.remove(0)
                            }

                            if (aListValue != null && aListValue.isNotEmpty()) {
                                for (i in 0 until aListValue.size) {
                                    val newObject : JSONObject = JSONObject()
                                    newObject.put(GlobalDefine.APP_NAME_KEY, aListValue[i].getAppName())
                                    newObject.put(GlobalDefine.PACKAGE_NAME_KEY, aListValue[i].getPackageName())
                                    newObject.put(GlobalDefine.APP_ICON_KEY, aListValue[i].getAppOrder())
                                    existList.put(newObject)
                                }
                                android.util.Log.d("@@@updateJsonDataToFile", "共有: ${aListValue.size} 筆")
                            }

                            val fileWriter : FileWriter = FileWriter(jsonFile.absoluteFile)
                            val bufferWriter : BufferedWriter = BufferedWriter(fileWriter)
                            bufferWriter.write(dataDetail.toString())
                            bufferWriter.close()
                            android.util.Log.d("@@@updateJsonDataToFile", "json data 已更新")
                        } else {
                            android.util.Log.d("@@@updateJsonDataToFile", "there is invalid json keys")
                        }
                    } catch (e : IOException) {
                        e.printStackTrace()
                        android.util.Log.d("@@@updateJsonDataToFile", "put to json IOException: ${e.message}")
                    } catch (e : JSONException) {
                        e.printStackTrace()
                        android.util.Log.d("@@@updateJsonDataToFile", "put to json JSONException: ${e.message}")
                    }
                }
            } else {
                android.util.Log.d("@@@updateJsonDataToFile", "無此檔案! ${aJsonFilePath}")
            }
        }
    }
}