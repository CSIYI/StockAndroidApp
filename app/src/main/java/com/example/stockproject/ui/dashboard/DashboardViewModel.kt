package com.example.stockproject.ui.dashboard

import android.util.Log
import androidx.lifecycle.*
import com.example.stockproject.api.StockAPI
import com.example.stockproject.model.IndexInfo
import com.example.stockproject.model.StockInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val internalIndexList = mutableListOf("%5EDJI", "%5EGSPC")
    private val internalStockList = mutableListOf("AAPL", "GOOG", "AMZN", "NVDA")
    val indexList = MutableLiveData<List<String>>()
    val stockList = MutableLiveData<List<String>>()
    val stockInfo = MutableLiveData<List<StockInfo>>()
    val indexInfo = MutableLiveData<List<IndexInfo>>()
    val mode = MutableLiveData<Int>()

    init {
        indexList.observeForever {
            viewModelScope.launch {
                it.map { async { StockAPI.fetchIndexProfile(it, "1min") } }
                        .awaitAll()
                        .let { indexInfo.postValue(it) }
            }
        }
        stockList.observeForever {
            viewModelScope.launch {
                it.map { async { StockAPI.fetchStockProfile(it) } }
                        .awaitAll()
                        .let { stockInfo.postValue(it) }
            }
        }
        indexList.postValue(internalIndexList)
        stockList.postValue(internalStockList)
        mode.postValue(0)
    }

    fun addStockToList(ticker: String) {
        internalStockList.add(ticker)
        stockList.postValue(internalStockList)
    }

    fun deleteLastStock() {
        internalStockList.removeAt(internalStockList.lastIndex)
        stockList.postValue(internalStockList)
    }


}

