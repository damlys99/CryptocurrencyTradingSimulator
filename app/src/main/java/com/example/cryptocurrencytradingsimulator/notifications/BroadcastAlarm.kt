package com.example.cryptocurrencytradingsimulator.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.api.Repository
import com.example.cryptocurrencytradingsimulator.data.models.Direction
import com.example.cryptocurrencytradingsimulator.utils.MyFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BroadcastAlarm : BroadcastReceiver() {
    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("EXCEPTION", exception.toString())
    }

    @Inject
    lateinit var repository: Repository

    private fun simpleNotification(context: Context) {
        GlobalScope.launch(handler) {
            val notificationsData = repository.getAllNotifications()
            val prices = repository.getPrices(notificationsData.map { it.id }.joinToString(","))

            val notifications = notificationsData.filter {
                (it.comp == Direction.GREATER_THAN && it.price <= prices[it.id]!!.usd) || (it.comp == Direction.LESSER_THAN && it.price >= prices[it.id]!!.usd)
            }
            if(!notifications.isEmpty()) {
                val content = notifications.map { "${it.id} ${if (it.comp == Direction.GREATER_THAN) ">=" else "<="} ${MyFormatter.currency(it.price)}" }.joinToString(
                    "\n"
                )

                val builder = NotificationCompat.Builder(context.applicationContext, "0")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("These Cryptocurrencies' prices reached wanted level!")
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.notify(0, builder.build())
            }
        }
    }


    override fun onReceive(context: Context, intent: Intent) {
        simpleNotification(context)
    }
}