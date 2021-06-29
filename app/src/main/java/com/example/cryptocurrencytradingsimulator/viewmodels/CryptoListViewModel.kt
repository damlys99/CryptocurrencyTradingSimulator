package com.example.cryptocurrencytradingsimulator.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.api.Repository
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.data.models.Favorite
import com.example.cryptocurrencytradingsimulator.ui.base.NavigEvent
import com.example.cryptocurrencytradingsimulator.ui.base.NavigEventInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: Repository
    //private val repository: Repository
) : BaseViewModel() {
    val cryptos: MutableLiveData<List<Crypto>> = MutableLiveData(arrayListOf())
    val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("EXCEPTION", exception.toString())
    }

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(handler) {
            withContext(Dispatchers.IO) {
                val tempList = repository.getAllCrypto()
                val favorites = repository.getAllFavorites()
                tempList.forEach {
                    if (favorites.map { el -> el.id }.contains(it.id)) {
                        it.favorite = true
                    }
                }
                cryptos.postValue(tempList)
            }
        }
    }


    fun chooseCrypto(crypto: Crypto) {
        val navigInfo = NavigEventInfo(
            R.id.action_menu_crypto_list_to_cryptoTabsFragment,
            arrayListOf(Pair("cryptoId", crypto.id!!))
        )
        _navigateToFragment.value = NavigEvent(navigInfo)

    }

    fun addOrDeleteFavorite(crypto: Crypto, isChecked: Boolean){
        crypto.favorite = isChecked
        GlobalScope.launch{
            repository.addOrDeleteFavorite(isChecked, Favorite(id = crypto.id!!))
        }

    }

}
