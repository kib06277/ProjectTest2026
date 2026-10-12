# CallTwseApi_MVC

## 資料流
```
使用者操作 ─▶ View (StockListScreen) ─▶ Controller (StockController)
                     ▲                              │ 修改
                     │ 通知 (Listener)              ▼
                     └────────────────────── Model (StockModel)
```

## 檔案對應
| 角色 | 檔案 | 職責 |
|---|---|---|
| Model | `model/StockModel.kt` | 持有資料、載入 API、排序；用觀察者模式通知 View |
| Controller | `controller/StockController.kt` | 接收使用者操作，決定要對 Model 做什麼（只寫不讀） |
| View | `ui/StockListScreen.kt` | 註冊 Listener 觀察 Model；把操作轉給 Controller；自己管理彈窗開關等純畫面狀態 |
| 組裝 | `ui/MainActivity.kt` | 建立 Model 與 Controller（lifecycleScope） |
| 共用 | `model/StockSorter.kt`、`ui/StockListContent.kt` | 排序純函式、無狀態畫面 |

## 與原本 MVVM 的差異
- View **直接觀察 Model**（MVVM 是觀察 ViewModel 的 StateFlow）。
- Model / Controller 都是普通類別，不跨轉向存活；轉向後重新載入（與原專案 `LaunchedEffect(Unit)` 每次重建都重打 API 的行為一致）。
