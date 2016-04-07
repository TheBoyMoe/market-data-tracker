package com.example.marketdatatracker.event;

import com.example.marketdatatracker.model.StockItem;

import java.util.List;

public class FetchStockQuoteEvent {

    private List<StockItem> mStockItems;

    public FetchStockQuoteEvent(List<StockItem> stockItems) {
        mStockItems = stockItems;
    }

    public List<StockItem> getStockItems() {
        return mStockItems;
    }


}
