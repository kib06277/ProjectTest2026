package edu.hcvs.calltwseapi

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.ui.StockCard
import edu.hcvs.calltwseapi.ui.StockDetailDialog
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme
import org.junit.Rule
import org.junit.Test

// 測試 StockCard 和 StockDetailDialog 的顯示與互動
class StockCardTest {

    @get:Rule
    val composeTestRule = createComposeRule() // 模擬一筆股票資料，用於測試 StockCard 和 StockDetailDialog 的顯示

    // 模擬一筆股票資料，用於測試 StockCard 和 StockDetailDialog 的顯示
    private val mockStock = STOCK_DAY_ALL_Data(
        Code = "2330", Name = "台積電",
        OpeningPrice = "910.00", ClosingPrice = "920.00",
        HighestPrice = "925.00", LowestPrice = "905.00",
        Change = "+10.00", TradeVolume = "25,000,000",
        TradeValue = "23,000,000,000", Transaction = "35,000",
        Date = "20250501"
    )

    // 測試 StockCard 是否正確顯示股票名稱與代號
    @Test
    fun `StockCard 應顯示股票名稱與代號`() {
        composeTestRule.setContent {
            CallTwseApiTheme {
                StockCard(stock = mockStock, monthlyAveragePrice = "915.00", onClick = {})
            }
        }
        composeTestRule.onNodeWithText("台積電").assertIsDisplayed()
        composeTestRule.onNodeWithText("(2330)").assertIsDisplayed()
    }

    // 測試 StockCard 點擊後是否觸發 onClick 事件
    @Test
    fun `StockCard 點擊後應觸發 onClick`() {
        var clicked = false
        composeTestRule.setContent {
            CallTwseApiTheme {
                StockCard(stock = mockStock, monthlyAveragePrice = "915.00", onClick = { clicked = true })
            }
        }
        composeTestRule.onNodeWithText("台積電").performClick()
        assert(clicked)
    }

    // 測試 StockDetailDialog 是否正確顯示本益比、殖利率和股價淨值比
    @Test
    fun `StockDetailDialog 應顯示本益比殖利率股價淨值比`() {
        val mockBwibbu = BWIBBUData(
            Code = "2330", Name = "台積電", Date = "20250501",
            PEratio = "22.53", DividendYield = "1.85", PBratio = "6.12"
        )
        composeTestRule.setContent {
            CallTwseApiTheme {
                StockDetailDialog(stock = mockStock, bwibbuData = mockBwibbu, onDismiss = {})
            }
        }
        composeTestRule.onNodeWithText("本益比").assertIsDisplayed()
        composeTestRule.onNodeWithText("22.53").assertIsDisplayed()
        composeTestRule.onNodeWithText("殖利率(%)").assertIsDisplayed()
        composeTestRule.onNodeWithText("1.85").assertIsDisplayed()
        composeTestRule.onNodeWithText("股價淨值比").assertIsDisplayed()
        composeTestRule.onNodeWithText("6.12").assertIsDisplayed()
    }
}