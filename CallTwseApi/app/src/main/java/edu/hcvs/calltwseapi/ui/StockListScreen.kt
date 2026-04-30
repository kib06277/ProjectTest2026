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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.ui.theme.BackgroundColor
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme
import edu.hcvs.calltwseapi.viewmodel.MainViewModel

// 個股列表畫面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(viewModel: MainViewModel) {

    val stockDayAllList by viewModel.STOCK_DAY_ALL_DataList.collectAsStateWithLifecycle()
    val stockDayAvgList by viewModel.STOCK_DAY_AVG_ALL_DataList.collectAsStateWithLifecycle()
    val bwibbuList by viewModel.BWIBBUDataList.collectAsStateWithLifecycle()

    val avgPriceMap = remember(stockDayAvgList) {
        stockDayAvgList.associateBy { it.Code }
    }
    val bwibbuMap = remember(bwibbuList) {
        bwibbuList.associateBy { it.Code }
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    var currentSort by remember { mutableStateOf(SortOption.CLOSING_PRICE) }
    var selectedStock by remember { mutableStateOf<STOCK_DAY_ALL_Data?>(null) }

    // 排序邏輯（預設降序）
    val sortedList = remember(stockDayAllList, currentSort) {
        stockDayAllList.sortedByDescending { stock ->
            when (currentSort) {
                SortOption.CLOSING_PRICE -> stock.ClosingPrice.replace(",", "").toDoubleOrNull() ?: 0.0
                SortOption.CHANGE        -> stock.Change.replace(",", "").toDoubleOrNull() ?: 0.0
                SortOption.TRADE_VOLUME  -> stock.TradeVolume.replace(",", "").toDoubleOrNull() ?: 0.0
                SortOption.TRADE_VALUE   -> stock.TradeValue.replace(",", "").toDoubleOrNull() ?: 0.0
                SortOption.TRANSACTION   -> stock.Transaction.replace(",", "").toDoubleOrNull() ?: 0.0
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getSTOCK_DAY_ALL_DataList()
        viewModel.getSTOCK_DAY_AVG_ALL_DataList()
        viewModel.getBWIBBUDataList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = { showBottomSheet = true }) {
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

        if (sortedList.isEmpty()) {
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
                items(items = sortedList, key = { it.Code }) { stock ->
                    val monthlyAvgPrice = avgPriceMap[stock.Code]?.MonthlyAveragePrice ?: "-"
                    StockCard(
                        stock = stock,
                        monthlyAveragePrice = monthlyAvgPrice,
                        onClick = { selectedStock = stock }
                    )
                }
            }
        }
    }

    // BottomSheet
    if (showBottomSheet) {
        SortBottomSheet(
            currentSort = currentSort,
            onSortSelected = { currentSort = it },
            onDismiss = { showBottomSheet = false }
        )
    }

    // Alert Dialog
    selectedStock?.let { stock ->
        StockDetailDialog(
            stock = stock,
            bwibbuData = bwibbuMap[stock.Code],
            onDismiss = { selectedStock = null }
        )
    }
}

// 預覽
@Preview(showBackground = true)
@Composable
fun StockListScreenPreview() {
    val viewModel: MainViewModel = viewModel()
    CallTwseApiTheme {
        StockListScreen(viewModel = viewModel)
    }
}