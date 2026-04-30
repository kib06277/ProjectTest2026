package edu.hcvs.calltwseapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.STOCK_DAY_AVG_ALL_Data
import edu.hcvs.calltwseapi.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 主 ViewModel，負責管理 UI 狀態並與 Repository 交互
class MainViewModel(
    private val repository: MainRepository = MainRepository()
) : ViewModel() {

    // 使用 MutableStateFlow 來管理股票殖利率、股價淨值比及本益比資料的狀態
    private val _BWIBBUDataList = MutableStateFlow<List<BWIBBUData>>(emptyList())

    // 對外暴露不可變的 StateFlow，供 UI 層觀察
    val BWIBBUDataList: StateFlow<List<BWIBBUData>> = _BWIBBUDataList

    // 使用 MutableStateFlow 來管理個股日平均價資料的狀態
    private val _STOCK_DAY_AVG_ALL_DataList = MutableStateFlow<List<STOCK_DAY_AVG_ALL_Data>>(emptyList())

    // 對外暴露不可變的 StateFlow，供 UI 層觀察
    val STOCK_DAY_AVG_ALL_DataList: StateFlow<List<STOCK_DAY_AVG_ALL_Data>> = _STOCK_DAY_AVG_ALL_DataList

    // 使用 MutableStateFlow 來管理個股日資料的狀態
    private val _STOCK_DAY_ALL_DataList = MutableStateFlow<List<STOCK_DAY_ALL_Data>>(emptyList())

    // 對外暴露不可變的 StateFlow，供 UI 層觀察
    val STOCK_DAY_ALL_DataList: StateFlow<List<STOCK_DAY_ALL_Data>> = _STOCK_DAY_ALL_DataList

    // 獲取股票殖利率、股價淨值比及本益比資料的方法，使用協程在 ViewModel 的作用域內執行
    fun getBWIBBUDataList() {
        viewModelScope.launch {
            try {
                val response = repository.getBWIBBUDataList()
                if (response.isSuccessful) {
                    _BWIBBUDataList.value = response.body() ?: emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 獲取個股日平均價資料的方法，使用協程在 ViewModel 的作用域內執行
    fun getSTOCK_DAY_AVG_ALL_DataList() {
        viewModelScope.launch {
            try {
                val response = repository.getSTOCK_DAY_AVG_ALL_DataList()
                if (response.isSuccessful) {
                    _STOCK_DAY_AVG_ALL_DataList.value = response.body() ?: emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 獲取個股日資料的方法，使用協程在 ViewModel 的作用域內執行
    fun getSTOCK_DAY_ALL_DataList() {
        viewModelScope.launch {
            try {
                val response = repository.getSTOCK_DAY_ALL_DataList()
                if (response.isSuccessful) {
                    _STOCK_DAY_ALL_DataList.value = response.body() ?: emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}