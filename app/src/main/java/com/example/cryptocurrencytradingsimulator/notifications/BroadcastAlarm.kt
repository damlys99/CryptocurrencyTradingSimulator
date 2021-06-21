package com.example.cryptocurrencytradingsimulator.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cryptocurrencytradingsimulator.R

class BroadcastAlarm: BroadcastReceiver() {

    private fun simpleNotification(context: Context) {

        val builder = NotificationCompat.Builder(context.applicationContext, "0")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Title")
            .setContentText("Content")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)
        // notificationId is a unique int for each notification that you must define

        notificationManager.notify(0, builder.build())
    }


    override fun onReceive(context: Context, intent: Intent) {
        simpleNotification(context)
    }
}