package edu.hcvs.calltwseapi.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.StockItem
import edu.hcvs.calltwseapi.mvi.StockListIntent
import edu.hcvs.calltwseapi.mvi.StockListState
import edu.hcvs.calltwseapi.mvi.StockListStore
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme

// ============================================================
// MVI 的 V（View）
// - 只做兩件事：render(State) 與 送出 Intent
// - 畫面上沒有任何 remember { mutableStateOf } 的業務狀態，全部來自單一 State
// ============================================================

// 連接 Store 的入口
@Composable
fun StockListScreen(store: StockListStore) {
    val state by store.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        store.dispatch(StockListIntent.LoadStocks)
    }

    StockListScreen(state = state, onIntent = store::dispatch)
}

// 純渲染：State 進、Intent 出（可直接用假 State 預覽或做 UI 測試）
@Composable
fun StockListScreen(
    state: StockListState,
    onIntent: (StockListIntent) -> Unit
) {
    StockListContent(
        stockItems = state.stockItems,
        currentSort = state.currentSort,
        isSortSheetVisible = state.isSortSheetVisible,
        selectedStock = state.selectedStock,
        selectedBwibbu = state.selectedBwibbu,
        onSortClick = { onIntent(StockListIntent.OpenSortSheet) },
        onSortSelected = { onIntent(StockListIntent.SelectSort(it)) },
        onSortDismiss = { onIntent(StockListIntent.DismissSortSheet) },
        onStockClick = { onIntent(StockListIntent.ClickStock(it)) },
        onDetailDismiss = { onIntent(StockListIntent.DismissDetail) }
    )
}

// 預覽：直接塞一個假 State
@Preview(showBackground = true)
@Composable
fun StockListScreenPreview() {
    val mockStock = STOCK_DAY_ALL_Data(
        Code = "2330", Name = "台積電",
        OpeningPrice = "910.00", ClosingPrice = "920.00",
        HighestPrice = "925.00", LowestPrice = "905.00",
        Change = "+10.00", TradeVolume = "25,000,000",
        TradeValue = "23,000,000,000", Transaction = "35,000",
        Date = "20250501"
    )
    CallTwseApiTheme {
        StockListScreen(
            state = StockListState(
                stockDayAllList = listOf(mockStock),
                stockItems = listOf(StockItem(mockStock, "915.00"))
            ),
            onIntent = {}
        )
    }
}
