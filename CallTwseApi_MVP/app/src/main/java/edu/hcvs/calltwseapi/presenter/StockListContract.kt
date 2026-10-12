package edu.hcvs.calltwseapi.presenter

import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.SortOption
import edu.hcvs.calltwseapi.model.StockItem

// ============================================================
// MVP 的 Contract：用介面明確定義 View 與 Presenter 之間「能互相說什麼」
// - View 只提供「顯示 / 隱藏」這類被動指令（Passive View）
// - Presenter 只提供「使用者做了什麼」的事件入口
// 兩邊都依賴介面而非實作，所以 Presenter 可以用假的 View 做單元測試
// ============================================================
interface StockListContract {

    // View 介面：由 Activity 實作，Presenter 透過它下指令
    interface View {
        fun showStocks(items: List<StockItem>)                              // 顯示列表
        fun showSortOptions(currentSort: SortOption)                        // 顯示排序彈窗
        fun hideSortOptions()                                               // 隱藏排序彈窗
        fun showStockDetail(stock: STOCK_DAY_ALL_Data, bwibbu: BWIBBUData?) // 顯示詳細資訊
        fun hideStockDetail()                                               // 隱藏詳細資訊
    }

    // Presenter 介面：由 StockListPresenter 實作，View 把使用者操作轉給它
    interface Presenter {
        fun attachView(view: View)                    // 綁定 View
        fun detachView()                              // 解除綁定並取消進行中的請求
        fun loadStocks()                              // 載入資料
        fun onSortButtonClicked()                     // 點擊排序按鈕
        fun onSortSelected(option: SortOption)        // 選擇排序選項
        fun onSortDismissed()                         // 關閉排序彈窗
        fun onStockClicked(stock: STOCK_DAY_ALL_Data) // 點擊個股
        fun onDetailDismissed()                       // 關閉詳細資訊
    }
}
