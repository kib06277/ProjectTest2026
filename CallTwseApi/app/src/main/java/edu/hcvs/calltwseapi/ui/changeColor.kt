package edu.hcvs.calltwseapi.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import edu.hcvs.calltwseapi.ui.theme.GreenValueColor
import edu.hcvs.calltwseapi.ui.theme.ValueTextColor

// 漲跌價差顏色：正用紅，負用綠
@Composable
fun changeColor(change: String): Color {
    val value = change.replace(",", "").toDoubleOrNull() ?: return ValueTextColor
    return (if (value >= 0) Color(0xFFCC0000) else GreenValueColor)
}