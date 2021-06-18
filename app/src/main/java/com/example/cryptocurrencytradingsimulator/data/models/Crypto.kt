package com.example.cryptocurrencytradingsimulator.data.models

import java.time.LocalDateTime

data class Crypto(
    val id : String? = "",
    val symbol: String? = "",
    val name: String? = "",
    val image: String? = "",
    var favorite: Boolean? = false,
    val current_price: Double? = 0.0,
    val price_change_24h: Double? = 0.0,
    val price_change_percentage_1h_in_currency: Double? = 0.0,
    val price_change_percentage_24h_in_currency: Double? = 0.0,
    val price_change_percentage_7d_in_currency: Double? = 0.0,
    val price_change_percentage_14d_in_currency: Double? = 0.0,
    val price_change_percentage_30d_in_currency: Double? = 0.0,
    val price_change_percentage_200d_in_currency: Double? = 0.0,
    val price_change_percentage_1y_in_currency: Double? = 0.0,
    val market_cap: String? = "",
    val total_volume: String? = "",
    val ath: Double? = 0.0,
    val atl: Double? = 0.0,
    val ath_date: String? = "",
    val atl_date: String? = "",
    val high_24h: Double? = 0.0,
    val low_24h: Double? = 0.0
)