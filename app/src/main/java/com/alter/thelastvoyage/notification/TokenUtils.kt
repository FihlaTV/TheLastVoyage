package com.alter.thelastvoyage.notification

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

object TokenUtils {

    interface TokenCallback {
        fun onSuccess(token: String)
        fun onFailure()
    }

    /**
     * Get Firebase token.
     * @param callback to execute.
     */
    fun getToken(callback: TokenCallback?) {
        val firebaseInstanceId = FirebaseInstanceId.getInstance()
        firebaseInstanceId.instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val newToken = task.result.token
                callback?.onSuccess(newToken)
                return@OnCompleteListener
            } else {
                callback?.onFailure()
                return@OnCompleteListener
            }
        })
    }
}
