package com.example.marketdatatracker.service;


import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.event.FetchStockQuoteEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;
import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class GetStockQuoteThread extends Thread{


    @Override
    public void run() {
        super.run();

        // TODO check shared preferences for stock choices - pass into query
        try {
            // query multiple stocks
            String[] query = {"INTC", "AAPL", "BABA", "TSLA", "AIR.PA", "YHOO"};
            Map<String, Stock> stocks = YahooFinance.get(query);
            Stock stock = null;

            List<com.example.marketdatatracker.model.Stock> stockList = new ArrayList<>();
            com.example.marketdatatracker.model.Stock stockItem;

            if(stocks != null) {

                // create a custom stock object for every stock object received
                Set<String> keys = stocks.keySet();
                String[] symbols = keys.toArray(new String[keys.size()]);
                for (String symbol : symbols) {
                    stock = stocks.get(symbol);
                    stockItem = buildCustomStockItem(stock);
                    Timber.i("Stock: %s", stockItem);
                    stockList.add(stockItem);
                }
                // TODO save the stocks to persistent storage
                // post the stock list
                EventBus.getDefault().post(new FetchStockQuoteEvent(stockList));
            }
            else
                EventBus.getDefault().post(new AppMessageEvent("Failed to retrieve quote data"));

        } catch (IOException e) {
            Timber.e(e, "Failed to retrieve quote data: %s", e.getMessage());
            EventBus.getDefault().post(new AppMessageEvent("Failed to retrieve quote data"));
        }
    }


    private com.example.marketdatatracker.model.Stock buildCustomStockItem(Stock stock) {

        return new com.example.marketdatatracker.model.Stock(
                stock.getName(),
                stock.getCurrency(),
                stock.getSymbol(),
                stock.getStockExchange(),
                stock.getQuote().getPrice(),
                stock.getQuote().getAvgVolume(),
                stock.getQuote().getVolume(),
                stock.getQuote().getDayHigh(),
                stock.getQuote().getDayLow(),
                stock.getQuote().getYearHigh(),
                stock.getQuote().getYearLow(),
                stock.getQuote().getOpen(),
                stock.getQuote().getPreviousClose(),
                stock.getQuote().getLastTradeTime(),
                stock.getStats().getMarketCap(),
                stock.getStats().getEps(),
                stock.getQuote().getChange(),
                stock.getQuote().getChangeInPercent()
        );
    }

}
