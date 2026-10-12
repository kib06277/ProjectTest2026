package edu.hcvs.calltwseapi.model

import edu.hcvs.calltwseapi.repository.MainRepository

// ============================================================
// MVC 的 M（Model）
// - 持有畫面需要的資料與商業邏輯（載入、排序、組合月均價）
// - 不認識 View，也不認識 Controller
// - 資料改變時透過「觀察者模式」通知已註冊的 Listener（通常就是 View）
// ============================================================
class StockModel(
    private val repository: MainRepository = MainRepository()
) {

    // Model 變動監聽介面（View 實作後註冊進來）
    fun interface Listener {
        fun onModelChanged(model: StockModel)
    }

    private val listeners = mutableListOf<Listener>()

    // 個股日資料（原始順序）
    var stockDayAllList: List<STOCK_DAY_ALL_Data> = emptyList()
        private set

    // 以股票代號為 key 的月平均價資料
    var avgPriceMap: Map<String, STOCK_DAY_AVG_ALL_Data> = emptyMap()
        private set

    // 以股票代號為 key 的本益比 / 殖利率 / 股價淨值比資料
    var bwibbuMap: Map<String, BWIBBUData> = emptyMap()
        private set

    // 目前排序選項
    var currentSort: SortOption = SortOption.CLOSING_PRICE
        private set

    // 已排序、可直接顯示的列表
    var stockItems: List<StockItem> = emptyList()
        private set

    // 註冊 / 移除觀察者
    fun addListener(listener: Listener) {
        listeners += listener
    }

    fun removeListener(listener: Listener) {
        listeners -= listener
    }

    // 載入個股日資料（例外交由呼叫端 Controller 處理）
    suspend fun loadStockDayAll() {
        val response = repository.getSTOCK_DAY_ALL_DataList()
        if (response.isSuccessful) {
            stockDayAllList = response.body() ?: emptyList()
            rebuildItems()
        }
    }

    // 載入個股日平均價資料
    suspend fun loadStockDayAvgAll() {
        val response = repository.getSTOCK_DAY_AVG_ALL_DataList()
        if (response.isSuccessful) {
            avgPriceMap = (response.body() ?: emptyList()).associateBy { it.Code }
            rebuildItems()
        }
    }

    // 載入本益比 / 殖利率 / 股價淨值比資料
    suspend fun loadBwibbu() {
        val response = repository.getBWIBBUDataList()
        if (response.isSuccessful) {
            bwibbuMap = (response.body() ?: emptyList()).associateBy { it.Code }
            notifyChanged()
        }
    }

    // 變更排序方式
    fun setSort(option: SortOption) {
        if (option == currentSort) return
        currentSort = option
        rebuildItems()
    }

    // 重新排序並組合顯示用列表，完成後通知觀察者
    private fun rebuildItems() {
        stockItems = StockSorter.buildItems(stockDayAllList, avgPriceMap, currentSort)
        notifyChanged()
    }

    // 通知所有觀察者（複製一份避免通知過程中被修改）
    private fun notifyChanged() {
        listeners.toList().forEach { it.onModelChanged(this) }
    }
}
