package edu.hcvs.calltwseapi.presenter

import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.STOCK_DAY_AVG_ALL_Data
import edu.hcvs.calltwseapi.model.SortOption
import edu.hcvs.calltwseapi.model.StockSorter
import edu.hcvs.calltwseapi.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.coroutines.cancellation.CancellationException

// ============================================================
// MVP 的 P（Presenter）
// - 持有畫面邏輯與狀態（資料、排序、目前點選的個股）
// - 呼叫 Repository（Model）取資料，處理完後「主動命令」View 顯示
// - 純 Kotlin 類別，不繼承任何 Android 類別，不 import 任何 Compose
// - 與 MVC 最大差別：View 不觀察 Model，所有顯示都由 Presenter 推送
// ============================================================
class StockListPresenter(
    private val repository: MainRepository = MainRepository(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) : StockListContract.Presenter {

    // 目前綁定的 View（可能為 null，例如 Activity 已銷毀）
    private var view: StockListContract.View? = null

    // Presenter 內部持有的狀態
    private var stockDayAllList: List<STOCK_DAY_ALL_Data> = emptyList()
    private var avgPriceMap: Map<String, STOCK_DAY_AVG_ALL_Data> = emptyMap()
    private var bwibbuMap: Map<String, BWIBBUData> = emptyMap()
    private var currentSort: SortOption = SortOption.CLOSING_PRICE
    private var selectedStock: STOCK_DAY_ALL_Data? = null

    override fun attachView(view: StockListContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
        scope.coroutineContext.cancelChildren() // 取消所有進行中的 API 請求，避免洩漏
    }

    // 三支 API 平行載入，各自回來就各自更新畫面
    override fun loadStocks() {
        fetch({ repository.getSTOCK_DAY_ALL_DataList() }) { list ->
            stockDayAllList = list
            renderStocks()
        }
        fetch({ repository.getSTOCK_DAY_AVG_ALL_DataList() }) { list ->
            avgPriceMap = list.associateBy { it.Code }
            renderStocks()
        }
        fetch({ repository.getBWIBBUDataList() }) { list ->
            bwibbuMap = list.associateBy { it.Code }
            // 若使用者已打開詳細資訊，資料到了就刷新對話框
            selectedStock?.let { view?.showStockDetail(it, bwibbuMap[it.Code]) }
        }
    }

    override fun onSortButtonClicked() {
        view?.showSortOptions(currentSort)
    }

    override fun onSortSelected(option: SortOption) {
        currentSort = option
        view?.hideSortOptions()
        renderStocks()
    }

    override fun onSortDismissed() {
        view?.hideSortOptions()
    }

    override fun onStockClicked(stock: STOCK_DAY_ALL_Data) {
        selectedStock = stock
        view?.showStockDetail(stock, bwibbuMap[stock.Code])
    }

    override fun onDetailDismissed() {
        selectedStock = null
        view?.hideStockDetail()
    }

    // 排序後推送給 View
    private fun renderStocks() {
        view?.showStocks(StockSorter.buildItems(stockDayAllList, avgPriceMap, currentSort))
    }

    // 共用的 API 呼叫流程：成功才處理，例外印出後忽略（與原本 ViewModel 行為一致）
    private fun <T> fetch(
        call: suspend () -> Response<List<T>>,
        onSuccess: (List<T>) -> Unit
    ) {
        scope.launch {
            try {
                val response = call()
                if (response.isSuccessful) {
                    onSuccess(response.body() ?: emptyList())
                }
            } catch (e: CancellationException) {
                throw e // 協程取消必須往外拋
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
