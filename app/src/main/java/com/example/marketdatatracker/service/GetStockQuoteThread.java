package com.example.marketdatatracker.service;


import android.content.Context;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.StockDataCache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;
import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * References:
 * [1] http://financequotes-api.com/
 * [2] http://financequotes-api.com/javadoc/yahoofinance/YahooFinance.html
 */

public class GetStockQuoteThread extends Thread{

    private Context mContext;

    public GetStockQuoteThread(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        super.run();

        // TODO check shared preferences for stock choices - pass into query

        try {
            // read the symbol codes from the xml array
            /*List<String> symbolList = Arrays.asList(mContext.getApplicationContext().getResources().getStringArray(R.array.symbols_array));
            String[] query = symbolList.toArray(new String[symbolList.size()]);
            Timber.i("QUERY: %s", Arrays.toString(query));*/


            // query multiple stocks
            String[] query = {"INTC", "AAPL", "BABA", "TSLA", "AIR.PA", "YHOO", "BP", "BT"};
            Map<String, Stock> stocks = YahooFinance.get(query);
            Stock stock;

            List<com.example.marketdatatracker.model.Stock> stockList = new ArrayList<>();
            com.example.marketdatatracker.model.Stock stockItem;

            if (stocks != null) {

                // create a custom stock object for every stock object received
                Set<String> keys = stocks.keySet();
                String[] symbols = keys.toArray(new String[keys.size()]);
                for (String symbol : symbols) {
                    stock = stocks.get(symbol);
                    stockItem = buildCustomStockItem(stock);
                    Timber.i("Stock: %s", stockItem);
                    stockList.add(stockItem);
                }

                // stash the data to the cache, let any interested parties know
                StockDataCache.getStockDataCache().setStocks(stockList);
                EventBus.getDefault().post(new AppMessageEvent(AppMessageEvent.STOCK_DOWNLOAD_COMPLETE));


                /*for (String symbol : query) {
                    stock = stocks.get(symbol);
                    name = stock.getName();
                    names.add(name);
                }*/

                // print the name list to the log
                //Timber.i("Quote names: %s", names);

            } else
                EventBus.getDefault().post(new AppMessageEvent(AppMessageEvent.STOCK_DOWNLOAD_FAILED));

        } catch (IOException e) {
            Timber.e(e, "Failed to retrieve quote data: %s", e.getMessage());
            EventBus.getDefault().post(new AppMessageEvent(AppMessageEvent.STOCK_DOWNLOAD_FAILED));
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
