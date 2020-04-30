package com.example.stockproject.model

import com.google.gson.JsonObject

data class StockInfo(
        var symbol: String = "",
        var fullName: String = "",
        var price: Double = 0.0,
        var change: Double = 0.0,
        var volume: Long = 0,
        var changePercent: String = "+0.00%"
)