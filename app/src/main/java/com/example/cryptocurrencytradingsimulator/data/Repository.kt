package com.example.cryptocurrencytradingsimulator.data
import javax.inject.Singleton

@Singleton
class Repository{
  fun getFavorites():List<String>{
      return arrayListOf("bitcoin", "ethereum")
  }
}