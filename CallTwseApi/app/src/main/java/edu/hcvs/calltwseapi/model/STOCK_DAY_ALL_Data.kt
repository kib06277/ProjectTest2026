package edu.hcvs.calltwseapi.model

// 個股日資料
data class STOCK_DAY_ALL_Data(
    val Change: String, //漲跌價差
    val ClosingPrice: String, //收盤
    val Code: String, //證券代號
    val Date: String, //日期
    val HighestPrice: String, //最高價
    val LowestPrice: String, //最低價
    val Name: String, //證券名稱
    val OpeningPrice: String, //開盤價
    val TradeValue: String, //成交金額
    val TradeVolume: String, //成交股數
    val Transaction: String //成交筆數
)