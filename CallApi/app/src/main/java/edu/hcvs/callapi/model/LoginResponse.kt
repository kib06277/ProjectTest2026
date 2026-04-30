package edu.hcvs.callapi.model

// 登錄響應數據模型
data class LoginResponse(
    val token: String , // 登錄令牌
    val expire_timestamp: Long , // 令牌過期時間戳
    val store_number: String // 儲位號碼
)