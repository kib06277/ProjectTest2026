package edu.hcvs.call_launches.repository

import edu.hcvs.call_launches.network.LaunchesRetrofitClient

// 主倉庫類，負責與 API 服務交互並提供數據給 ViewModel
class MainRepository {

    // 獲取 Launches 列表
    suspend fun getLaunchesList() = LaunchesRetrofitClient.api.getLaunches()
}