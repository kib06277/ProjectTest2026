package edu.hcvs.calltwseapi.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import edu.hcvs.calltwseapi.ui.theme.GreenValueColor
import edu.hcvs.calltwseapi.ui.theme.ValueTextColor

// 收盤價顏色：高於月均價用紅，低於用綠
@Composable
fun closingPriceColor(closingPrice: String, monthlyAvgPrice: String): Color {
    val closing = closingPrice.replace(",", "").toDoubleOrNull() ?: return ValueTextColor
    val avg = monthlyAvgPrice.replace(",", "").toDoubleOrNull() ?: return ValueTextColor
    return if (closing >= avg) Color(0xFFCC0000) else GreenValueColor
}