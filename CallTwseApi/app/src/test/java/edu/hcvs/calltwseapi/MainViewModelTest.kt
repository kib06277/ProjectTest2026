package edu.hcvs.calltwseapi

import app.cash.turbine.test
import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.repository.MainRepository
import edu.hcvs.calltwseapi.viewmodel.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

// MainViewModel 的單元測試，使用 MockK 模擬 Repository 的行為，並使用 Turbine 測試 StateFlow 的更新
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher() // 使用 UnconfinedTestDispatcher 來執行協程，確保測試的同步性
    private lateinit var repository: MainRepository // 模擬的 Repository，將在測試中被替換為 MockK 的實現
    private lateinit var viewModel: MainViewModel // 要測試的 ViewModel 實例

    // 在每個測試之前設置測試環境，將 MainDispatcher 替換為測試用的 Dispatcher，並初始化 ViewModel 和 Repository
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = MainViewModel(repository)
    }

    // 在每個測試之後重置 MainDispatcher，確保不影響其他測試
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 測試當 getBWIBBUDataList 成功時，ViewModel 的 StateFlow 是否正確更新為模擬的資料列表
    @Test
    fun `getBWIBBUDataList 成功時應更新 StateFlow`() = runTest {
        val mockData = listOf(
            BWIBBUData("2330", "20250501", "1.85", "台積電", "6.12", "22.53")
        )
        coEvery { repository.getBWIBBUDataList() } returns Response.success(mockData)

        viewModel.BWIBBUDataList.test {
            assertEquals(emptyList<BWIBBUData>(), awaitItem())
            viewModel.getBWIBBUDataList()
            assertEquals(mockData, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // 測試當 getSTOCK_DAY_ALL_DataList 成功時，ViewModel 的 StateFlow 是否正確更新為模擬的資料列表
    @Test
    fun `getSTOCK_DAY_ALL_DataList 成功時應更新 StateFlow`() = runTest {
        val mockData = listOf(
            STOCK_DAY_ALL_Data(
                Change = "+10.00", ClosingPrice = "920.00", Code = "2330",
                Date = "20250501", HighestPrice = "925.00", LowestPrice = "905.00",
                Name = "台積電", OpeningPrice = "910.00", TradeValue = "23,000,000,000",
                TradeVolume = "25,000,000", Transaction = "35,000"
            )
        )
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.success(mockData)

        viewModel.STOCK_DAY_ALL_DataList.test {
            assertEquals(emptyList<STOCK_DAY_ALL_Data>(), awaitItem())
            viewModel.getSTOCK_DAY_ALL_DataList()
            assertEquals(mockData, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // 測試當 API 發生例外時，ViewModel 的 StateFlow 是否保持為空列表，確保錯誤處理不會導致狀態更新
    @Test
    fun `API 發生例外時 StateFlow 應保持空列表`() = runTest {
        coEvery { repository.getBWIBBUDataList() } throws Exception("Network error")

        viewModel.BWIBBUDataList.test {
            assertEquals(emptyList<BWIBBUData>(), awaitItem())
            viewModel.getBWIBBUDataList()
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }
}