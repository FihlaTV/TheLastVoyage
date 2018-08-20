package com.alter.thelastvoyage

import android.app.Application

import java.util.Locale

/**
 * When the application runs, the first thing it executes is this base class that extends
 * the Android Application super class whose function is to maintain a global application state.
 * Static variables that need to be shared all over the application are here as well as system information,
 * and various methods called by the system, like when the device configuration changes,
 * or when the overall system is running low on memory.
 *
 * @see android.app.Application
 *
 * @author Vitor Ribeiro
 */
class TheLastVoyage : Application() {

    companion object {

        /** Log tag.  */
        private val TAG = TheLastVoyage::class.java.simpleName

        /** Default Locale  */
        val LOCALE = Locale.ENGLISH

        /** Version code number.  */
        var build = 1
        /** Version build name.  */
        var buildName = "v1.0"
    }

    override fun onCreate() {
        super.onCreate()

    }

    /**
     * This is called when the overall system is running low on memory,
     * and would like the actively running process to try to tighten their belt.
     *
     * @see android.app.Application.onLowMemory
     */
    override fun onLowMemory() {
        System.gc()
        super.onLowMemory()
    }
}
