package com.example.marketdatatracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton used to hold the stock data
 * Reference:
 * [1] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch07s03.html
 * [2] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09.html
 */
public class StockDataCache {

    public static final String STOCK_OBJECT = "stock";
    private static StockDataCache sStockDataCache;
    private static List<Stock> mStocks;

    private StockDataCache() {  }

    public static StockDataCache getStockDataCache() {
        if(sStockDataCache == null) {
            sStockDataCache = new StockDataCache();
            mStocks = new ArrayList<>();
        }
        return sStockDataCache;
    }


    public List<Stock> getStocks() {
        return mStocks;
    }

    public void setStocks(List<Stock> stocks) {
        mStocks.clear();
        mStocks.addAll(stocks);
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
