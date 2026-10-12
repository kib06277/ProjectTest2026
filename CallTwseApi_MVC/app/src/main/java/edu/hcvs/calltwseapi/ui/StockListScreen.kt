package edu.hcvs.calltwseapi.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import edu.hcvs.calltwseapi.controller.StockController
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.StockModel

// ============================================================
// MVC 的 V（View）
// - 直接「觀察」Model：註冊 StockModel.Listener，Model 變動時刷新畫面
// - 使用者操作中「會改變資料」的（載入、排序）交給 Controller
// - 純畫面狀態（彈窗開關、點選哪一筆）留在 View 自己管理
// 這就是經典 MVC 三角關係：View → Controller → Model → (通知) → View
// ============================================================
@Composable
fun StockListScreen(model: StockModel, controller: StockController) {

    // 從 Model 讀出來的資料快照（Model 通知時更新）
    var stockItems by remember { mutableStateOf(model.stockItems) }
    var currentSort by remember { mutableStateOf(model.currentSort) }
    var bwibbuMap by remember { mutableStateOf(model.bwibbuMap) }

    // 註冊 / 解除 Model 觀察者
    DisposableEffect(model) {
        val listener = StockModel.Listener { changed ->
            stockItems = changed.stockItems
            currentSort = changed.currentSort
            bwibbuMap = changed.bwibbuMap
        }
        model.addListener(listener)
        onDispose { model.removeListener(listener) }
    }

    // 純 View 狀態
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedStock by remember { mutableStateOf<STOCK_DAY_ALL_Data?>(null) }

    // 畫面進入時通知 Controller
    LaunchedEffect(Unit) {
        controller.onScreenStarted()
    }

    StockListContent(
        stockItems = stockItems,
        currentSort = currentSort,
        isSortSheetVisible = showBottomSheet,
        selectedStock = selectedStock,
        selectedBwibbu = selectedStock?.let { bwibbuMap[it.Code] },
        onSortClick = { showBottomSheet = true },
        onSortSelected = { controller.onSortSelected(it) },
        onSortDismiss = { showBottomSheet = false },
        onStockClick = { selectedStock = it },
        onDetailDismiss = { selectedStock = null }
    )
}
