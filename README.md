# ProjectTest

Kotlin + Jetpack Compose 的 Android 練習專案集合。主要練習以 Retrofit / OkHttp3 呼叫 API，並比較 MVC / MVP / MVVM / MVI 四種架構的寫法。

## 技術棧

| 類別 | 使用技術 |
|---|---|
| 語言 | Kotlin |
| UI | Jetpack Compose、Material 3 |
| 網路 | Retrofit、OkHttp3（HttpLoggingInterceptor）、Gson |
| 非同步 | Coroutine、Flow / StateFlow |
| 架構 | MVC、MVP、MVVM、MVI |
| 測試 | JUnit、MockK、Turbine、kotlinx-coroutines-test |
| 規劃中 | Room、Hilt |

## 專案列表

| 專案 | 內容 | 架構 |
|---|---|---|
| [CallApi](./CallApi) | 呼叫站點列表 API（GET）與登入 API（POST，JSON Body / FormUrlEncoded） | MVVM |
| [CallNewsListApi](./CallNewsListApi) | 呼叫臺灣證券交易所 OpenAPI，取得新聞列表 | MVVM |
| [Call_Launches](./Call_Launches) | 呼叫 SpaceX API 取得發射紀錄，用 Coil 載入圖片 | MVVM |
| [CallTwseApi](./CallTwseApi) | 臺灣證券交易所 OpenAPI 個股資訊：個股日成交、月平均價、本益比 / 殖利率 / 股價淨值比，支援排序與詳細資訊對話框 | MVVM |
| [CallTwseApi_MVC](./CallTwseApi_MVC) | 同 CallTwseApi，改寫為 MVC | MVC |
| [CallTwseApi_MVP](./CallTwseApi_MVP) | 同 CallTwseApi，改寫為 MVP | MVP |
| [CallTwseApi_MVI](./CallTwseApi_MVI) | 同 CallTwseApi，改寫為 MVI | MVI |

## CallTwseApi 四種架構比較

四個版本的畫面與功能完全相同，差別只在狀態放在哪裡、事件交給誰處理。每個架構版本的資料夾內都有 `ARCHITECTURE.md`，說明資料流與各檔案的職責。

| 架構 | 狀態放在哪 | 畫面如何更新 | 使用者操作交給誰 | 螢幕轉向後 |
|---|---|---|---|---|
| MVVM | `MainViewModel`（3 個 StateFlow） | 畫面收集 StateFlow | 直接呼叫 ViewModel 函式 | ViewModel 保留資料，但畫面仍會重打 API |
| MVC | `StockModel` | 畫面註冊 Listener 觀察 Model | `StockController` 修改 Model | 重新載入 |
| MVP | `StockListPresenter` | Presenter 呼叫 View 介面方法推送 | `StockListPresenter` | 重新載入 |
| MVI | 單一 `StockListState` | 畫面收集唯一的 State | `dispatch(Intent)` → Reducer | 保留 State，不重打 API |

三個新架構版本共用以下改動：

- 排序邏輯抽成純函式 `model/StockSorter`，`SortOption` 移到 model 層
- 無狀態畫面 `ui/StockListContent` 由三版共用
- applicationId 分別加上 `.mvc` / `.mvp` / `.mvi`，可以和原版同時安裝在同一台裝置上比較

## 開發環境

- Android Studio，Gradle JDK 請使用 **JDK 21**（JBR 21 或 Temurin 21）。Gradle 8.13 不支援 JDK 25。
- 每個資料夾都是獨立的 Android 專案，請在 Android Studio 中分別開啟。
