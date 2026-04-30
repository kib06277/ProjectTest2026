package edu.hcvs.callapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.hcvs.callapi.model.LoginResponse
import edu.hcvs.callapi.model.SiteModel
import edu.hcvs.callapi.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 主 ViewModel，負責管理 UI 狀態並與 Repository 交互
class MainViewModel : ViewModel() {

    private val repository = MainRepository() // 創建 Repository 實例

    // 使用 MutableStateFlow 來管理網站列表和登錄結果的狀態
    private val _siteList = MutableStateFlow<List<SiteModel>>(emptyList())

    // 對外暴露不可變的 StateFlow，供 UI 層觀察
    val siteList: StateFlow<List<SiteModel>> = _siteList

    // 登錄結果的 MutableStateFlow，初始值為 null
    private val _loginResult = MutableStateFlow<LoginResponse?>(null)

    // 對外暴露不可變的 StateFlow，供 UI 層觀察登錄結果
    val loginResult: StateFlow<LoginResponse?> = _loginResult

    // 獲取網站列表的方法，使用協程在 ViewModel 的作用域內執行
    fun getSiteList() {
        viewModelScope.launch {
            try {
                val response = repository.getSiteList()
                if (response.isSuccessful) {
                    _siteList.value = response.body() ?: emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 登錄方法，使用協程在 ViewModel 的作用域內執行
    fun login() {
        viewModelScope.launch {
            try {
                val response = repository.login1()
                if (response.isSuccessful) {
                    _loginResult.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}