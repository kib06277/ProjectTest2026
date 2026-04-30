package edu.hcvs.callapi.repository

import edu.hcvs.callapi.model.LoginRequest
import edu.hcvs.callapi.network.LoginRetrofitClient
import edu.hcvs.callapi.network.SiteRetrofitClient

// 主倉庫類，負責與 API 服務交互並提供數據給 ViewModel
class MainRepository {

    // 獲取網站列表
    suspend fun getSiteList() = SiteRetrofitClient.api.getSiteList()

    // 用戶登錄，使用硬編碼的帳號和密碼進行測試
    suspend fun login() =
        LoginRetrofitClient.api.login(
            LoginRequest(
                account = "TESTCharlie",
                password = "TESTCharlie"
            )
        )

    suspend fun login1() =
        LoginRetrofitClient.api.login(
            account = "TESTCharlie",
            password = "TESTCharlie"
        )
}