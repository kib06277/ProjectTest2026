package edu.hcvs.calltwseapi.network

import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.STOCK_DAY_AVG_ALL_Data
import retrofit2.Response
import retrofit2.http.GET

// API 服務接口，定義與後端 API 的交互方法
interface ApiService {

    // 獲取股票殖利率、股價淨值比及本益比資料
    @GET("v1/exchangeReport/BWIBBU_ALL")
    suspend fun getBWIBBUALLList(): Response<List<BWIBBUData>>

    // 獲取個股日平均價資料
    @GET("v1/exchangeReport/STOCK_DAY_AVG_ALL")
    suspend fun getSTOCK_DAY_AVG_ALLList(): Response<List<STOCK_DAY_AVG_ALL_Data>>

    // 獲取個股日資料
    @GET("v1/exchangeReport/STOCK_DAY_ALL")
    suspend fun getSTOCK_DAY_ALLList(): Response<List<STOCK_DAY_ALL_Data>>
}