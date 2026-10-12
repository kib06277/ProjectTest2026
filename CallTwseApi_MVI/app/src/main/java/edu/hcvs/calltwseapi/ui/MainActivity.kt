package edu.hcvs.calltwseapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import edu.hcvs.calltwseapi.mvi.StockListStore
import edu.hcvs.calltwseapi.ui.StockListScreen
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme

// 主畫面（MVI 版）
// Store 以 viewModels() 取得，轉向時 State 會保留，不會重新打 API
class MainActivity : ComponentActivity() {

    private val store: StockListStore by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CallTwseApiTheme {
                StockListScreen(store = store)
            }
        }
    }
}
