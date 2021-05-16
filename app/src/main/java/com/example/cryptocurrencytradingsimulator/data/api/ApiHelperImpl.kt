package com.example.cryptocurrencytradingsimulator.data.api

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.cryptocurrencytradingsimulator.MainApplication
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.data.models.CryptoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
):ApiHelper{
    override fun getAllCrypto(): MutableLiveData<List<Crypto>> {
        val data : MutableLiveData<List<Crypto>> = MutableLiveData()
        val requestCall = apiService.getAllCrypto()
        requestCall.enqueue(object : Callback<List<Crypto>> {
            override fun onResponse(call: Call<List<Crypto>>, response: Response<List<Crypto>>) {
                Log.d("Response", "onResponse: ${response.body()}")
                if (response.isSuccessful) {
                    data.postValue(response.body()!!)
                } else {
                    Toast.makeText(
                        MainApplication.applicationContext(),
                        "Something went wrong ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Crypto>>, t: Throwable) {
                Toast.makeText(
                    MainApplication.applicationContext(),
                    "Something went wrong, ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        return data
    }
}