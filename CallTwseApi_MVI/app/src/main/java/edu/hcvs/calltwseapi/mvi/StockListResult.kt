package edu.hcvs.calltwseapi.mvi

import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.STOCK_DAY_AVG_ALL_Data

// ============================================================
// 非同步工作（API）完成後產生的結果
// Store 執行副作用（打 API）後，把結果包成 Result 交給 Reducer
// 這樣 Reducer 仍是純函式，不需要知道 API 怎麼打
// ============================================================
sealed interface StockListResult {
    data class StockDayAllLoaded(val list: List<STOCK_DAY_ALL_Data>) : StockListResult
    data class StockDayAvgLoaded(val list: List<STOCK_DAY_AVG_ALL_Data>) : StockListResult
    data class BwibbuLoaded(val list: List<BWIBBUData>) : StockListResult
}
