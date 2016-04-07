package com.example.marketdatatracker.model.data;

import com.example.marketdatatracker.model.StockItem;

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
    private static List<StockItem> sStockItems;

    private StockDataCache() {  }

    public static StockDataCache getStockDataCache() {
        if(sStockDataCache == null) {
            sStockDataCache = new StockDataCache();
            sStockItems = new ArrayList<>();
        }
        return sStockDataCache;
    }


    public List<StockItem> getStocks() {
        return sStockItems;
    }

    public void setStocks(List<StockItem> stockItems) {
        sStockItems.clear();
        sStockItems.addAll(stockItems);
    }

    public StockItem getStock(String symbol) {
        if(symbol != null) {
            for (StockItem stockItem : sStockItems) {
                if(stockItem.getSymbol().equals(symbol)) {
                    return stockItem;
                }
            }
        }
        return null;
    }


}
