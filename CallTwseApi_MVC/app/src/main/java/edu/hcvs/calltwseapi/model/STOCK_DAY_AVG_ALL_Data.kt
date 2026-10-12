package edu.hcvs.calltwseapi.model

// 個股日平均價資料
data class STOCK_DAY_AVG_ALL_Data(
    val ClosingPrice: String, //收盤價
    val Code: String, //股票代號
    val Date: String, //日期
    val MonthlyAveragePrice: String, //月平均價
    val Name: String //股票名稱
)