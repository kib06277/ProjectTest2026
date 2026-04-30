package edu.hcvs.callnewslistapi.network

import edu.hcvs.callnewslistapi.model.NewsListModel
import retrofit2.Response
import retrofit2.http.GET

// API 服務接口，定義與後端 API 的交互方法
interface ApiService {

    // 獲取新聞列表
    @GET("v1/news/newsList")
    suspend fun getNewsList(): Response<List<NewsListModel>>
}