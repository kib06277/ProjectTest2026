package edu.hcvs.calltwseapi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme
import edu.hcvs.calltwseapi.ui.theme.LabelTextColor

// 對話框資訊行
@Composable
fun DialogInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = LabelTextColor, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

// 預覽
@Preview(showBackground = true)
@Composable
fun DialogInfoRowPreview() {
    CallTwseApiTheme {
        DialogInfoRow(label = "本益比", value = "22.53")
    }
}