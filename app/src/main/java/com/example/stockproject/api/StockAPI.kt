package com.example.stockproject.api

import android.content.Context
import com.example.stockproject.model.IndexInfo
import com.example.stockproject.model.StockInfo
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import kotlinx.coroutines.*


object StockAPI {
    private lateinit var ctx: Context
    private lateinit var ioco: CoroutineScope
    private const val URL_BASE = "https://financialmodelingprep.com/api/v3"

    fun init(context: Context) {
        ctx = context
        ioco = CoroutineScope(Dispatchers.IO)
    }

    private fun fetchAsync(url: String): Deferred<JsonElement> =
            ioco.async {
                Fuel.get(url)
                        .awaitString()
                        .let { JsonParser.parseString(it) }
            }

    suspend fun fetchStockProfile(sym: String): StockInfo {
        val json = fetchAsync("$URL_BASE/company/profile/$sym")
                .await()
                .asJsonObject
        val profileJson = json.getAsJsonObject("profile")
        return StockInfo().apply {
            symbol = json.get("symbol").asString
            fullName = profileJson.get("companyName").asString
            price = profileJson.get("price").asDouble
            change = profileJson.get("changes").asDouble
        }
    }

    suspend fun fetchIndexProfile(index: String, span: String): IndexInfo {

        val info = IndexInfo()
        val quote = fetchAsync("$URL_BASE/quote/$index")
        val timeSeries = fetchAsync("$URL_BASE/historical-chart/$span/$index")

        quote.await().asJsonArray.get(0).asJsonObject.apply {
            info.index = get("symbol").asString
            info.span = span
            info.price = get("price").asDouble
            info.name = get("name").asString
            info.open = get("open").asDouble
            info.dayLow = get("dayLow").asDouble
            info.dayHigh = get("dayHigh").asDouble
            info.volume = get("volume").asLong
        }

        timeSeries.await().asJsonArray.apply {
            info.datapoint = this.map { it.asJsonObject.get("open").asDouble }
        }
        return info
    }
}