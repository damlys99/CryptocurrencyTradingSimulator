package com.example.cryptocurrencytradingsimulator.viewmodels

import com.example.cryptocurrencytradingsimulator.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: Repository
): BaseViewModel() {
    val cryptos = repository.getAllCrypto()


}
