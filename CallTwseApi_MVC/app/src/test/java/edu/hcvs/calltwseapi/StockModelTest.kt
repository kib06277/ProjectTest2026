package edu.hcvs.calltwseapi

import edu.hcvs.calltwseapi.model.SortOption
import edu.hcvs.calltwseapi.model.StockModel
import edu.hcvs.calltwseapi.repository.MainRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

// MVC：Model 單元測試（Model 不依賴 View / Controller，可以單獨測）
class StockModelTest {

    private lateinit var repository: MainRepository
    private lateinit var model: StockModel

    @Before
    fun setup() {
        repository = mockk()
        model = StockModel(repository)
    }

    @Test
    fun `載入個股日資料後應依收盤價降序並通知觀察者`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.success(
            listOf(stockDay("2330", closing = "920.00"), stockDay("0050", closing = "1,000.00"), stockDay("2317", closing = "150.00"))
        )
        var notifyCount = 0
        model.addListener { notifyCount++ }

        model.loadStockDayAll()

        assertEquals(listOf("0050", "2330", "2317"), model.stockItems.map { it.stock.Code })
        assertEquals(1, notifyCount)
    }

    @Test
    fun `載入月均價後列表應帶入月平均價`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.success(listOf(stockDay("2330")))
        coEvery { repository.getSTOCK_DAY_AVG_ALL_DataList() } returns Response.success(listOf(stockAvg("2330", "915.00")))

        model.loadStockDayAll()
        assertEquals("-", model.stockItems.single().monthlyAveragePrice)

        model.loadStockDayAvgAll()
        assertEquals("915.00", model.stockItems.single().monthlyAveragePrice)
    }

    @Test
    fun `切換排序應重新排序並通知，選相同排序則不通知`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.success(
            listOf(stockDay("A", closing = "10", change = "-1"), stockDay("B", closing = "5", change = "+2"))
        )
        model.loadStockDayAll()
        var notifyCount = 0
        model.addListener { notifyCount++ }

        model.setSort(SortOption.CHANGE)
        assertEquals(listOf("B", "A"), model.stockItems.map { it.stock.Code })
        assertEquals(1, notifyCount)

        model.setSort(SortOption.CHANGE)
        assertEquals(1, notifyCount)
    }

    @Test
    fun `API 回應失敗時資料應保持空列表`() = runTest {
        coEvery { repository.getSTOCK_DAY_ALL_DataList() } returns Response.error(500, "".toResponseBody(null))

        model.loadStockDayAll()

        assertTrue(model.stockItems.isEmpty())
    }

    @Test
    fun `移除觀察者後不應再收到通知`() = runTest {
        coEvery { repository.getBWIBBUDataList() } returns Response.success(listOf(bwibbu("2330")))
        var notifyCount = 0
        val listener = StockModel.Listener { notifyCount++ }
        model.addListener(listener)
        model.removeListener(listener)

        model.loadBwibbu()

        assertEquals(0, notifyCount)
        assertEquals("10.00", model.bwibbuMap["2330"]?.PEratio)
    }
}
