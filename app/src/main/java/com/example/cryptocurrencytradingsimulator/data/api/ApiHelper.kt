package com.example.cryptocurrencytradingsimulator.data.api

import androidx.lifecycle.MutableLiveData
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import retrofit2.Call
import retrofit2.Response

interface ApiHelper {
    fun getAllCrypto(data: MutableLiveData<List<Crypto>>)
}