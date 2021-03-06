package com.example.cryptocurrencytradingsimulator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cryptocurrencytradingsimulator.data.dao.FavoriteDao
import com.example.cryptocurrencytradingsimulator.data.dao.NotificationsDao
import com.example.cryptocurrencytradingsimulator.data.dao.OwnedDao
import com.example.cryptocurrencytradingsimulator.data.dao.TransactionDao
import com.example.cryptocurrencytradingsimulator.data.models.Favorite
import com.example.cryptocurrencytradingsimulator.data.models.Notification
import com.example.cryptocurrencytradingsimulator.data.models.Owned
import com.example.cryptocurrencytradingsimulator.data.models.Transaction
import com.example.cryptocurrencytradingsimulator.utils.DATABASE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(
    entities = [Favorite::class, Transaction::class, Owned::class, Notification::class], version = 9, exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun transactionDao(): TransactionDao
    abstract  fun ownedDao(): OwnedDao
    abstract fun notificationsDao(): NotificationsDao
    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            GlobalScope.launch(Dispatchers.IO) { rePopulateDb(instance) }
                        }
                    }
                )
                .fallbackToDestructiveMigration()
                .build()
        }

        suspend fun rePopulateDb(instance: AppDatabase?) {
            instance?.let { db ->
                withContext(Dispatchers.IO) {
                    val todoDao: FavoriteDao = db.favoriteDao()
                    val transactionDao: TransactionDao = db.transactionDao()
                    val ownedDao: OwnedDao = db.ownedDao()
                    val notificationsDao: NotificationsDao = db.notificationsDao()
                }
            }
        }
    }
}
