package edu.hcvs.calltwseapi.mvi

import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.SortOption

// ============================================================
// MVI 的 I（Intent）：使用者「想做什麼」
// View 不呼叫任何函式名稱，只送出 Intent 物件給 Store
// 所有可能的使用者操作都列在這裡，一目了然
// ============================================================
sealed interface StockListIntent {
    data object LoadStocks : StockListIntent                              // 載入資料
    data object OpenSortSheet : StockListIntent                           // 點擊排序按鈕
    data object DismissSortSheet : StockListIntent                        // 關閉排序彈窗
    data class SelectSort(val option: SortOption) : StockListIntent       // 選擇排序選項
    data class ClickStock(val stock: STOCK_DAY_ALL_Data) : StockListIntent // 點擊個股
    data object DismissDetail : StockListIntent                           // 關閉詳細資訊
}
