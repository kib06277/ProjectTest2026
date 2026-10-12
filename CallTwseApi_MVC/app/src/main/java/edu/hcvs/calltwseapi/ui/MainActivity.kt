package edu.hcvs.calltwseapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import edu.hcvs.calltwseapi.controller.StockController
import edu.hcvs.calltwseapi.model.StockModel
import edu.hcvs.calltwseapi.ui.StockListScreen
import edu.hcvs.calltwseapi.ui.theme.CallTwseApiTheme

// 主畫面（MVC 版）
// Activity 只負責「組裝」：建立 Model、建立 Controller、把兩者交給 View
// 預覽請使用 StockListContentPreview（ui/StockListContent.kt）
class MainActivity : ComponentActivity() {

    // Model：資料與商業邏輯
    private val model = StockModel()

    // Controller：使用 lifecycleScope，Activity 銷毀時自動取消進行中的請求
    private val controller by lazy { StockController(model, lifecycleScope) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CallTwseApiTheme {
                StockListScreen(model = model, controller = controller)
            }
        }
    }
}
