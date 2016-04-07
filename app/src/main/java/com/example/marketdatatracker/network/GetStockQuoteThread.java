package com.example.marketdatatracker.network;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.preference.PreferenceManager;

import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.StockItem;
import com.example.marketdatatracker.model.data.StockDataCache;
import com.example.marketdatatracker.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
    private SharedPreferences mPrefs;

    public GetStockQuoteThread(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void run() {
        super.run();
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        if(!isInterrupted()) {

            synchronized (this) {
                mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            }

            List<StockItem> stockItemList = new ArrayList<>();
            StockItem stockItem;

            // empty array list should nothing be found in shared preferences
            Set<String> defaultPortfolio = new HashSet<>();
            defaultPortfolio.addAll(new ArrayList<String>());

            // fetch user preferences, otherwise pass in the default
            Set<String> temp = mPrefs.getStringSet(Constants.PREFS_STOCK_PORTFOLIO_SET, defaultPortfolio);
            Set<String> portfolio = new TreeSet<>(temp); // sort
            String[] symbols = portfolio.toArray(new String[portfolio.size()]);

            if (symbols.length == 0) {
                // if the returned string set is empty, post a message to the user
                EventBus.getDefault().post(new AppMessageEvent(Constants.STOCK_PORTFOLIO_NOT_DEFINED));
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
                        stockItemList.add(stockItem);
                    }

                    // stash the data to the cache, let any interested parties know
                    StockDataCache.getStockDataCache().setStocks(stockItemList);
                    EventBus.getDefault().post(new AppMessageEvent(Constants.STOCK_DOWNLOAD_COMPLETE));

                } else
                    EventBus.getDefault().post(new AppMessageEvent(Constants.STOCK_DOWNLOAD_FAILED));

            } catch (IOException e) {
                Timber.e(e, "Failed to retrieve quote data: %s", e.getMessage());
                EventBus.getDefault().post(new AppMessageEvent(Constants.STOCK_DOWNLOAD_FAILED));
            }
        }
    }


    private StockItem buildCustomStockItem(Stock stock) {

        // convert BigDecimal to longs, Realm does NOT support BigDecimal (or java.util.Calendar)
        return new StockItem(
                stock.getName(),
                stock.getCurrency(),
                stock.getSymbol(),
                stock.getStockExchange(),
                stock.getQuote().getPrice().doubleValue(),
                stock.getQuote().getAvgVolume(),
                stock.getQuote().getVolume(),
                stock.getQuote().getDayHigh().longValue(),
                stock.getQuote().getDayLow().longValue(),
                stock.getQuote().getYearHigh().longValue(),
                stock.getQuote().getYearLow().longValue(),
                stock.getQuote().getOpen().longValue(),
                stock.getQuote().getPreviousClose().longValue(),
                stock.getStats().getMarketCap().longValue(),
                stock.getStats().getEps().longValue(),
                stock.getQuote().getChange().longValue(),
                stock.getQuote().getChangeInPercent().longValue(),
                stock.getDividend().getAnnualYield().longValue(),
                stock.getDividend().getAnnualYieldPercent().longValue()
        );

    }

}
