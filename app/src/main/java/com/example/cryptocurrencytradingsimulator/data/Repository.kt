package com.example.cryptocurrencytradingsimulator.data
import androidx.lifecycle.MutableLiveData
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.data.models.CryptoChartData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository{
  fun getFavorites():List<String>{
      return arrayListOf("bitcoin", "ethereum")
  }
}