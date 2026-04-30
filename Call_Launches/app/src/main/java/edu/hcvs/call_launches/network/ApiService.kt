package edu.hcvs.call_launches.network

import edu.hcvs.call_launches.model.LaunchesData
import retrofit2.Response

import retrofit2.http.GET

// API 服務接口，定義與後端 API 的交互方法
interface ApiService {

    // 獲取 Launches 列表
    @GET("v5/launches")
    suspend fun getLaunches(): Response<List<LaunchesData>>
}