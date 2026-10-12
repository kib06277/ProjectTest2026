package edu.hcvs.calltwseapi.model

// 排序選項（原本定義在 ui/SortBottomSheet.kt，
// 因為排序屬於商業邏輯、會被 Model / Presenter / Reducer 使用，所以移到 model 層）
enum class SortOption(val label: String) {
    CLOSING_PRICE("收盤價"),
    CHANGE("漲跌價差"),
    TRADE_VOLUME("成交股數"),
    TRADE_VALUE("成交金額"),
    TRANSACTION("成交筆數")
}
