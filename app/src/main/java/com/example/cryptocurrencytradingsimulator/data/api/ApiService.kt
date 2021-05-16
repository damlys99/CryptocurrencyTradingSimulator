package com.example.cryptocurrencytradingsimulator.data.api

import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page=1&sparkline=false&price_change_percentage=24h")
    fun getAllCrypto() : Call<List<Crypto>>


}
