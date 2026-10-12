package edu.hcvs.calltwseapi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme

// 成交量行
@Composable
fun StockVolumeRow(
    transaction: String,
    tradeVolume: String,
    tradeValue: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VolumeItem(label = "成交筆數", value = transaction)
        VolumeItem(label = "成交股數", value = tradeVolume)
        VolumeItem(label = "成交金額", value = tradeValue)
    }
}

// 預覽
@Preview(showBackground = true)
@Composable
fun StockVolumeRowPreview() {
    CallTwseApiTheme {
        StockVolumeRow(
            transaction = "35,000",
            tradeVolume = "25,000,000",
            tradeValue = "23,000,000,000"
        )
    }
}