package com.alter.thelastvoyage.notification

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


/**
 * Class for receiving messages from Firebase Cloud Messaging.
 *
 * @author Vitor Ribeiro
 */
class MessagingService : FirebaseMessagingService() {

    companion object {
        /** Log Tag.  */
        private val TAG = MessagingService::class.java.simpleName
    }

    /**
     * Called when a new token for the default Firebase project is generated.
     * @param token used for sending messages to this application instance.
     * This token is the same as the one retrieved by getInstanceId().
     */
    @WorkerThread
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
    }

    /**
     * Called when message is received.
     *
     * @param message received from Firebase Cloud Messaging.
     */
    @WorkerThread
    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        if (message == null) {
            Log.e(TAG, "Invalid remote message.")
            return
        }

        Log.e(TAG, "From: " + message.from + " - At: " + message.sentTime)
        //
        //        RemoteMessage.Notification notification = message.getNotification();
        //        if (notification != null) {
        //            String noteBody = notification.getBody();
        //            if (!TextUtils.isEmpty(noteBody)) {
        //                AnalyticsService.sendNotification();
        //                NotificationService.showDefaultPushNotification(this,
        //                        notification.getTitle(),
        //                        noteBody,
        //                        message.getSentTime());
        //                LogHelper.i(TAG, "Default push shown.");
        //                return;
        //            }
        //        }
        //
        //        Map<String, String> payload = message.getData();
        //        if (payload == null) {
        //            LogHelper.w(TAG, "Message Payload is empty.");
        //            return;
        //        }
        //
        //
        //        if (!NotificationService.isAvailable(this)) {
        //            LogHelper.e(TAG, "Notification Service is unavailable.");
        //            return;
        //        }

    }

    /**
     * Called when the FCM server deletes pending messages.
     */
    @WorkerThread
    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    /**
     * Called when an upstream message has been successfully sent to the FCM connection server.
     * @param msgId id of the upstream message sent using send(RemoteMessage).
     */
    @WorkerThread
    override fun onMessageSent(msgId: String?) {
        super.onMessageSent(msgId)
    }

    /**
     * Called when there was an error sending an upstream message.
     * @param msgId id of the upstream message sent using send(RemoteMessage).
     * @param e description of the error, typically a SendException.
     */
    @WorkerThread
    override fun onSendError(msgId: String?, e: Exception?) {
        super.onSendError(msgId, e)
    }
}
