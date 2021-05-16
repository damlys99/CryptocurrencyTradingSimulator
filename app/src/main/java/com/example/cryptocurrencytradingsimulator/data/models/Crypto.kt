package com.example.cryptocurrencytradingsimulator.data.models

data class CryptoResponse(
    val body: List<Crypto>? = null,
    val status: String? = ""
)
data class Crypto(
    val id : String? = "",
    val symbol: String? = "",
    val name: String? = "",
    val image: String? = "",
    val current_price: String? = "",
    val price_change_percentage_24h_in_currency: Double = 0.0
)