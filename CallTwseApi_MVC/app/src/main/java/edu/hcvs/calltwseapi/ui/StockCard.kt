package edu.hcvs.calltwseapi.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme
import edu.hcvs.calltwseapi.ui.theme.CardBackgroundColor
import edu.hcvs.calltwseapi.ui.theme.GreenValueColor
import edu.hcvs.calltwseapi.ui.theme.SubtitleTextColor

// 個股卡片
@Composable
fun StockCard(
    stock: STOCK_DAY_ALL_Data,
    monthlyAveragePrice: String,
    onClick: () -> Unit
) {
    // 偵測螢幕方向
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        if (isLandscape) {
            // 橫向：左右兩欄排列
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // 左欄：名稱
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "(${stock.Code})", fontSize = 12.sp, color = SubtitleTextColor)
                    Text(
                        text = stock.Name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                    )
                    StockVolumeRow(
                        transaction = stock.Transaction,
                        tradeVolume = stock.TradeVolume,
                        tradeValue = stock.TradeValue
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                // 右欄：價格資訊
                Column(modifier = Modifier.weight(1f)) {
                    StockPriceRow(
                        leftLabel = "開盤價", leftValue = stock.OpeningPrice, leftColor = GreenValueColor,
                        rightLabel = "收盤價", rightValue = stock.ClosingPrice,
                        rightColor = closingPriceColor(stock.ClosingPrice, monthlyAveragePrice)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StockPriceRow(
                        leftLabel = "最高價", leftValue = stock.HighestPrice, leftColor = MaterialTheme.colorScheme.onSurface,
                        rightLabel = "最低價", rightValue = stock.LowestPrice, rightColor = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StockPriceRow(
                        leftLabel = "漲跌價差", leftValue = stock.Change, leftColor = changeColor(stock.Change),
                        rightLabel = "月平均價", rightValue = monthlyAveragePrice, rightColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            // 直向：原本排列
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(text = "(${stock.Code})", fontSize = 12.sp, color = SubtitleTextColor)
                Text(
                    text = stock.Name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                )
                StockPriceRow(
                    leftLabel = "開盤價", leftValue = stock.OpeningPrice, leftColor = GreenValueColor,
                    rightLabel = "收盤價", rightValue = stock.ClosingPrice,
                    rightColor = closingPriceColor(stock.ClosingPrice, monthlyAveragePrice)
                )
                Spacer(modifier = Modifier.height(6.dp))
                StockPriceRow(
                    leftLabel = "最高價", leftValue = stock.HighestPrice, leftColor = MaterialTheme.colorScheme.onSurface,
                    rightLabel = "最低價", rightValue = stock.LowestPrice, rightColor = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                StockPriceRow(
                    leftLabel = "漲跌價差", leftValue = stock.Change, leftColor = changeColor(stock.Change),
                    rightLabel = "月平均價", rightValue = monthlyAveragePrice, rightColor = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                StockVolumeRow(
                    transaction = stock.Transaction,
                    tradeVolume = stock.TradeVolume,
                    tradeValue = stock.TradeValue
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,  name = "Light")
@Composable
fun StockCardDarkPreview() {
    val mockStock = STOCK_DAY_ALL_Data(
        Code = "2330", Name = "台積電",
        OpeningPrice = "910.00", ClosingPrice = "920.00",
        HighestPrice = "925.00", LowestPrice = "905.00",
        Change = "+10.00", TradeVolume = "25,000,000",
        TradeValue = "23,000,000,000", Transaction = "35,000",
        Date = "20250501"
    )
    CallTwseApiTheme {
        StockCard(stock = mockStock, monthlyAveragePrice = "915.00", onClick = {})
    }
}