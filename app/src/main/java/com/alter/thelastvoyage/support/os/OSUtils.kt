package com.alter.thelastvoyage.support.os

import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.PowerManager
import android.view.View
import android.view.inputmethod.InputMethodManager

import com.alter.thelastvoyage.support.util.LogHelper

import java.io.File

/**
 * Class containing some static utility methods for the OS,
 * like versions, keyboard, screen, etc.
 * Static final constants declared in later versions
 * of the OS are inlined at compile time.
 *
 * @author Vitor Ribeiro
 */
object OSUtils {

    /** Log Tag.  */
    private val TAG = OSUtils::class.java.simpleName

    /**
     * Get version name.
     * @param context of the caller.
     * @return version name.
     */
    fun getVersionName(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (t: Throwable) {
            context.getString(android.R.string.unknownName)
        }
    }

    /**
     * Get version code.
     * @param context of the caller.
     * @return version code.
     */
    @TargetApi(28)
    fun getVersionCode(context: Context): Long {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (hasPie()) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        } catch (t: Throwable) {
            0
        }
    }

    /**
     * Get Theme.
     * @param context of the caller.
     * @return theme resource.
     */
    fun getTheme(context: Context): Int {
        return try {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_META_DATA).applicationInfo.theme
        } catch (t: Throwable) {
            0
        }
    }

    /**
     * Check if the user-visible SDK version of the framework is equal or newer to API 16.
     *
     * @return true if API 16+, false otherwise.
     */
    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN
    }

    /**
     * Check if the user-visible SDK version of the framework is equal or newer to API 18.
     *
     * @return true if API 18+, false otherwise.
     */
    fun hasJellyBeanMR2(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2
    }

    /**
     * Check if the user-visible SDK version of the framework is equal or newer to API 19.
     *
     * @return true if API 19+, false otherwise.
     */
    fun hasKitKat(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT
    }

    /**
     * Check if the user-visible SDK version of the framework is equal or newer to API 21.
     *
     * @return true if API 21+, false otherwise.
     */
    fun hasLollipop(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP
    }

    /**
     * Check if the user-visible SDK version of the framework is equal or newer to API 23.
     *
     * @return true if API 23+, false otherwise.
     */
    fun hasMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.M
    }

    /**
     * Check if the user-visible SDK version of the framework is equal or newer to API 26.
     *
     * @return true if API 26+, false otherwise.
     */
    fun hasOreo(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.O
    }

    /**
     * Check if the user-visible SDK version of the framework is equal or newer to API 28.
     *
     * @return true if API 28+, false otherwise.
     */
    fun hasPie(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.P
    }

    /**
     * Erase application data from disk.
     * Equivalent to the user choosing to clear the app's data from within the device settings UI.
     * It erases all dynamic data associated with the app except OBB files.
     *
     * @param context of the caller.
     * @return true if wipe is successful, false otherwise.
     */
    fun wipeApplicationData(context: Context): Boolean {
        try {
            if (OSUtils.hasKitKat()) {
                val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                return actManager.clearApplicationUserData()
            }

            val cacheDirectory = context.cacheDir
            val applicationDirectory = File(cacheDirectory.parent)
            if (applicationDirectory.exists()) {
                val fileNames = applicationDirectory.list()
                for (fileName in fileNames) {
                    if (fileName != "lib") {
                        StorageUtils.deleteRecursive(File(applicationDirectory, fileName))
                    }
                }
            }
        } catch (t: Throwable) {
            return false
        }

        return true
    }

    /**
     * Check if the app is on the device's power whitelist.
     * @param context of the caller.
     * @return true if it is, false otherwise.
     */
    fun isAppWhiteListed(context: Context): Boolean {
        if (OSUtils.hasMarshmallow()) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            return powerManager.isIgnoringBatteryOptimizations(context.packageName)
        }

        return false
    }

    /**
     * Check if the device is in power save mode.
     * @param context of the caller.
     * @return true if it is, false otherwise.
     */
    fun isPowerSaveModeEnabled(context: Context): Boolean {
        if (OSUtils.hasLollipop()) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            return powerManager.isPowerSaveMode
        }

        return false
    }

    /**
     * Check if the device is dozing/idling.
     * @param context of the caller.
     * @return true if it is, false otherwise.
     */
    fun isDozing(context: Context): Boolean {
        if (OSUtils.hasMarshmallow()) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            return powerManager.isDeviceIdleMode && !isAppWhiteListed(context)
        }

        return false
    }

    /**
     * Hide keyboard.
     * @param context of the caller.
     * @param focus token view.
     * @return true if operation was successful, false otherwise.
     */
    fun hideKeyboard(context: Context, focus: View): Boolean {
        return try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(focus.windowToken, 0)
            imm.hideSoftInputFromWindow(focus.applicationWindowToken, 0)
            true
        } catch (t: Throwable) {
            LogHelper.e(TAG, "Unable to hide keyboard", t)
            false
        }
    }

    /**
     * Hide keyboard.
     * @param activity on display.
     * @return true if operation was successful, false otherwise.
     */
    fun hideKeyboard(activity: Activity): Boolean {
        val focus = activity.currentFocus
        return if (focus != null) {
            hideKeyboard(activity, focus)
        } else false
    }
}
