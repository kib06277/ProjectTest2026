package edu.hcvs.callnewslistapi.model

// 新聞列表項目數據模型
data class NewsListModel(
    val Date: String, // 新聞日期
    val Title: String, // 新聞標題
    val Url: String // 新聞 URL
)