package com.example.cryptocurrencytradingsimulator.di

import android.content.Context
import android.content.SharedPreferences
import com.example.cryptocurrencytradingsimulator.BuildConfig
import com.example.cryptocurrencytradingsimulator.data.AppDatabase
import com.example.cryptocurrencytradingsimulator.data.Repository
import com.example.cryptocurrencytradingsimulator.data.api.ApiService
import com.example.cryptocurrencytradingsimulator.data.dao.FavoriteDao
import com.example.cryptocurrencytradingsimulator.data.dao.OwnedDao
import com.example.cryptocurrencytradingsimulator.data.dao.TransactionDao
import com.example.cryptocurrencytradingsimulator.utils.BASE_URL
import com.example.cryptocurrencytradingsimulator.utils.PREF_FILE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences{
        val sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        if(!sharedPreferences.contains("currency")){
            editor.putString("currency", "usd").apply()
        }
        if(!sharedPreferences.contains("money")){
            editor.putFloat("money", 10000.0F).apply()
        }
        return sharedPreferences
    }

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.apply { loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase): FavoriteDao {
        return appDatabase.favoriteDao()
    }

    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao{
        return appDatabase.transactionDao()
    }

    @Provides
    fun provideOwnedDao(appDatabase: AppDatabase): OwnedDao {
        return appDatabase.ownedDao()
    }

}