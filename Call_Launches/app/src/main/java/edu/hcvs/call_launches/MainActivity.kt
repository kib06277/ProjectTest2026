package edu.hcvs.call_launches

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import edu.hcvs.call_launches.model.LaunchesData
import edu.hcvs.call_launches.viewmodel.MainViewModel
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val viewModel: MainViewModel = viewModel()
            val launchesList by viewModel.LaunchesList.collectAsState()

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Button(
                        onClick = {
                            viewModel.getLaunchesList()
                        }
                    ) {
                        Text("Get LaunchesList")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn {
                        items(launchesList) { launches ->
                            LaunchesListItem(launches)
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LaunchesListItem(launches: LaunchesData) {

    // UTC 轉本地時間
    val formattedDate = try {
        val zonedDateTime = ZonedDateTime.parse(launches.date_utc)
        zonedDateTime
            .withZoneSameInstant(ZoneId.systemDefault())
            .format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            )
    } catch (e: Exception) {
        "時間格式錯誤"
    }

    // success 狀態處理
    val statusText: String
    val statusColor: Color
    when (launches.success) {
        true -> {
            statusText = "成功"
            statusColor = Color(0xFF4CAF50)
        }
        false -> {
            statusText = "失敗"
            statusColor = Color.Red
        }
        null -> {
            statusText = "尚未執行"
            statusColor = Color.Gray
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 任務名稱
            Text(
                text = launches.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            // 任務圖片
            AsyncImage(
                model = launches.links.patch.small,
                contentDescription = "Mission Patch",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 發射時間
            Text(text = "Launch Date : $formattedDate")
            Spacer(modifier = Modifier.height(8.dp))

            // 成功狀態
            Box(
                modifier = Modifier
                    .background(
                        color = statusColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = statusText,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 任務描述
            Text(
                text = launches.details ?: "無任務描述",
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}