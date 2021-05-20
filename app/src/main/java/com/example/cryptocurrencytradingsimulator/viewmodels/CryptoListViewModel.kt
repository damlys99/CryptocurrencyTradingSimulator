package com.example.cryptocurrencytradingsimulator.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.cryptocurrencytradingsimulator.MainApplication
import com.example.cryptocurrencytradingsimulator.data.Repository
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: Repository,
): BaseViewModel() {
    val cryptos: MutableLiveData<List<Crypto>> = MutableLiveData(arrayListOf())
    init{
        loadData()
    }

    fun loadData(){
            repository.getAllCrypto(cryptos)
    }


}
