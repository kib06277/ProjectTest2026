package edu.hcvs.calltwseapi

import edu.hcvs.calltwseapi.model.BWIBBUData
import edu.hcvs.calltwseapi.model.STOCK_DAY_ALL_Data
import edu.hcvs.calltwseapi.model.STOCK_DAY_AVG_ALL_Data

// 單元測試共用的假資料產生函式

// 建立一筆個股日資料
fun stockDay(
    code: String,
    closing: String = "100.00",
    change: String = "0.00",
    volume: String = "0",
    value: String = "0",
    transaction: String = "0"
) = STOCK_DAY_ALL_Data(
    Change = change, ClosingPrice = closing, Code = code, Date = "20250501",
    HighestPrice = closing, LowestPrice = closing, Name = "股票$code",
    OpeningPrice = closing, TradeValue = value, TradeVolume = volume, Transaction = transaction
)

// 建立一筆個股日平均價資料
fun stockAvg(code: String, avg: String) = STOCK_DAY_AVG_ALL_Data(
    ClosingPrice = "0", Code = code, Date = "20250501", MonthlyAveragePrice = avg, Name = "股票$code"
)

// 建立一筆本益比 / 殖利率 / 股價淨值比資料
fun bwibbu(code: String, pe: String = "10.00") = BWIBBUData(
    Code = code, Date = "20250501", DividendYield = "1.00", Name = "股票$code", PBratio = "1.00", PEratio = pe
)
