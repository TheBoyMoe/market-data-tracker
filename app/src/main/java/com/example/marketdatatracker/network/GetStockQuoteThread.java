package com.example.marketdatatracker.network;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.preference.PreferenceManager;

import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.StockDataCache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;
import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * References:
 * [1] http://financequotes-api.com/
 * [2] http://financequotes-api.com/javadoc/yahoofinance/YahooFinance.html
 */

public class GetStockQuoteThread extends Thread{

    private Context mContext;
    private SharedPreferences mPrefs;

    public GetStockQuoteThread(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void run() {
        super.run();
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        synchronized (this) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        }

        List<com.example.marketdatatracker.model.Stock> stockList = new ArrayList<>();
        com.example.marketdatatracker.model.Stock stockItem;

        Set<String> defaultPortfolio = new HashSet<>();
        defaultPortfolio.addAll(new ArrayList<String>());

        // fetch user preferences, otherwise pass in the default
        Set<String> portfolio = mPrefs.getStringSet("quote_list", defaultPortfolio);
        String[] symbols = portfolio.toArray(new String[portfolio.size()]);
        if(symbols.length == 0) {
            // if the returned string set is empty, post a message to the user
            EventBus.getDefault().post(new AppMessageEvent(AppMessageEvent.STOCK_PORTFOLIO_NOT_DEFINED));
            return;
        }

        try {
            // query the Yahoo Finance API
            Map<String, Stock> stocks = YahooFinance.get(symbols);
            Stock stock;

            if (stocks != null) {
                for (String symbol : symbols) {
                    stock = stocks.get(symbol);
                    stockItem = buildCustomStockItem(stock);
                    Timber.i("Stock: %s", stockItem);
                    stockList.add(stockItem);
                }

                // stash the data to the cache, let any interested parties know
                StockDataCache.getStockDataCache().setStocks(stockList);
                EventBus.getDefault().post(new AppMessageEvent(AppMessageEvent.STOCK_DOWNLOAD_COMPLETE));

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
                stock.getQuote().getChangeInPercent(),
                stock.getDividend().getAnnualYield(),
                stock.getDividend().getAnnualYieldPercent()
        );
    }

}
