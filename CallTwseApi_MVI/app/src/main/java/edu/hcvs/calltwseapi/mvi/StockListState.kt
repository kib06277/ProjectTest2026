package edu.hcvs.calltwseapi.mvi

import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.STOCK_DAY_AVG_ALL_Data
import edu.hcvs.calltwseapi.model.SortOption
import edu.hcvs.calltwseapi.model.StockItem

// ============================================================
// MVI 的 M（Model = 畫面狀態 State）
// - 整個畫面「只有一個」不可變的 State（Single Source of Truth）
// - 原本 MVVM 有三個 StateFlow + 畫面裡多個 remember 狀態，這裡全部合併
// - 任何時刻的畫面 = render(State)，方便除錯、重現問題
// ============================================================
data class StockListState(
    val stockDayAllList: List<STOCK_DAY_ALL_Data> = emptyList(),      // 個股日資料（原始順序）
    val avgPriceMap: Map<String, STOCK_DAY_AVG_ALL_Data> = emptyMap(), // 月平均價（以代號為 key）
    val bwibbuMap: Map<String, BWIBBUData> = emptyMap(),               // 本益比等資料（以代號為 key）
    val currentSort: SortOption = SortOption.CLOSING_PRICE,            // 目前排序
    val stockItems: List<StockItem> = emptyList(),                     // 已排序、可直接顯示的列表
    val isSortSheetVisible: Boolean = false,                           // 排序彈窗是否顯示
    val selectedStock: STOCK_DAY_ALL_Data? = null                      // 目前點選的個股
) {
    // 目前點選個股對應的本益比資料（由 State 推導，不另外存）
    val selectedBwibbu: BWIBBUData?
        get() = selectedStock?.let { bwibbuMap[it.Code] }
}
