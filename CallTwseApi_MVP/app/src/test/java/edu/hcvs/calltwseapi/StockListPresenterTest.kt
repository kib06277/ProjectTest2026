package edu.hcvs.calltwseapi

import edu.hcvs.calltwseapi.model.SortOption
import edu.hcvs.calltwseapi.model.StockItem
import edu.hcvs.calltwseapi.presenter.StockListContract
import edu.hcvs.calltwseapi.presenter.StockListPresenter
import edu.hcvs.calltwseapi.repository.MainRepository
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

// MVP：Presenter 單元測試
// View 是介面，直接用 MockK 建一個假的 View，驗證 Presenter「有沒有下對指令」
@OptIn(ExperimentalCoroutinesApi::class)
class StockListPresenterTest {

    private lateinit var repository: MainRepository
    private lateinit var view: StockListContract.View

    @Before
    fun setup() {
        repository = mockk()
        view = mockk(relaxed = true)
    }

    // 建立 Presenter 並綁定假 View
    private fun TestScope.createPresenter() =
        StockListPresenter(repository, this).also { it.attachView(view) }

    @Test
    fun `loadStocks 成功後應命令 View 顯示依收盤價降序的列表`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.success(
            listOf(stockDay("2317", closing = "150.00"), stockDay("2330", closing = "920.00"))
        )
        coEvery { repository.getSTOCK_DAY_AVG_ALL_DataList() } returns Response.success(listOf(stockAvg("2330", "915.00")))
        coEvery { repository.getBWIBBUDataList() } returns Response.success(emptyList())
        val presenter = createPresenter()

        presenter.loadStocks()
        advanceUntilIdle()

        // 最後一次推送：兩支 API 都回來後的完整列表
        verify {
            view.showStocks(
                listOf(
                    StockItem(stockDay("2330", closing = "920.00"), "915.00"),
                    StockItem(stockDay("2317", closing = "150.00"), "-")
                )
            )
        }
    }

    @Test
    fun `選擇排序後應隱藏彈窗並推送重新排序的列表`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.success(
            listOf(stockDay("A", change = "-1"), stockDay("B", change = "+2"))
        )
        coEvery { repository.getSTOCK_DAY_AVG_ALL_DataList() } returns Response.success(emptyList())
        coEvery { repository.getBWIBBUDataList() } returns Response.success(emptyList())
        val presenter = createPresenter()
        presenter.loadStocks()
        advanceUntilIdle()

        presenter.onSortSelected(SortOption.CHANGE)

        verify { view.hideSortOptions() }
        verify { view.showStocks(listOf(StockItem(stockDay("B", change = "+2"), "-"), StockItem(stockDay("A", change = "-1"), "-"))) }
    }

    @Test
    fun `點擊排序按鈕應帶目前排序開啟彈窗`() = runTest {
        val presenter = createPresenter()

        presenter.onSortButtonClicked()

        verify { view.showSortOptions(SortOption.CLOSING_PRICE) }
    }

    @Test
    fun `點擊個股應命令 View 顯示對應的本益比資料`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.success(emptyList())
        coEvery { repository.getSTOCK_DAY_AVG_ALL_DataList() } returns Response.success(emptyList())
        coEvery { repository.getBWIBBUDataList() } returns Response.success(listOf(bwibbu("2330", pe = "22.53")))
        val presenter = createPresenter()
        presenter.loadStocks()
        advanceUntilIdle()

        presenter.onStockClicked(stockDay("2330"))

        verify { view.showStockDetail(stockDay("2330"), bwibbu("2330", pe = "22.53")) }
    }

    @Test
    fun `API 例外時不應命令 View 顯示任何列表`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } throws Exception("Network error")
        coEvery { repository.getSTOCK_DAY_AVG_ALL_DataList() } throws Exception("Network error")
        coEvery { repository.getBWIBBUDataList() } throws Exception("Network error")
        val presenter = createPresenter()

        presenter.loadStocks()
        advanceUntilIdle()

        verify(exactly = 0) { view.showStocks(any()) }
    }

    @Test
    fun `detachView 後不應再對 View 下任何指令`() = runTest {
        val presenter = createPresenter()
        presenter.detachView()

        presenter.onSortButtonClicked()
        presenter.onStockClicked(stockDay("2330"))

        confirmVerified(view)
    }
}
