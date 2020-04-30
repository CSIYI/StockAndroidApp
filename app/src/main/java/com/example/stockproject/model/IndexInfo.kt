package com.example.stockproject.model

import java.math.BigInteger

data class IndexInfo (
    var index: String = "",
    var name: String = "",
    var span: String = "",
    var price: Double = 0.0,
    var open: Double = 0.0,
    var dayLow: Double = 0.0,
    var dayHigh: Double = 0.0,
    var volume: Long = 0,
    var datapoint: List<Double> = listOf()
)