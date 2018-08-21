package com.alter.thelastvoyage.support.lang

import android.util.Base64

/**
 * Utility class with a bundle of parsing methods.
 *
 * @author Vitor Ribeiro
 */
object ParsingUtils {

    /** Convert String to Base64.
     * @param base string to convert.
     * @return corresponding Base64 string or null if not a valid string.
     */
    fun stringToBase64(base: String): String? {
        return try {
            Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP)
        } catch (t: Throwable) {
            null
        }
    }

    /** Convert Base64 to string.
     * @param base Base64 string to convert.
     * @return corresponding string or null if not a valid string.
     */
    fun base64ToString(base: String): String? {
        return try {
            String(Base64.decode(base, Base64.NO_WRAP))
        } catch (t: Throwable) {
            null
        }
    }

    /**
     * Caps first letter of a string.
     * @param data to caps.
     * @return modified string.
     */
    fun capsFirstLetter(data: String): String {
        val firstLetter = data.substring(0, 1).toUpperCase()
        return if (data.length > 1) {
            firstLetter + data.substring(1).toLowerCase()
        } else firstLetter
    }
}
