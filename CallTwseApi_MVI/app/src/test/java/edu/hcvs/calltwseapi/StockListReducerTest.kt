package edu.hcvs.calltwseapi

import edu.hcvs.calltwseapi.model.SortOption
import edu.hcvs.calltwseapi.mvi.StockListIntent
import edu.hcvs.calltwseapi.mvi.StockListReducer
import edu.hcvs.calltwseapi.mvi.StockListResult
import edu.hcvs.calltwseapi.mvi.StockListState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

// MVI：Reducer 純函式測試（不需要協程、不需要 Mock）
class StockListReducerTest {

    private val initial = StockListState()

    @Test
    fun `個股日資料載入後應依收盤價降序產生列表`() {
        val result = StockListResult.StockDayAllLoaded(
            listOf(stockDay("2317", closing = "150.00"), stockDay("2330", closing = "920.00"))
        )

        val state = StockListReducer.reduce(initial, result)

        assertEquals(listOf("2330", "2317"), state.stockItems.map { it.stock.Code })
        assertEquals("-", state.stockItems.first().monthlyAveragePrice)
    }

    @Test
    fun `月均價載入後列表應帶入月平均價`() {
        val loaded = StockListReducer.reduce(initial, StockListResult.StockDayAllLoaded(listOf(stockDay("2330"))))

        val state = StockListReducer.reduce(loaded, StockListResult.StockDayAvgLoaded(listOf(stockAvg("2330", "915.00"))))

        assertEquals("915.00", state.stockItems.single().monthlyAveragePrice)
    }

    @Test
    fun `選擇排序應更新排序、關閉彈窗並重新排序`() {
        val loaded = StockListReducer.reduce(
            initial.copy(isSortSheetVisible = true),
            StockListResult.StockDayAllLoaded(listOf(stockDay("A", change = "-1"), stockDay("B", change = "+2")))
        )

        val state = StockListReducer.reduce(loaded, StockListIntent.SelectSort(SortOption.CHANGE))

        assertEquals(SortOption.CHANGE, state.currentSort)
        assertFalse(state.isSortSheetVisible)
        assertEquals(listOf("B", "A"), state.stockItems.map { it.stock.Code })
    }

    @Test
    fun `開關排序彈窗`() {
        val opened = StockListReducer.reduce(initial, StockListIntent.OpenSortSheet)
        assertTrue(opened.isSortSheetVisible)

        val closed = StockListReducer.reduce(opened, StockListIntent.DismissSortSheet)
        assertFalse(closed.isSortSheetVisible)
    }

    @Test
    fun `點擊個股後 selectedBwibbu 應對應到該股資料，關閉後清空`() {
        val withBwibbu = StockListReducer.reduce(initial, StockListResult.BwibbuLoaded(listOf(bwibbu("2330", pe = "22.53"))))

        val clicked = StockListReducer.reduce(withBwibbu, StockListIntent.ClickStock(stockDay("2330")))
        assertEquals("22.53", clicked.selectedBwibbu?.PEratio)

        val dismissed = StockListReducer.reduce(clicked, StockListIntent.DismissDetail)
        assertNull(dismissed.selectedStock)
        assertNull(dismissed.selectedBwibbu)
    }

    @Test
    fun `LoadStocks Intent 本身不改變狀態`() {
        assertEquals(initial, StockListReducer.reduce(initial, StockListIntent.LoadStocks))
    }
}
