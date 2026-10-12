package edu.hcvs.calltwseapi.mvi

import edu.hcvs.calltwseapi.model.StockSorter

// ============================================================
// Reducer：(舊 State, Intent 或 Result) -> 新 State
// - 純函式：沒有副作用、不打 API、不碰 Android，同樣輸入一定得到同樣輸出
// - 所有狀態變化都集中在這裡，是 MVI 最容易測試的部分
// ============================================================
object StockListReducer {

    // 處理使用者 Intent
    fun reduce(state: StockListState, intent: StockListIntent): StockListState =
        when (intent) {
            StockListIntent.LoadStocks -> state // 載入屬於副作用，由 Store 處理，狀態不變
            StockListIntent.OpenSortSheet -> state.copy(isSortSheetVisible = true)
            StockListIntent.DismissSortSheet -> state.copy(isSortSheetVisible = false)
            is StockListIntent.SelectSort ->
                state.copy(currentSort = intent.option, isSortSheetVisible = false).withSortedItems()
            is StockListIntent.ClickStock -> state.copy(selectedStock = intent.stock)
            StockListIntent.DismissDetail -> state.copy(selectedStock = null)
        }

    // 處理 API 回來的 Result
    fun reduce(state: StockListState, result: StockListResult): StockListState =
        when (result) {
            is StockListResult.StockDayAllLoaded ->
                state.copy(stockDayAllList = result.list).withSortedItems()
            is StockListResult.StockDayAvgLoaded ->
                state.copy(avgPriceMap = result.list.associateBy { it.Code }).withSortedItems()
            is StockListResult.BwibbuLoaded ->
                state.copy(bwibbuMap = result.list.associateBy { it.Code })
        }

    // 依目前資料與排序重新產生顯示列表
    private fun StockListState.withSortedItems(): StockListState =
        copy(stockItems = StockSorter.buildItems(stockDayAllList, avgPriceMap, currentSort))
}
