package com.example.marketdatatracker.model;

import java.util.List;

/**
 * Singleton used to hold the stock data
 */
public class StockDataCache {

    private static StockDataCache sStockDataCache;
    private List<Stock> mStocks;

    private StockDataCache() {  }

    public static StockDataCache getStockDataCache() {
        if(sStockDataCache == null) {
            sStockDataCache = new StockDataCache();
        }
        return sStockDataCache;
    }


    public List<Stock> getStocks() {
        return mStocks;
    }

    public void setStocks(List<Stock> stocks) {
        mStocks = stocks;
    }


}
