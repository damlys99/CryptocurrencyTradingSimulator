package com.example.cryptocurrencytradingsimulator.data

import androidx.lifecycle.MutableLiveData
import com.example.cryptocurrencytradingsimulator.data.api.ApiHelper
import com.example.cryptocurrencytradingsimulator.data.api.ApiService
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    fun getAllCrypto(data: MutableLiveData<List<Crypto>>) = apiHelper.getAllCrypto(data)
}