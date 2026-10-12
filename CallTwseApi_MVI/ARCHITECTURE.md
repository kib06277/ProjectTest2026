# CallTwseApi_MVI

## 資料流（單向）
```
View ──Intent──▶ Store.dispatch() ──▶ Reducer(state, intent) ──▶ 新 State ──▶ View
                       │
                       └─ 副作用(打 API) ──Result──▶ Reducer(state, result) ──▶ 新 State
```

## 檔案對應
| 角色 | 檔案 | 職責 |
|---|---|---|
| Intent | `mvi/StockListIntent.kt` | 列出使用者所有可能的操作 |
| Result | `mvi/StockListResult.kt` | API 完成後的結果 |
| State (Model) | `mvi/StockListState.kt` | 整個畫面唯一的不可變狀態 |
| Reducer | `mvi/StockListReducer.kt` | 純函式：舊 State + Intent/Result → 新 State |
| Store | `mvi/StockListStore.kt` | 唯一入口 `dispatch()`、唯一出口 `state`；執行副作用 |
| View | `ui/StockListScreen.kt` | render(State)、送出 Intent |
| 共用 | `model/StockSorter.kt`、`ui/StockListContent.kt` | 排序純函式、無狀態畫面 |

## 與原本 MVVM 的差異
- 原本 3 個 StateFlow + 畫面內 3 個 `remember` 狀態 → 合併為 **1 個 `StockListState`**。
- 原本 3 個 public 函式 → 統一為 **`dispatch(Intent)`**。
- 狀態變化全集中在 Reducer，可不用協程、不用 Mock 直接測試。
- Store 以 Jetpack ViewModel 承載，轉向時保留 State，且不會重打 API。
