package com.jop.jetpack.firebase.utils

import android.app.NotificationChannel
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
import com.jop.jetpack.firebase.data.DataNotification
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageService: FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if(message.data.isNotEmpty()) {
            val jsonString= Json.encodeToString(message.data)
            val data = Json.decodeFromString<DataNotification>(jsonString)

            showNotification(data)
        }
    }

    private fun showNotification(data: DataNotification) {
        val notificationId: Int = SimpleDateFormat("ddhhmmss",  Locale("id", "ID")).format(Date()).toInt()

        try {
            val intent = MainActivity.newInstance(applicationContext, data.targetValue, data.targetData)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }

            val pIntent = PendingIntent.getActivity(
                applicationContext,
                System.currentTimeMillis().toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationIcon: Int = R.mipmap.ic_launcher
            val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            notificationBuilder = NotificationCompat.Builder(this, "Testing Notification")
                .setSmallIcon(notificationIcon)
                .setContentTitle(data.title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(data.body))
                .setSound(notificationSound)
                .setContentIntent(pIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel("Testing Notification", "General", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(notificationId, notificationBuilder.build())
        } catch (e: Exception){
            Log.e("TEST ERROR", e.message.toString())
        }
    }
}