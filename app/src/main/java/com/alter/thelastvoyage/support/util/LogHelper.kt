package com.alter.thelastvoyage.support.util

import android.util.Log
import com.alter.thelastvoyage.TheLastVoyage

/**
 * Utility class for Logs.
 *
 * @author Vitor Ribeiro
 */
object LogHelper {

    /** Log Tag.  */
    private val TAG = LogHelper::class.java.simpleName

    /**
     * Show log.
     * @param priority of the log.
     * @param tag of the log.
     * @param message of the log.
     * @param throwable of the log.
     */
    private fun log(priority: Int, tag: String, message: String, throwable: Throwable?) {
        CrashUtils.log(priority, tag, message, throwable)

        when (priority) {
            Log.VERBOSE -> Log.v(tag, message)
            Log.DEBUG -> Log.d(tag, message)
            Log.INFO -> Log.i(tag, message)
            Log.WARN -> Log.w(tag, message)
            Log.ERROR -> Log.e(tag, message)
            else -> Log.wtf(tag, message)
        }
    }

    /**
     * Show verbose log.
     * @param tag of the log.
     * @param message of the log.
     */
    fun v(tag: String, message: String) {
        log(Log.VERBOSE, tag, message, null)
    }

    /**
     * Show debug log.
     * @param tag of the log.
     * @param message of the log.
     */
    fun d(tag: String, message: String) {
        log(Log.DEBUG, tag, message, null)
    }

    /**
     * Show information log.
     * @param tag of the log.
     * @param message of the log.
     */
    fun i(tag: String, message: String) {
        log(Log.INFO, tag, message, null)
    }

    /**
     * Show warning log.
     * @param tag of the log.
     * @param message of the log.
     */
    fun w(tag: String, message: String) {
        log(Log.WARN, tag, message, null)
    }

    /**
     * Show error log.
     * @param tag of the log.
     * @param message of the log.
     * @param throwable of the error.
     */
    @JvmOverloads
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        log(Log.ERROR, tag, message, throwable)
    }

    /**
     * Log for debugging purposes.
     * @param message of the log.
     */
    fun flag(message: String) {
        flag(TAG, message)
    }

    /**
     * Log for debugging purposes.
     * @param tag of the log.
     * @param message of the log.
     */
    fun flag(tag: String, message: String) {
        if (!TheLastVoyage.isDebug) {
            return
        }

        Log.e(tag, message)
    }
}