package edu.hcvs.calltwseapi.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme
import edu.hcvs.calltwseapi.ui.theme.LabelTextColor
import edu.hcvs.calltwseapi.ui.theme.ValueTextColor

// 成交量項目
@Composable
fun VolumeItem(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = LabelTextColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "($value)",
            fontSize = 12.sp,
            color = ValueTextColor
        )
    }
}

// 預覽
@Preview(showBackground = true)
@Composable
fun VolumeItemPreview() {
    CallTwseApiTheme {
        VolumeItem(label = "成交筆數", value = "35,000")
    }
}