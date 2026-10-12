package edu.hcvs.calltwseapi.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Retrofit 客戶端對象，負責創建 Retrofit 實例並提供 API 服務接口的實現
object ExchangeReportListRetrofitClient {

    // 基礎 URL，指向後端 API 的根地址
    private const val BASE_URL = "https://openapi.twse.com.tw/"

    // 日誌攔截器，用於打印 HTTP 請求和響應的詳細信息，方便調試
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient，添加日誌攔截器以便在調試時查看請求和響應的詳細信息
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // API 服務接口的實現，使用 Retrofit 創建 ApiService 的實例
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}