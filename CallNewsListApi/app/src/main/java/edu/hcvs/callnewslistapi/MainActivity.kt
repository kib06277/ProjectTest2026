package edu.hcvs.callnewslistapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.hcvs.callnewslistapi.model.NewsListModel
import edu.hcvs.callnewslistapi.viewmodel.MainViewModel

// 主活動，負責設置 UI 並與 ViewModel 交互
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel() // 獲取 ViewModel 實例
            val newsList by viewModel.newsList.collectAsState() // 觀察新聞列表的狀態

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
                            viewModel.getNewsList() // 調用 ViewModel 的方法來獲取新聞列表
                        }
                    ) {
                        Text("Get NewsList")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn {
                        items(newsList) { newsitem ->
                            NewsListItems(newsitem)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsListItems(newsitem: NewsListModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(text = "Title : ${newsitem.Title}")
            Text(text = "Url : ${newsitem.Url}")
            Text(text = "Date : ${newsitem.Date}")
        }
    }
}