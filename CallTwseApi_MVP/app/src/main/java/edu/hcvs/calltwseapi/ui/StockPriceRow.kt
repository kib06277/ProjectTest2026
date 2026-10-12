package edu.hcvs.calltwseapi.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme
import edu.hcvs.calltwseapi.ui.theme.GreenValueColor
import edu.hcvs.calltwseapi.ui.theme.LabelTextColor

// 價格行
@Composable
fun StockPriceRow(
    leftLabel: String, leftValue: String, leftColor: Color,
    rightLabel: String, rightValue: String, rightColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = leftLabel,  fontSize = 13.sp, color = LabelTextColor, modifier = Modifier.width(60.dp), textAlign = TextAlign.End)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = leftValue,  fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = leftColor,  modifier = Modifier.weight(1f))
        Text(text = rightLabel, fontSize = 13.sp, color = LabelTextColor, modifier = Modifier.width(60.dp), textAlign = TextAlign.End)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = rightValue, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = rightColor, modifier = Modifier.weight(1f))
    }
}

// 預覽
@Preview(showBackground = true)
@Composable
fun StockPriceRowPreview() {
    CallTwseApiTheme {
        StockPriceRow(
            leftLabel = "開盤價",
            leftValue = "910.00",
            leftColor = GreenValueColor,
            rightLabel = "收盤價",
            rightValue = "920.00",
            rightColor = Color(0xFFCC0000)
        )
    }
}