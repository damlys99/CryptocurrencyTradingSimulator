package com.example.cryptocurrencytradingsimulator.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.api.Repository
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.data.models.Owned
import com.example.cryptocurrencytradingsimulator.data.models.Price
import com.example.cryptocurrencytradingsimulator.data.models.Transaction
import com.example.cryptocurrencytradingsimulator.ui.base.NavigEvent
import com.example.cryptocurrencytradingsimulator.ui.base.NavigEventInfo
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPreferences: SharedPreferences):
    BaseViewModel() {

    val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("EXCEPTION", exception.toString())
    }

    val prices: MutableLiveData<Map<String, Price>> = MutableLiveData()
    val owned: MutableLiveData<List<Owned>> = MutableLiveData()
    val chartData: MutableLiveData<List<Transaction>> = MutableLiveData()
    var money: MutableLiveData<Float> = MutableLiveData(sharedPreferences.getFloat("money", 0F))

    init {
        getOwnedAndPrices()
        getChartData()
    }


    fun getOwnedAndPrices(){
        GlobalScope.async(handler) {
            val ownd = repository.getAllOwned()
            owned.postValue(ownd)
            prices.postValue(repository.getPrices(ownd.map { it.cryptoId }.joinToString(",")))
        }
    }

    fun chooseCrypto(owned: Owned) {
        Log.e("S", owned.cryptoId)
        val navigInfo = NavigEventInfo(
            R.id.action_menu_profile_to_cryptoTabsFragment,
            arrayListOf(Pair("cryptoId", owned.cryptoId))
        )
        _navigateToFragment.value = NavigEvent(navigInfo)

    }

    fun getChartData(){
        GlobalScope.async(handler) {
            chartData.postValue(repository.getAllTransactions())
        }
    }
}