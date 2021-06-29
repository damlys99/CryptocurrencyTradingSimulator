package com.example.cryptocurrencytradingsimulator.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.cryptocurrencytradingsimulator.data.models.Notification

@Dao
interface NotificationsDao: BaseDao<Notification> {

    @Query("SELECT * FROM Notification")
    fun getAllNotifications(): List<Notification>

    @Query("SELECT * FROM Notification where id = :cryptoId")
    fun getNotification(cryptoId: String): Notification
}