package edu.hcvs.calltwseapi

import androidx.compose.ui.graphics.Color
import edu.hcvs.calltwseapi.ui.changeColor
import edu.hcvs.calltwseapi.ui.theme.GreenValueColor
import edu.hcvs.calltwseapi.ui.theme.ValueTextColor
import kotlin.test.Test
import kotlin.test.assertEquals

// 測試 changeColor 函式
class ChangeColorTest {
    @Test
    fun `正值應返回紅色`() {
        val result = changeColor("+10.00")
        assertEquals(Color(0xFFCC0000), result)
    }

    @Test
    fun `零應返回紅色`() {
        val result = changeColor("0.00")
        assertEquals(Color(0xFFCC0000), result)
    }

    @Test
    fun `負值應返回綠色`() {
        val result = changeColor("-10.00")
        assertEquals(GreenValueColor, result)
    }

    @Test
    fun `無效字串應返回預設顏色`() {
        val result = changeColor("-")
        assertEquals(ValueTextColor, result)
    }

    @Test
    fun `空字串應返回預設顏色`() {
        val result = changeColor("")
        assertEquals(ValueTextColor, result)
    }

    @Test
    fun `含逗號的正值應正確解析`() {
        val result = changeColor("+1,000.00")
        assertEquals(Color(0xFFCC0000), result)
    }

    @Test
    fun `含逗號的負值應正確解析`() {
        val result = changeColor("-1,000.00")
        assertEquals(GreenValueColor, result)
    }
}