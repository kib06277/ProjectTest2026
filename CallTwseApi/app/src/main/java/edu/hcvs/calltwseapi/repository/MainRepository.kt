package edu.hcvs.calltwseapi.repository

import edu.hcvs.calltwseapi.network.ExchangeReportListRetrofitClient

// 主倉庫類，負責與 API 服務交互並提供數據給 ViewModel
class MainRepository {

    // 獲取股票殖利率、股價淨值比及本益比資料列表
    suspend fun getBWIBBUDataList() = ExchangeReportListRetrofitClient.api.getBWIBBUALLList()

    // 獲取個股日平均價資料列表
    suspend fun getSTOCK_DAY_AVG_ALL_DataList() = ExchangeReportListRetrofitClient.api.getSTOCK_DAY_AVG_ALLList()

    // 獲取個股日資料
    suspend fun getSTOCK_DAY_ALL_DataList() = ExchangeReportListRetrofitClient.api.getSTOCK_DAY_ALLList()
}