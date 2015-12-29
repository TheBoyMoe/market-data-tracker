package com.example.marketdatatracker.event;

import com.example.marketdatatracker.model.Stock;

import java.util.List;

public class FetchStockQuoteEvent {

    private List<Stock> mStocks;

    public FetchStockQuoteEvent(List<Stock> stocks) {
        mStocks = stocks;
    }

    public List<Stock> getStocks() {
        return mStocks;
    }


}
