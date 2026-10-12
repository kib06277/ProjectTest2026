package edu.hcvs.calltwseapi.model

// 排序與列表組合的純函式（不依賴 Android，方便單元測試）
// 原本這段邏輯寫在 StockListScreen 的 remember 區塊內，現在抽出來給各架構的邏輯層使用
object StockSorter {

    // 依排序選項做降序排序
    fun sortDescending(
        stocks: List<STOCK_DAY_ALL_Data>,
        option: SortOption
    ): List<STOCK_DAY_ALL_Data> =
        stocks.sortedByDescending { stock ->
            when (option) {
                SortOption.CLOSING_PRICE -> stock.ClosingPrice
                SortOption.CHANGE        -> stock.Change
                SortOption.TRADE_VOLUME  -> stock.TradeVolume
                SortOption.TRADE_VALUE   -> stock.TradeValue
                SortOption.TRANSACTION   -> stock.Transaction
            }.toSortValue()
        }

    // 排序後組合成畫面要顯示的 StockItem（帶入月平均價）
    fun buildItems(
        stocks: List<STOCK_DAY_ALL_Data>,
        avgPriceMap: Map<String, STOCK_DAY_AVG_ALL_Data>,
        option: SortOption
    ): List<StockItem> =
        sortDescending(stocks, option).map { stock ->
            StockItem(
                stock = stock,
                monthlyAveragePrice = avgPriceMap[stock.Code]?.MonthlyAveragePrice ?: "-"
            )
        }

    // 去除千分位逗號後轉成數字，無法解析時視為 0
    private fun String.toSortValue(): Double = replace(",", "").toDoubleOrNull() ?: 0.0
}
