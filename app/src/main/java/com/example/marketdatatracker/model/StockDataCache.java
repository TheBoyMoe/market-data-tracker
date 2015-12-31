package com.example.marketdatatracker.model;

import java.util.List;

/**
 * Singleton used to hold the stock data
 * Reference:
 * [1] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch07s03.html
 * [2] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09.html
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

    public Stock getStock(String symbol) {
        if(symbol != null) {
            for (Stock stock : mStocks) {
                if(stock.getSymbol().equals(symbol)) {
                    return stock;
                }
            }
        }
        return null;
    }


}
