package edu.hcvs.callapi.model

// 登錄請求數據模型
data class LoginRequest(
    val account: String, // 用戶帳號
    val password: String // 用戶密碼
)