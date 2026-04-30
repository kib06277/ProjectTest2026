package edu.hcvs.call_launches.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.hcvs.call_launches.model.LaunchesData
import edu.hcvs.call_launches.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 主 ViewModel，負責管理 UI 狀態並與 Repository 交互
class MainViewModel : ViewModel() {

    private val repository = MainRepository() // 創建 Repository 實例

    // 使用 MutableStateFlow 來管理網站列表和登錄結果的狀態
    private val _LaunchesList = MutableStateFlow<List<LaunchesData>>(emptyList())

    // 對外暴露不可變的 StateFlow，供 UI 層觀察
    val LaunchesList: StateFlow<List<LaunchesData>> = _LaunchesList

    // 登錄結果的 MutableStateFlow，初始值為 null
    fun getLaunchesList() {
        viewModelScope.launch {
            try {
                val response = repository.getLaunchesList()
                if (response.isSuccessful) {
                    _LaunchesList.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}