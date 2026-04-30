package edu.hcvs.calltwseapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.hcvs.calltwseapi.ui.StockListScreen
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme
import edu.hcvs.calltwseapi.viewmodel.MainViewModel

// 主畫面
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels() // 使用 viewModels() 委託來獲取 ViewModel 實例

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CallTwseApiTheme {
                StockListScreen(viewModel = viewModel) // 傳入 ViewModel
            }
        }
    }
}

// 預覽
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    CallTwseApiTheme {
        StockListScreen(viewModel = viewModel())
    }
}

