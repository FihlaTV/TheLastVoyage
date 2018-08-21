package com.alter.thelastvoyage.support.content

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import com.alter.thelastvoyage.support.util.LogHelper

/**
 * Utility class with intent methods.
 *
 * @author Vitor Ribeiro
 */
object IntentUtils {

    /** Log Tag.  */
    private val TAG = IntentUtils::class.java.simpleName

    /**
     * Check intent availability.
     * @param context of the caller.
     * @param intent to check.
     * @return true if intent is available, false otherwise.
     */
    fun isAvailable(context: Context, intent: Intent): Boolean {
        return try {
            val packageManager = context.packageManager
            val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            list.size > 0
        } catch (t: Throwable) {
            false
        }
    }

    /**
     * Check if a component exists that can handle this intent.
     * @param context of the caller.
     * @param intent to start
     * @return true if intent was started, false otherwise.
     */
    fun resolveIntent(context: Context, intent: Intent): Boolean {
        return try {
            intent.resolveActivity(context.packageManager) != null
        } catch (t: Throwable) {
            LogHelper.w(TAG, "Unable to resolve intent.")
            false
        }
    }

    /**
     * Authorize intent.
     * @param context of the caller.
     * @param intent to authorize.
     * @param uri for the file.
     * @return intent or null if an error occurs.
     */
    fun authorizeIntent(context: Context, intent: Intent, uri: Uri): Intent? {
        return try {
            val resolvedIntentActivities = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolvedIntentInfo in resolvedIntentActivities) {
                val packageName = resolvedIntentInfo.activityInfo.packageName
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            intent
        } catch (t: Throwable) {
            val uriString = uri.toString()
            LogHelper.e(TAG, "Unable to authorize intent: $uriString", t)
            null
        }
    }

    /**
     * Get action intent.
     * @param uri link.
     * @return intent.
     */
    fun getActionIntent(uri: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    }

    /**
     * Get content intent.
     * @param type MIME date type.
     * @return content intent.
     */
    fun getContentIntent(type: String): Intent {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = type
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return intent
    }

    /**
     * Get image capture intent.
     * @param context of the caller.
     * @param photoURI for the photo file.
     * @return intent or null if an error occurs.
     */
    fun getImageCaptureIntent(context: Context, photoURI: Uri): Intent? {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        return authorizeIntent(context, intent, photoURI)
    }

    /**
     * Get video capture intent.
     * @param context of the caller.
     * @param videoURI for the video file.
     * @return intent or null if an error occurs.
     */
    fun getVideoCaptureIntent(context: Context, videoURI: Uri): Intent? {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        return authorizeIntent(context, intent, videoURI)
    }
}
