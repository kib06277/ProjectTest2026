package edu.hcvs.callapi.network

import edu.hcvs.callapi.model.LoginRequest
import edu.hcvs.callapi.model.LoginResponse
import edu.hcvs.callapi.model.SiteModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

// API 服務接口，定義與後端 API 的交互方法
interface ApiService {

    // 獲取網站列表
    @GET("api/app/Site")
    suspend fun getSiteList(): Response<List<SiteModel>>

    // 用戶登錄(使用 raw 方式)
    @POST("api/app/backend/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // 用戶登錄(使用 from-data 方式)
    @FormUrlEncoded
    @POST("api/app/backend/auth/login")
    suspend fun login(
        @Field("account") account: String,
        @Field("password") password: String
    ): Response<LoginResponse>
}