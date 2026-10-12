# CallTwseApi_MVP

## 資料流
```
使用者操作 ─▶ View (MainActivity) ─▶ Presenter (StockListPresenter) ─▶ Repository
                     ▲                         │
                     └──── showXxx()/hideXxx() ┘   （Presenter 主動推送）
```

## 檔案對應
| 角色 | 檔案 | 職責 |
|---|---|---|
| Contract | `presenter/StockListContract.kt` | 以介面定義 View 與 Presenter 能互相呼叫的方法 |
| Presenter | `presenter/StockListPresenter.kt` | 持有所有畫面邏輯與狀態，打 API、排序，再命令 View 顯示 |
| View | `ui/MainActivity.kt` | 實作 `Contract.View`，完全被動，只把指令存成 Compose state |
| Model | `repository/`、`network/`、`model/` | 資料來源（未變動） |
| 共用 | `model/StockSorter.kt`、`ui/StockListContent.kt` | 排序純函式、無狀態畫面 |

## 與原本 MVVM 的差異
- View 不觀察任何東西，所有顯示都是 Presenter **呼叫 View 介面方法**推送。
- Presenter 是純 Kotlin 類別，測試時直接用 MockK 建假 View 驗證「有沒有下對指令」。
- `onDestroy` 時 `detachView()` 取消進行中的請求、避免洩漏 Activity。
