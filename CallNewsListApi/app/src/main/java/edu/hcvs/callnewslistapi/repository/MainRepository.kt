package edu.hcvs.callnewslistapi.repository

import edu.hcvs.callnewslistapi.network.NewsListRetrofitClient

// 主倉庫類，負責與 API 服務交互並提供數據給 ViewModel
class MainRepository {

    // 獲取網站列表
    suspend fun getNewsList() = NewsListRetrofitClient.api.getNewsList()
}