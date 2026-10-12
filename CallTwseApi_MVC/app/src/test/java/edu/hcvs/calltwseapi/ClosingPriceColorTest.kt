package edu.hcvs.calltwseapi

import org.junit.Assert.assertEquals
import org.junit.Test
import androidx.compose.ui.graphics.Color
import edu.hcvs.calltwseapi.ui.closingPriceColor
import edu.hcvs.calltwseapi.ui.theme.GreenValueColor
import edu.hcvs.calltwseapi.ui.theme.ValueTextColor

// 收盤價顏色測試
class ClosingPriceColorTest {

    @Test
    fun `收盤價高於月均價應返回紅色`() {
        val result = closingPriceColor("920.00", "915.00")
        assertEquals(Color(0xFFCC0000), result)
    }

    @Test
    fun `收盤價等於月均價應返回紅色`() {
        val result = closingPriceColor("915.00", "915.00")
        assertEquals(Color(0xFFCC0000), result)
    }

    @Test
    fun `收盤價低於月均價應返回綠色`() {
        val result = closingPriceColor("910.00", "915.00")
        assertEquals(GreenValueColor, result)
    }

    @Test
    fun `收盤價無效字串應返回預設顏色`() {
        val result = closingPriceColor("-", "915.00")
        assertEquals(ValueTextColor, result)
    }

    @Test
    fun `月均價無效字串應返回預設顏色`() {
        val result = closingPriceColor("920.00", "-")
        assertEquals(ValueTextColor, result)
    }

    @Test
    fun `含逗號的數值應正確解析`() {
        val result = closingPriceColor("1,000.00", "900.00")
        assertEquals(Color(0xFFCC0000), result)
    }
}