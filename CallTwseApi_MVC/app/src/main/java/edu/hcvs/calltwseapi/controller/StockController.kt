package edu.hcvs.calltwseapi.controller

import edu.hcvs.calltwseapi.model.SortOption
import edu.hcvs.calltwseapi.model.StockModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

// ============================================================
// MVC 的 C（Controller）
// - 接收 View 轉來的使用者操作，決定要對 Model 做什麼
// - 只「寫」Model，不負責把資料推回 View（View 自己觀察 Model）
// - CoroutineScope 由外部注入（Activity 傳 lifecycleScope，測試傳 TestScope）
// ============================================================
class StockController(
    private val model: StockModel,
    private val scope: CoroutineScope
) {

    // 畫面啟動：三支 API 平行載入，任一支失敗不影響其他
    fun onScreenStarted() {
        launchSafely { model.loadStockDayAll() }
        launchSafely { model.loadStockDayAvgAll() }
        launchSafely { model.loadBwibbu() }
    }

    // 使用者選擇排序方式
    fun onSortSelected(option: SortOption) {
        model.setSort(option)
    }

    // 啟動協程並捕捉例外（行為與原本 ViewModel 的 try / catch 相同）
    private fun launchSafely(block: suspend () -> Unit): Job =
        scope.launch {
            try {
                block()
            } catch (e: CancellationException) {
                throw e // 協程取消必須往外拋，不能吞掉
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
}
