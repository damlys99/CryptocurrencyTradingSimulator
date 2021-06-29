package com.example.cryptocurrencytradingsimulator.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class NotificationService {

    companion object {
        fun setAlarm(context: Context) {
            createNotificationChannel(context)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val intent = Intent(context, BroadcastAlarm::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            // Hopefully your alarm will have a lower frequency than this!
            alarmManager?.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                AlarmManager.INTERVAL_HALF_HOUR / 30,
                pendingIntent
            )
        }
        private fun createNotificationChannel(context: Context) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("0", "channel_name", importance)
                channel.description = "channel desc"

                val a: NotificationManager? = null
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

}