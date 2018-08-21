package com.alter.thelastvoyage.support.util

import android.text.TextUtils
import com.alter.thelastvoyage.TheLastVoyage
import com.alter.thelastvoyage.notification.TokenUtils
import com.crashlytics.android.Crashlytics

/**
 * Utility class for errors.
 *
 * @author Vitor Ribeiro
 */
object CrashUtils {

    /** Log Tag.  */
    private val TAG = CrashUtils::class.java.simpleName

    /**
     * Setup Crashlytics identifiers.
     */
    fun setupCrashlytics() {
        TokenUtils.getToken(object : TokenUtils.TokenCallback {
            override fun onSuccess(token: String) {
                if (!TextUtils.isEmpty(token)) {
                    Crashlytics.setUserIdentifier(token)
                }
            }

            override fun onFailure() {
                // Ignore
            }
        })

        val userName = "name"
        if (!TextUtils.isEmpty(userName)) {
            Crashlytics.setUserName(userName)
        }

        val userEmail = "email"
        if (!TextUtils.isEmpty(userEmail)) {
            Crashlytics.setUserEmail(userEmail)
        }
    }

    /**
     * Save log and report to Crashlytics.
     * @param priority of the log.
     * @param tag of the log.
     * @param message of the log.
     * @param throwable of the error.
     */
    fun log(priority: Int, tag: String, message: String, throwable: Throwable?) {
        if (TheLastVoyage.isDebug) {
            return
        }

        Crashlytics.log(priority, tag, message)

        if (throwable != null) {
            Crashlytics.logException(throwable)
        }
    }
}
