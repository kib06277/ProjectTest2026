package edu.hcvs.callapi.model

// 網站信息數據模型
data class SiteModel(
    val name: String, // 網站名稱
    val host_name: String, // 網站主機名稱
    val site_url: String, // 網站 URL
    val app_live_websocket: String, // 網站的 WebSocket URL
    val type_live_third_party_vendor: String, // 網站使用的第三方直播供應商類型
    val web_msg_websocket: String // 網站的 Web 消息 WebSocket URL
)