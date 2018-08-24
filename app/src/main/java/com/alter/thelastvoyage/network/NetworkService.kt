package com.alter.thelastvoyage.network

import com.alter.thelastvoyage.support.util.LogHelper
import io.fabric.sdk.android.services.network.NetworkUtils
import java.net.HttpURLConnection
import java.net.URL

object NetworkService {

    /** Log tag.  */
    private val TAG = NetworkUtils::class.java.simpleName

    /**
     * Checks if there is an active Internet connection.
     *
     * @return true if there is Internet, false otherwise.
     */
    fun isInternetConnected(timeout: Int): Boolean {
        var httpUrlConnection: HttpURLConnection? = null
        try {

            val url = URL("http://google.com/generate_204")
            httpUrlConnection = url.openConnection() as HttpURLConnection
            httpUrlConnection.instanceFollowRedirects = false
            httpUrlConnection.connectTimeout = timeout
            httpUrlConnection.readTimeout = timeout
            httpUrlConnection.useCaches = false
            httpUrlConnection.readTimeout = timeout

            httpUrlConnection.connect()

            return httpUrlConnection.responseCode == HttpURLConnection.HTTP_NO_CONTENT
        } catch (e: Exception) {
            LogHelper.w(TAG, "Unable to check connection.")
        } finally {
            httpUrlConnection?.disconnect()
        }

        return false
    }
}