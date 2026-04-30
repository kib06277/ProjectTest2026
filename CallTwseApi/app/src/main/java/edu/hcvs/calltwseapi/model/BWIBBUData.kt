package edu.hcvs.calltwseapi.model

// 股票殖利率、股價淨值比及本益比資料
data class BWIBBUData(
    val Code: String, //股票代號
    val Date: String, //日期
    val DividendYield: String, //殖利率(%)
    val Name: String, //股票名稱
    val PBratio: String, //股價淨值比
    val PEratio: String //本益比
)