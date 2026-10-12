package edu.hcvs.calltwseapi.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.hcvs.calltwseapi.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.coroutines.cancellation.CancellationException

// ============================================================
// Store：MVI 的資料流中樞
//   View --(Intent)--> Store.dispatch()
//                        ├─ Reducer 產生新 State
//                        └─ 需要時執行副作用（打 API）→ Result → Reducer
//   View <--(State)-- Store.state（唯一的 StateFlow）
//
// 使用 Jetpack ViewModel 當作容器只是為了「轉向後保留 State」，
// 與 MVVM 的差別在於：單一 State、單一入口 dispatch()、狀態變化集中在 Reducer。
// ============================================================
class StockListStore(
    private val repository: MainRepository = MainRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(StockListState())

    // 對外唯一的狀態出口
    val state: StateFlow<StockListState> = _state.asStateFlow()

    // 是否已發出過載入（ViewModel 跨轉向存活，避免轉向後重複打 API）
    private var hasRequestedLoad = false

    // 對外唯一的事件入口
    fun dispatch(intent: StockListIntent) {
        _state.update { StockListReducer.reduce(it, intent) }

        // 副作用
        if (intent is StockListIntent.LoadStocks) loadStocks()
    }

    // 三支 API 平行載入，各自完成後產生 Result 交給 Reducer
    private fun loadStocks() {
        if (hasRequestedLoad) return
        hasRequestedLoad = true
        fetch({ repository.getSTOCK_DAY_ALL_DataList() }) { StockListResult.StockDayAllLoaded(it) }
        fetch({ repository.getSTOCK_DAY_AVG_ALL_DataList() }) { StockListResult.StockDayAvgLoaded(it) }
        fetch({ repository.getBWIBBUDataList() }) { StockListResult.BwibbuLoaded(it) }
    }

    // 共用的 API 呼叫流程：成功才產生 Result，例外印出後忽略（與原本 ViewModel 行為一致）
    private fun <T> fetch(
        call: suspend () -> Response<List<T>>,
        toResult: (List<T>) -> StockListResult
    ) {
        viewModelScope.launch {
            try {
                val response = call()
                if (response.isSuccessful) {
                    val result = toResult(response.body() ?: emptyList())
                    _state.update { StockListReducer.reduce(it, result) }
                }
            } catch (e: CancellationException) {
                throw e // 協程取消必須往外拋
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
