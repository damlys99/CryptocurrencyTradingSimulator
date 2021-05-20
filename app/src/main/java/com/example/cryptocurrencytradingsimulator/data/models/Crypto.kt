package com.example.cryptocurrencytradingsimulator.data.models

data class Crypto(
    val id : String? = "",
    val symbol: String? = "",
    val name: String? = "",
    val image: String? = "",
    val current_price: Double? = 0.0,
    val price_change_percentage_24h_in_currency: Double = 0.0
)