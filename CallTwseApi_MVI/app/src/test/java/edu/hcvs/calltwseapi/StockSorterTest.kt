package edu.hcvs.calltwseapi

import edu.hcvs.calltwseapi.model.SortOption
import edu.hcvs.calltwseapi.model.StockSorter
import org.junit.Assert.assertEquals
import org.junit.Test

// StockSorter 純函式測試（三種架構共用的排序邏輯）
class StockSorterTest {

    @Test
    fun `依收盤價降序且能解析千分位逗號`() {
        val stocks = listOf(stockDay("A", closing = "920.00"), stockDay("B", closing = "1,000.00"), stockDay("C", closing = "15.5"))
        val result = StockSorter.sortDescending(stocks, SortOption.CLOSING_PRICE)
        assertEquals(listOf("B", "A", "C"), result.map { it.Code })
    }

    @Test
    fun `依漲跌價差降序時負值排在後面`() {
        val stocks = listOf(stockDay("A", change = "-5.00"), stockDay("B", change = "+3.00"), stockDay("C", change = "0.00"))
        val result = StockSorter.sortDescending(stocks, SortOption.CHANGE)
        assertEquals(listOf("B", "C", "A"), result.map { it.Code })
    }

    @Test
    fun `無法解析的數值視為 0`() {
        val stocks = listOf(stockDay("A", volume = "-"), stockDay("B", volume = "10"))
        val result = StockSorter.sortDescending(stocks, SortOption.TRADE_VOLUME)
        assertEquals(listOf("B", "A"), result.map { it.Code })
    }

    @Test
    fun `buildItems 應帶入月平均價，查無時為減號`() {
        val stocks = listOf(stockDay("A"), stockDay("B"))
        val avgMap = mapOf("A" to stockAvg("A", "99.00"))
        val items = StockSorter.buildItems(stocks, avgMap, SortOption.CLOSING_PRICE)
        assertEquals("99.00", items.first { it.stock.Code == "A" }.monthlyAveragePrice)
        assertEquals("-", items.first { it.stock.Code == "B" }.monthlyAveragePrice)
    }
}
