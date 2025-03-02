package com.jop.jetpack.firebase.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jop.jetpack.firebase.MainActivity
import com.jop.jetpack.firebase.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageService: FirebaseMessagingService() {
    private lateinit var intentNotification: Intent
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        showNotification(this, message)
    }

    private fun showNotification(context: Context, notification: RemoteMessage) {
        val notificationId: Int = SimpleDateFormat("ddhhmmss",  Locale("id", "ID")).format(Date()).toInt()
        val data = notification.data

        intentNotification = MainActivity.newInstance(
            context, targetValue = data["target_value"].toString()
        )

        intentNotification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        try {
            val notificationIcon: Int = R.mipmap.ic_launcher
            val resultPendingIntent = PendingIntent.getActivity(context, 1001, intentNotification, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            notificationBuilder = NotificationCompat.Builder(context, "Testing Notification")
                .setSmallIcon(notificationIcon)
                .setContentTitle(notification.notification!!.title)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notification.notification!!.body))

            notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH

            notificationBuilder.setContentIntent(resultPendingIntent)

            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Since android Oreo notification channel is needed.
//            val importance = if(isHigh) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(
//                channelId,
//                if (isHigh) context.resources.getString(R.string.txt_trans) else "General",
//                importance
//            )
//            notificationManager.createNotificationChannel("channel")
            notificationManager.notify(notificationId, notificationBuilder.build())
        } catch (e: Exception){
            Log.e("TEST ERROR", e.message.toString())
        }
    }
}