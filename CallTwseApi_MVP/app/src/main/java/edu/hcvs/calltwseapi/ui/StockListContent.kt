package edu.hcvs.calltwseapi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.SortOption
import edu.hcvs.calltwseapi.model.StockItem
import edu.hcvs.calltwseapi.ui.theme.BackgroundColor
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme

// 個股列表畫面（無狀態 Stateless）
// 只負責「把傳進來的資料畫出來」並把使用者操作往外丟，不持有任何資料、不做排序。
// 狀態從哪裡來、事件交給誰處理，由各架構（MVC / MVP / MVI）自行決定。
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListContent(
    stockItems: List<StockItem>,          // 已排序、已帶入月均價的列表
    currentSort: SortOption,              // 目前排序選項
    isSortSheetVisible: Boolean,          // 是否顯示排序彈窗
    selectedStock: STOCK_DAY_ALL_Data?,   // 目前點選的個股（null 表示不顯示對話框）
    selectedBwibbu: BWIBBUData?,          // 點選個股對應的本益比等資料
    onSortClick: () -> Unit,              // 點擊排序按鈕
    onSortSelected: (SortOption) -> Unit, // 選擇排序選項
    onSortDismiss: () -> Unit,            // 關閉排序彈窗
    onStockClick: (STOCK_DAY_ALL_Data) -> Unit, // 點擊個股卡片
    onDetailDismiss: () -> Unit           // 關閉詳細資訊對話框
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = onSortClick) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "排序",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->

        if (stockItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = stockItems, key = { it.stock.Code }) { item ->
                    StockCard(
                        stock = item.stock,
                        monthlyAveragePrice = item.monthlyAveragePrice,
                        onClick = { onStockClick(item.stock) }
                    )
                }
            }
        }
    }

    // BottomSheet
    if (isSortSheetVisible) {
        SortBottomSheet(
            currentSort = currentSort,
            onSortSelected = onSortSelected,
            onDismiss = onSortDismiss
        )
    }

    // Alert Dialog
    selectedStock?.let { stock ->
        StockDetailDialog(
            stock = stock,
            bwibbuData = selectedBwibbu,
            onDismiss = onDetailDismiss
        )
    }
}

// 預覽（使用假資料，不需要建立任何 ViewModel / Presenter / Controller）
@Preview(showBackground = true)
@Composable
fun StockListContentPreview() {
    val mockStock = STOCK_DAY_ALL_Data(
        Code = "2330", Name = "台積電",
        OpeningPrice = "910.00", ClosingPrice = "920.00",
        HighestPrice = "925.00", LowestPrice = "905.00",
        Change = "+10.00", TradeVolume = "25,000,000",
        TradeValue = "23,000,000,000", Transaction = "35,000",
        Date = "20250501"
    )
    CallTwseApiTheme {
        StockListContent(
            stockItems = listOf(StockItem(mockStock, "915.00")),
            currentSort = SortOption.CLOSING_PRICE,
            isSortSheetVisible = false,
            selectedStock = null,
            selectedBwibbu = null,
            onSortClick = {},
            onSortSelected = {},
            onSortDismiss = {},
            onStockClick = {},
            onDetailDismiss = {}
        )
    }
}
