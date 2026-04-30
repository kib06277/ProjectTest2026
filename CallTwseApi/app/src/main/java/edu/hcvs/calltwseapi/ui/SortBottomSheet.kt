package edu.hcvs.calltwseapi.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme

// 排序選項
enum class SortOption(val label: String) {
    CLOSING_PRICE("收盤價"),
    CHANGE("漲跌價差"),
    TRADE_VOLUME("成交股數"),
    TRADE_VALUE("成交金額"),
    TRANSACTION("成交筆數")
}

// 排序選項底部彈窗
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    currentSort: SortOption,
    onSortSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "排序方式（降序）",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            SortOption.entries.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSortSelected(option)
                            onDismiss()
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentSort == option,
                        onClick = {
                            onSortSelected(option)
                            onDismiss()
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option.label, fontSize = 15.sp)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// 預覽
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SortBottomSheetPreview() {
    var currentSort by remember { mutableStateOf(SortOption.CLOSING_PRICE) }
    CallTwseApiTheme {
        SortBottomSheet(
            currentSort = currentSort,
            onSortSelected = { currentSort = it },
            onDismiss = {}
        )
    }
}