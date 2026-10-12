package edu.hcvs.calltwseapi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme
import edu.hcvs.calltwseapi.ui.theme.SubtitleTextColor

// 股票詳細資訊對話框
@Composable
fun StockDetailDialog(
    stock: STOCK_DAY_ALL_Data,
    bwibbuData: BWIBBUData?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(text = stock.Name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = stock.Code, fontSize = 13.sp, color = SubtitleTextColor)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DialogInfoRow(label = "本益比", value = bwibbuData?.PEratio ?: "-")
                DialogInfoRow(label = "殖利率(%)", value = bwibbuData?.DividendYield ?: "-")
                DialogInfoRow(label = "股價淨值比", value = bwibbuData?.PBratio ?: "-")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("關閉") }
        }
    )
}

// 預覽
@Preview(showBackground = true)
@Composable
fun StockDetailDialogPreview() {
    val mockStock = STOCK_DAY_ALL_Data(
        Code = "2330",
        Name = "台積電",
        OpeningPrice = "910.00",
        ClosingPrice = "920.00",
        HighestPrice = "925.00",
        LowestPrice = "905.00",
        Change = "+10.00",
        TradeVolume = "25,000,000",
        TradeValue = "23,000,000,000",
        Transaction = "35,000",
        Date = "20250501"
    )
    val mockBwibbu = BWIBBUData(
        Code = "2330",
        Name = "台積電",
        Date = "20250501",
        PEratio = "22.53",
        DividendYield = "1.85",
        PBratio = "6.12"
    )
    CallTwseApiTheme {
        StockDetailDialog(
            stock = mockStock,
            bwibbuData = mockBwibbu,
            onDismiss = {}
        )
    }
}