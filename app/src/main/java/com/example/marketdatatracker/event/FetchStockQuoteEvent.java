package com.example.marketdatatracker.event;

import java.util.Map;

import yahoofinance.Stock;

public class FetchStockQuoteEvent {

    private Map<String, Stock> mQuotes;

    public FetchStockQuoteEvent(Map<String, Stock> quotes) {
        this.mQuotes = quotes;
    }

    public Map<String, Stock> getQuotes() {
        return mQuotes;
    }
}
