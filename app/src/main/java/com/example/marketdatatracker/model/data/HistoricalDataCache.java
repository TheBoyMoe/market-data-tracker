package com.example.marketdatatracker.model.data;

import com.example.marketdatatracker.model.historical.StockValues;

import java.util.ArrayList;
import java.util.List;

public class HistoricalDataCache {

    private static HistoricalDataCache sCache;
    private static List<StockValues> sList;

    private HistoricalDataCache() {}

    public static HistoricalDataCache getHistoricalDataCache() {
        if(sCache == null) {
            sCache = new HistoricalDataCache();
            sList = new ArrayList<>();
        }
        return sCache;
    }

    public List<StockValues> getHistoricalValues() {
        return sList;
    }

    public void setHistoricalValues(List<StockValues> list) {
        sList.clear();
        sList.addAll(list);
    }

    public StockValues getStockValues(String symbol) {
        if(symbol != null) {
            for(StockValues stock : sList) {
                if(stock.getSymbol().equals(symbol))
                    return stock;
            }
        }
        return null;
    }
}
