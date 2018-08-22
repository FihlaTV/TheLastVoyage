package com.alter.thelastvoyage.support.os

import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.webkit.MimeTypeMap
import androidx.annotation.NonNull
import androidx.core.content.FileProvider
import androidx.loader.content.CursorLoader
import com.alter.thelastvoyage.support.util.LogHelper
import java.io.File

/**
 * Utility class for storage methods.
 *
 * @author Vitor Ribeiro
 */
object StorageUtils {

    /** Log Tag.  */
    private val TAG = StorageUtils::class.java.simpleName

    /** MIME types.  */
    const val MIME_TYPE_ALL = "*/*"
    const val MIME_TYPE_TEXT = "text/*"
    const val MIME_TYPE_AUDIO = "audio/*"
    const val MIME_TYPE_IMAGE = "image/*"
    const val MIME_TYPE_VIDEO = "video/*"
    const val MIME_TYPE_APP = "application/*"
    const val MIME_TYPE_BINARY = "application/octet-stream"

    /** Providers from uri.  */
    private const val EXTERNAL_STORAGE_PROVIDER = "com.android.externalstorage.documents"
    private const val DOWNLOADS_PROVIDER = "com.android.providers.downloads.documents"
    private const val MEDIA_PROVIDER = "com.android.providers.media.documents"
    private const val GOOGLE_PHOTOS_PROVIDER = "com.google.android.apps.photos.content"
    private const val GOOGLE_DRIVE_PROVIDER = "com.google.android.apps.docs.storage"

    /** Content. */
    private const val PUBLIC_DOWNLOADS = "content://downloads/public_downloads"

    /** Uri scheme. */
    private const val CONTENT_SCHEME = "content"
    private const val FILE_SCHEME = "file"

    /**
     * Get Uri from file.
     * @param context of the caller.
     * @param file to parse.
     * @return Uri.
     */
    fun getUriFromFile(context: Context, file: File): Uri? {
        return try {
            val authority = context.applicationContext.packageName + ".provider"
            FileProvider.getUriForFile(context, authority, file)
        } catch (throwable: Throwable) {
            LogHelper.e(TAG, "Unable to get Uri from file.", throwable)
            null
        }
    }

    /**
     * Get Uri from path.
     * @param context of the caller.
     * @param path to parse.
     * @return Uri.
     */
    fun getUriFromPath(context: Context, path: String): Uri? {
        return try {
            val file = File(Uri.decode(path))
            getUriFromFile(context, file)
        } catch (throwable: Throwable) {
            LogHelper.e(TAG, "Unable to get Uri from path.", throwable)
            null
        }
    }

    /**
     * Get file name.
     * @param path of the file.
     * @return file name or null if unknown.
     */
    fun getFileNameFromPath(path: String): String? {
        return try {
            val file = File(path)
            file.name
        } catch (t: Throwable) {
            LogHelper.e(TAG, "Unable to get file name from path.", t)
            null
        }
    }

    /**
     * Get file name.
     * @param context of the caller.
     * @param uri to parse.
     * @return file name or null if unknown.
     */
    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        try {
            if (isContentScheme(uri)) {
                val cursor = getCursor(context, uri, null, null, null)
                fileName = getCursorData(cursor, OpenableColumns.DISPLAY_NAME)
            }
        } catch (t: Throwable) {
            LogHelper.e(TAG, "Unable to get file name from cursor.", t)
        }

        if (fileName == null) {
            try {
                fileName = uri.path
                val cut = fileName!!.lastIndexOf('/')
                if (cut >= 0) {
                    fileName = fileName.substring(cut + 1)
                }
            } catch (t: Throwable) {
                LogHelper.e(TAG, "Unable to get file name from uri path.", t)
            }
        }

        return fileName
    }

    /**
     * Get Uri path.
     * @param context of the caller.
     * @param uri to parse.
     * @return uri path.
     */
    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (OSUtils.hasKitKat() && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val path = getExternalStorageProviderPathFromUri(uri)
                if (!TextUtils.isEmpty(path)) {
                    return path
                }
            }

            // DownloadsProvider
            if (isDownloadsDocument(uri)) {
                return getDownloadsProviderPathFromUri(context, uri)
            }

            // MediaProvider
            if (isMediaDocument(uri)) {
                return getMediaProviderPathFromUri(context, uri)
            }

            // Google Drive
            if (isGoogleDrive(uri)) {
                return getFileNameFromUri(context, uri)
            }
        }

        // MediaStore
        if (isContentScheme(uri)) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.lastPathSegment
            }
        }

        // File
        return if (isFileScheme(uri)) {
            uri.path
        } else getDefaultPathFromUri(context, uri)
    }

    /**
     * Get Uri path from content resolver.
     * @param context of the caller.
     * @param uri to parse.
     * @return uri path.
     */
    private fun getDefaultPathFromUri(context: Context, uri: Uri): String? {
        val column = "_data"
        val projection = arrayOf(column)
        val cursor = getCursor(context, uri, projection, null, null)
        val path = getCursorData(cursor, column)

        return if (TextUtils.isEmpty(path)) {
            getFileNameFromUri(context, uri)
        } else path
    }

    /**
     * Get Uri path from External Storage Provider.
     * @param uri to parse.
     * @return uri path.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun getExternalStorageProviderPathFromUri(uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        if (TextUtils.isEmpty(docId)) {
            return null
        }

        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (split.size < 2) {
            return null
        }

        val type = split[0]
        val path = split[1]

        return if ("primary".equals(type, ignoreCase = true)) {
            Environment.getExternalStorageDirectory().toString() + File.separator + path
        } else null

        // TODO: handle non-primary volumes
    }

    /**
     * Get Uri path from Downloads Provider.
     * @param context of the caller.
     * @param uri to parse.
     * @return uri path.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun getDownloadsProviderPathFromUri(context: Context, uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        if (TextUtils.isEmpty(docId)) {
            return null
        }

        val contentUri = ContentUris.withAppendedId(Uri.parse(PUBLIC_DOWNLOADS), java.lang.Long.valueOf(docId))
        return getDefaultPathFromUri(context, contentUri)
    }

    /**
     * Get Uri path from Media Provider.
     * @param context of the caller.
     * @param uri to parse.
     * @return uri path.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun getMediaProviderPathFromUri(@NonNull context: Context, @NonNull uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        if (TextUtils.isEmpty(docId)) {
            return null
        }

        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (split.size < 2) {
            return null
        }

        val type = split[0]
        val selectionArgs = arrayOf(split[1])

        val contentUri: Uri
        val column: String
        val selection: String
        when (type) {
            "image" -> {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                column = MediaStore.Images.Media.DATA
                selection = MediaStore.Images.Media._ID + "=?"
            }
            "video" -> {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                column = MediaStore.Video.Media.DATA
                selection = MediaStore.Video.Media._ID + "=?"
            }
            "audio" -> {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                column = MediaStore.Audio.Media.DATA
                selection = MediaStore.Audio.Media._ID + "=?"
            }
            else -> {
                contentUri = MediaStore.Files.getContentUri("external")
                column = "_data"
                selection = "_id=?"
            }
        }
        val projection = arrayOf(column)

        val cursor = getCursor(context, contentUri, projection, selection, selectionArgs)
        return getCursorData(cursor, column)
    }

    /**
     * Get content resolver cursor.
     * @param context of the caller.
     * @param uri to query
     * @param projection a list of which columns to return.
     * @param selection a filter declaring which rows to return, formatted as an
     * SQL WHERE clause (excluding the WHERE itself).
     * @param selectionArgs arguments to replace ?s in the order that they
     * appear in the selection. The values will be bound as Strings.
     * @return cursor object.
     */
    private fun getCursor(context: Context, uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?): Cursor? {
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        } catch (t: Throwable) {
            LogHelper.e(TAG, "Unable to get cursor.", t)
        }

        try {
            if (cursor == null) {
                val cursorLoader = CursorLoader(context, uri, projection, null, null, null)
                cursor = cursorLoader.loadInBackground()
            }
        } catch (t: Throwable) {
            LogHelper.e(TAG, "Unable to get cursor loader.", t)
        }

        return cursor
    }

    /**
     * Get cursor data.
     * @param cursor to use.
     * @param column to use.
     * @return cursor data.
     */
    private fun getCursorData(cursor: Cursor?, column: String): String? {
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } catch (t: Throwable) {
            LogHelper.e(TAG, "Unable to read cursor data.", t)
        } finally {
            cursor?.close()
        }

        return null
    }

    /**
     * Gets the extension of a file.
     * @param path of the file.
     * @return extension including the dot, or empty if there is no extension.
     */
    fun getExtension(path: String): String {
        val dot = path.lastIndexOf(".")
        return if (dot >= 0) {
            path.substring(dot)
        } else ""
    }

    /**
     * Get MIME type for the given path.
     * @param path of the file.
     * @return The MIME type for the given file.
     */
    fun getMimeType(path: String): String {
        val extension = getExtension(path)
        if (TextUtils.isEmpty(extension)) {
            return MIME_TYPE_BINARY
        }
        val extensionNoDot = extension.substring(1)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extensionNoDot) ?: MIME_TYPE_BINARY
    }

    /**
     * Check if the file extension corresponds to an image.
     * @param path to check.
     * @return true if it is an image, false otherwise.
     */
    fun isImage(path: String): Boolean {
        val extension = getExtension(path)
        if (TextUtils.isEmpty(extension)) {
            return false
        }

        when (extension) {
            ".jpeg", ".jpg", ".bmp", ".png", ".gif" -> return true
        }

        return false
    }

    /**
     * Check if the file extension corresponds to a video.
     * @param path to check.
     * @return true if it is a video, false otherwise.
     */
    fun isVideo(path: String): Boolean {
        val extension = getExtension(path)
        if (TextUtils.isEmpty(extension)) {
            return false
        }

        when (extension) {
            ".mp4", ".3gpp" -> return true
        }

        return false
    }

    /**
     * Checks whether the Uri authority is External Storage Provider.
     * @param uri to check.
     * @return true if it is, false otherwise.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return EXTERNAL_STORAGE_PROVIDER.equals(uri.authority, ignoreCase = true)
    }

    /**
     * Checks whether the Uri authority is Downloads Provider.
     * @param uri to check.
     * @return true if it is, false otherwise.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return DOWNLOADS_PROVIDER.equals(uri.authority, ignoreCase = true)
    }

    /**
     * Checks whether the Uri authority is Media Provider.
     * @param uri to check.
     * @return true if it is, false otherwise.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return MEDIA_PROVIDER.equals(uri.authority, ignoreCase = true)
    }

    /**
     * Checks whether the Uri authority is Google Photos.
     * @param uri to check.
     * @return true if it is, false otherwise.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return GOOGLE_PHOTOS_PROVIDER.equals(uri.authority, ignoreCase = true)
    }

    /**
     * Checks whether the Uri authority is Google Drive.
     * @param uri to check.
     * @return true if it is, false otherwise.
     */
    fun isGoogleDrive(uri: Uri): Boolean {
        return GOOGLE_DRIVE_PROVIDER.equals(uri.authority, ignoreCase = true)
    }

    /**
     * Checks whether the Uri scheme is Content.
     * @param uri to check.
     * @return true if it is, false otherwise.
     */
    fun isContentScheme(uri: Uri): Boolean {
        return CONTENT_SCHEME.equals(uri.scheme, ignoreCase = true)
    }

    /**
     * Checks whether the Uri scheme is File.
     * @param uri to check.
     * @return true if it is, false otherwise.
     */
    fun isFileScheme(uri: Uri): Boolean {
        return FILE_SCHEME.equals(uri.scheme, ignoreCase = true)
    }

    /**
     * Recursively delete file or directory.
     * @param file or directory to delete.
     * @return true if successful, false otherwise.
     */
    fun deleteRecursive(file: File?): Boolean {
        var deletedAll = true
        if (file != null) {
            if (file.isDirectory) {
                val children = file.list()
                for (aChildren in children) {
                    deletedAll = deleteRecursive(File(file, aChildren)) && deletedAll
                }
            } else {
                deletedAll = file.delete()
            }
        }
        return deletedAll
    }
}