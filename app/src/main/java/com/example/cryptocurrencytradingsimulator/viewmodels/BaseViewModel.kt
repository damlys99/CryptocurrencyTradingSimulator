package com.example.cryptocurrencytradingsimulator.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptocurrencytradingsimulator.ui.base.NavigEvent
import com.example.cryptocurrencytradingsimulator.ui.base.NavigEventInfo

open class BaseViewModel : ViewModel(){
    internal val _navigateToFragment = MutableLiveData<NavigEvent<NavigEventInfo>>()
    val navigateToFragment: LiveData<NavigEvent<NavigEventInfo>>
        get() = _navigateToFragment
}