package com.example.stockproject.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockproject.api.StockAPI
import com.example.stockproject.model.IndexInfo
import com.example.stockproject.model.StockInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    val indexList = listOf("%5EDJI", "%5EGSPC")
    val stockList = listOf("AAPL", "GOOG", "AMZN", "NVDA")
    val stockInfo = MutableLiveData<List<StockInfo>>()
    val indexInfo = MutableLiveData<List<IndexInfo>>()

    init {
        viewModelScope.launch {
            stockList
                    .map { async { StockAPI.fetchStockProfile(it) } }
                    .awaitAll()
                    .let { stockInfo.postValue(it) }
        }

        viewModelScope.launch {
            indexList
                    .map { async { StockAPI.fetchIndexProfile(it, "1min") } }
                    .awaitAll()
                    .let { indexInfo.postValue(it) }
        }
    }

}

