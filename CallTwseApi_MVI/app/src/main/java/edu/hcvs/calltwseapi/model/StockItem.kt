package edu.hcvs.calltwseapi.model

// 列表顯示用資料：個股日資料 + 對應的月平均價
// （原本是在 StockListScreen 內用 avgPriceMap 查表組合，現在由邏輯層先組好再交給畫面）
data class StockItem(
    val stock: STOCK_DAY_ALL_Data, // 個股日資料
    val monthlyAveragePrice: String // 月平均價，查無資料時為 "-"
)
