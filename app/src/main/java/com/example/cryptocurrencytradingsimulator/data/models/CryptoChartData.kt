package com.example.cryptocurrencytradingsimulator.data.models

data class CryptoChartData(
    val prices: List<List<Double>>? = arrayListOf(),
    val market_caps: List<List<Double>>? = arrayListOf(),
    val total_volumes: List<List<Double>>? = arrayListOf()
)