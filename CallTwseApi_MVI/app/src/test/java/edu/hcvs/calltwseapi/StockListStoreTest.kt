package edu.hcvs.calltwseapi

import app.cash.turbine.test
import edu.hcvs.calltwseapi.mvi.StockListIntent
import edu.hcvs.calltwseapi.mvi.StockListStore
import edu.hcvs.calltwseapi.repository.MainRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

// MVI：Store 測試（Intent 進、State 出，使用 Turbine 觀察 StateFlow）
@OptIn(ExperimentalCoroutinesApi::class)
class StockListStoreTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: MainRepository
    private lateinit var store: StockListStore

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        store = StockListStore(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `LoadStocks 後 State 應包含三支 API 的資料`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.success(listOf(stockDay("2330")))
        coEvery { repository.getSTOCK_DAY_AVG_ALL_DataList() } returns Response.success(listOf(stockAvg("2330", "915.00")))
        coEvery { repository.getBWIBBUDataList() } returns Response.success(listOf(bwibbu("2330", pe = "22.53")))

        store.dispatch(StockListIntent.LoadStocks)

        val state = store.state.value
        assertEquals("915.00", state.stockItems.single().monthlyAveragePrice)
        assertEquals("22.53", state.bwibbuMap["2330"]?.PEratio)
    }

    @Test
    fun `重複送出 LoadStocks 不應重複打 API`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.success(emptyList())
        coEvery { repository.getSTOCK_DAY_AVG_ALL_DataList() } returns Response.success(emptyList())
        coEvery { repository.getBWIBBUDataList() } returns Response.success(emptyList())

        store.dispatch(StockListIntent.LoadStocks)
        store.dispatch(StockListIntent.LoadStocks)

        coVerify(exactly = 1) { repository.getSTOCK_DAY_ALL_DataList() }
    }

    @Test
    fun `API 例外時 State 應保持初始狀態`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } throws Exception("Network error")
        coEvery { repository.getSTOCK_DAY_AVG_ALL_DataList() } throws Exception("Network error")
        coEvery { repository.getBWIBBUDataList() } throws Exception("Network error")

        store.state.test {
            val initial = awaitItem()
            store.dispatch(StockListIntent.LoadStocks)
            expectNoEvents()
            assertTrue(initial.stockItems.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OpenSortSheet 應發出彈窗顯示的新 State`() = runTest {
        store.state.test {
            awaitItem() // 初始狀態
            store.dispatch(StockListIntent.OpenSortSheet)
            assertTrue(awaitItem().isSortSheetVisible)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
