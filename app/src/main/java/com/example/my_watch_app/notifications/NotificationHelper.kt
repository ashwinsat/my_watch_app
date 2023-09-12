package com.example.my_watch_app.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat

private const val CHANNEL_ID = "my_notification_channel"
private const val NOTIFICATION_ID = 1

open class NotificationHelper {
    @SuppressLint("ObsoleteSdkInt")
    fun showNotification(context: Context, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "My Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(androidx.wear.tiles.material.R.drawable.avatar)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    androidx.wear.tiles.material.R.drawable.avatar
                )
            )
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}