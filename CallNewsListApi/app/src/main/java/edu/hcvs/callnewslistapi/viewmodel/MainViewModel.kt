package edu.hcvs.callnewslistapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.hcvs.callnewslistapi.model.NewsListModel
import edu.hcvs.callnewslistapi.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 主 ViewModel，負責管理 UI 狀態並與 Repository 交互
class MainViewModel : ViewModel() {

    private val repository = MainRepository() // 創建 Repository 實例

    // 使用 MutableStateFlow 來管理網站列表和登錄結果的狀態
    private val _newsList = MutableStateFlow<List<NewsListModel>>(emptyList())

    // 對外暴露不可變的 StateFlow，供 UI 層觀察
    val newsList: StateFlow<List<NewsListModel>> = _newsList

    // 獲取網站列表的方法，使用協程在 ViewModel 的作用域內執行
    fun getNewsList() {
        viewModelScope.launch {
            try {
                val response = repository.getNewsList()
                if (response.isSuccessful) {
                    _newsList.value = response.body() ?: emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}