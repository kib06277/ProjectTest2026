package edu.hcvs.calltwseapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.SortOption
import edu.hcvs.calltwseapi.model.StockItem
import edu.hcvs.calltwseapi.presenter.StockListContract
import edu.hcvs.calltwseapi.presenter.StockListPresenter
import edu.hcvs.calltwseapi.ui.StockListContent
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme

// ============================================================
// MVP 的 V（View）—— 由 Activity 實作 StockListContract.View
// - 完全被動：不做任何判斷，只執行 Presenter 下達的 show / hide 指令
// - 指令內容存進 Compose 的 mutableStateOf，Compose 會自動重組畫面
// - 使用者操作一律原封不動轉給 Presenter
// 預覽請使用 StockListContentPreview（ui/StockListContent.kt）
// ============================================================
class MainActivity : ComponentActivity(), StockListContract.View {

    private val presenter: StockListContract.Presenter = StockListPresenter()

    // Presenter 指令寫入的畫面狀態
    private var stockItems by mutableStateOf<List<StockItem>>(emptyList())
    private var currentSort by mutableStateOf(SortOption.CLOSING_PRICE)
    private var isSortSheetVisible by mutableStateOf(false)
    private var selectedStock by mutableStateOf<STOCK_DAY_ALL_Data?>(null)
    private var selectedBwibbu by mutableStateOf<BWIBBUData?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        presenter.attachView(this)
        setContent {
            CallTwseApiTheme {
                StockListContent(
                    stockItems = stockItems,
                    currentSort = currentSort,
                    isSortSheetVisible = isSortSheetVisible,
                    selectedStock = selectedStock,
                    selectedBwibbu = selectedBwibbu,
                    onSortClick = presenter::onSortButtonClicked,
                    onSortSelected = presenter::onSortSelected,
                    onSortDismiss = presenter::onSortDismissed,
                    onStockClick = presenter::onStockClicked,
                    onDetailDismiss = presenter::onDetailDismissed
                )
            }
        }
        presenter.loadStocks()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    // ---------- StockListContract.View 實作 ----------

    override fun showStocks(items: List<StockItem>) {
        stockItems = items
    }

    override fun showSortOptions(currentSort: SortOption) {
        this.currentSort = currentSort
        isSortSheetVisible = true
    }

    override fun hideSortOptions() {
        isSortSheetVisible = false
    }

    override fun showStockDetail(stock: STOCK_DAY_ALL_Data, bwibbu: BWIBBUData?) {
        selectedStock = stock
        selectedBwibbu = bwibbu
    }

    override fun hideStockDetail() {
        selectedStock = null
        selectedBwibbu = null
    }
}
