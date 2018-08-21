package com.alter.thelastvoyage

import com.alter.thelastvoyage.support.os.OSUtils

import androidx.multidex.MultiDexApplication

/**
 * Application base class. It contains the variables, constants and methods used across the application.
 * When the application runs, the first thing it executes is this base class that extends
 * the Android Application super class whose function is to maintain a global application state.
 * Static variables that need to be shared all over the application are here as well as system information,
 * like Android version, and various methods called by the system, like when the overall system is running low on memory.
 *
 * @see android.app.Application
 *
 *
 * @author Vitor Ribeiro
 */
class TheLastVoyage : MultiDexApplication() {

    companion object {

        /** Application instance.  */
        lateinit var instance: TheLastVoyage
            private set

        /** Version name.  */
        lateinit var versionName: String
            private set

        /** Version code.  */
        var version: Int = 0
            private set

        /**
         * Check if app is in debug mode.
         * @return true if it is, false otherwise.
         */
        val isDebug: Boolean
            get() {
                return when (BuildConfig.BUILD_TYPE) {
                    "release" -> false
                    else -> true
                }
            }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Get current version
        version = OSUtils.getVersionCode(this)
        versionName = OSUtils.getVersionName(this)
    }

    /**
     * Called when the overall system is running low on memory,
     * and actively running processes should trim their memory usage.
     */
    override fun onLowMemory() {
        super.onLowMemory()
        System.gc()
    }

}
