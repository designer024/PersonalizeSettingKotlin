package com.leapsy.personalizesettingkotlin.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.leapsy.personalizesettingkotlin.GlobalDefine
import com.leapsy.personalizesettingkotlin.R
import com.leapsy.personalizesettingkotlin.databinding.ActivityMainBinding
import com.leapsy.personalizesettingkotlin.jsonHelper.JsonFileWriteReadManager
import com.leapsy.personalizesettingkotlin.menuHelper.GetAllInstalledAppInfo
import com.leapsy.personalizesettingkotlin.menuHelper.SetDefaultMenuAppIcon
import com.leapsy.personalizesettingkotlin.permissionHelper.PermissionHelper
import java.io.File

open class BaseActivity : AppCompatActivity() {
    protected lateinit var activityMainBinding : ActivityMainBinding

    private val requestPermissionList = arrayOf (
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE )

    private var mJsonFolderPath : String = ""
    protected var jsonFilePath : String = ""
    protected lateinit var jsonFileWriteReadManager : JsonFileWriteReadManager

    protected lateinit var setDefaultMenuAppIcon : SetDefaultMenuAppIcon
    protected lateinit var getAllInstalledAppInfo : GetAllInstalledAppInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTheme(R.style.theme_color)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        mJsonFolderPath = "${GlobalDefine.EXTERNAL_STORAGE_PATH}${GlobalDefine.PERSONALIZED_SETTING_FOLDER}${File.separator}"
        jsonFilePath = "${GlobalDefine.EXTERNAL_STORAGE_PATH}${GlobalDefine.PERSONALIZED_SETTING_FOLDER}${File.separator}${GlobalDefine.PERSONALIZED_SETTING_JSON_FILE_NAME}"
        //android.util.Log.d("xxxxoox" ,"folder: ${mJsonFolderPath}\n, path: ${jsonFilePath}")
        jsonFileWriteReadManager = JsonFileWriteReadManager()

        getAllInstalledAppInfo = GetAllInstalledAppInfo(this@BaseActivity)

        // 取得主菜預設的app
        initDefaultApp()

        if (PermissionHelper.requestPermissions(this@BaseActivity, requestPermissionList, PermissionHelper.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)) {
            jsonFileWriteReadManager.createNewJsonFile(mJsonFolderPath, jsonFilePath)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermissionHelper.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                jsonFileWriteReadManager.createNewJsonFile(mJsonFolderPath, jsonFilePath)
                jsonFileWriteReadManager.readJsonFileData(jsonFilePath)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onStart() {
        super.onStart()

        // check MANAGE_EXTERNAL_STORAGE permission
        checkManagerExternalStoragePermission()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()

        //取得此裝置所有已安裝APP之名稱、Package Name 及版本號 等
        getAllInstalledAppInfo.getAllAppInstalled()

        if (PermissionHelper.requestPermissions(this@BaseActivity, requestPermissionList, PermissionHelper.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)) {
            jsonFileWriteReadManager.createNewJsonFile(mJsonFolderPath, jsonFilePath)
            jsonFileWriteReadManager.readJsonFileData(jsonFilePath)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    /**
     * check MANAGE_EXTERNAL_STORAGE permission
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkManagerExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {

                activityMainBinding.popForAskManageExternalStorageEnableLayout.visibility = View.VISIBLE
                activityMainBinding.goSetting2Button.setOnClickListener { startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)) }

                /*val builder = AlertDialog.Builder(this)
                    .setMessage("Need MANAGE_EXTERNAL_STORAGE permission")
                    .setPositiveButton("Confirm") {
                            _, _ -> startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
                    }
                    .show()*/
            } else {
                activityMainBinding.popForAskManageExternalStorageEnableLayout.visibility = View.GONE
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            hideSystemUI()
        }
    }
    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            // Enables regular immersive mode.
            // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
            // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    /**
     * 取得主菜預設的app
     */
    private fun initDefaultApp() {
        setDefaultMenuAppIcon = SetDefaultMenuAppIcon(this)
        setDefaultMenuAppIcon.setAllDefaultApp()
    }
}