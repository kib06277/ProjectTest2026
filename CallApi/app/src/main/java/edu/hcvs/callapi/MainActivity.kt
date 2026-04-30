package edu.hcvs.callapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.hcvs.callapi.model.SiteModel
import edu.hcvs.callapi.viewmodel.MainViewModel

// 主活動，負責設置 UI 並與 ViewModel 交互
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel() // 獲取 ViewModel 實例
            val siteList by viewModel.siteList.collectAsState() // 觀察網站列表的狀態
            val loginResult by viewModel.loginResult.collectAsState() // 觀察登錄結果的狀態

            // UI 組件，使用 Material3 主題
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                // 使用 Column 來垂直排列按鈕和網站列表
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.getSiteList() // 調用 ViewModel 的方法來獲取網站列表
                        }
                    ) {
                        Text("Get Site List")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            viewModel.login() // 調用 ViewModel 的方法來執行登錄操作
                        }
                    ) {
                        Text("Login")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    // 顯示登錄結果，如果有的話
                    loginResult?.let {
                        Text(text = "Store Number : ${it.store_number}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Token : ${it.token}")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn {
                        items(siteList) { site ->
                            SiteItem(site)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SiteItem(site: SiteModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(text = "Name : ${site.name}")
            Text(text = "Host : ${site.host_name}")
            Text(text = "Site URL : ${site.site_url}")
            Text(text = "Vendor : ${site.type_live_third_party_vendor}")
        }
    }
}