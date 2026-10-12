package edu.hcvs.calltwseapi

import edu.hcvs.calltwseapi.controller.StockController
import edu.hcvs.calltwseapi.model.SortOption
import edu.hcvs.calltwseapi.model.StockModel
import edu.hcvs.calltwseapi.repository.MainRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

// MVC：Controller 單元測試（注入 TestScope 取代 lifecycleScope）
@OptIn(ExperimentalCoroutinesApi::class)
class StockControllerTest {

    private val repository: MainRepository = mockk()
    private val model = StockModel(repository)

    @Test
    fun `onScreenStarted 應載入三支 API 的資料到 Model`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.success(listOf(stockDay("2330")))
        coEvery { repository.getSTOCK_DAY_AVG_ALL_DataList() } returns Response.success(listOf(stockAvg("2330", "915.00")))
        coEvery { repository.getBWIBBUDataList() } returns Response.success(listOf(bwibbu("2330", pe = "22.53")))
        val controller = StockController(model, this)

        controller.onScreenStarted()
        advanceUntilIdle()

        assertEquals("915.00", model.stockItems.single().monthlyAveragePrice)
        assertEquals("22.53", model.bwibbuMap["2330"]?.PEratio)
    }

    @Test
    fun `其中一支 API 例外時不影響其他資料載入`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.success(listOf(stockDay("2330")))
        coEvery { repository.getSTOCK_DAY_AVG_ALL_DataList() } throws Exception("Network error")
        coEvery { repository.getBWIBBUDataList() } returns Response.success(listOf(bwibbu("2330")))
        val controller = StockController(model, this)

        controller.onScreenStarted()
        advanceUntilIdle()

        assertEquals(1, model.stockItems.size)
        assertEquals("-", model.stockItems.single().monthlyAveragePrice)
        assertTrue(model.bwibbuMap.containsKey("2330"))
    }

    @Test
    fun `onSortSelected 應更新 Model 的排序`() = runTest {
        val controller = StockController(model, this)

        controller.onSortSelected(SortOption.TRADE_VALUE)

        assertEquals(SortOption.TRADE_VALUE, model.currentSort)
    }
}
