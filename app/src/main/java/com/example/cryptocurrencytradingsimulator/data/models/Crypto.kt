package com.example.cryptocurrencytradingsimulator.data.models

import java.time.LocalDateTime

data class CryptoListItem(
    val id : String? = "",
    val symbol: String? = "",
    val name: String? = "",
    val image: String? = "",
    val current_price: Double? = 0.0,
    val price_change_percentage_24h_in_currency: Double = 0.0,
    val market_cap: Double? = 0.0,
    val total_volume: Double? = 0.0,
    val ath: Double? = 0.0,
    val atl: Double? = 0.0,
    val ath_date: String? = "",
    val atl_date: String? = "",
    
)