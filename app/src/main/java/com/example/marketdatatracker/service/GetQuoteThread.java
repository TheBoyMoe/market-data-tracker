package com.example.marketdatatracker.service;


import android.util.Log;

import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.event.FetchStockQuoteEvent;

import java.io.IOException;
import java.util.Map;

import de.greenrobot.event.EventBus;
import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class GetQuoteThread extends Thread{

    @Override
    public void run() {
        super.run();

        try {
            // single stock quote
            //Stock stock = YahooFinance.get("INTC");
            //Log.d("THREAD", stock.toString());

            // query multiple stocks
            String[] query = {"INTC", "AAPL", "BABA", "TSLA", "AIR.PA", "YHOO"};
            Map<String, Stock> stocks = YahooFinance.get(query);
            if(stocks != null) {
                EventBus.getDefault().post(new FetchStockQuoteEvent(stocks));
                for (String symbol : query) {
                    Timber.i("%s : %.2f", symbol, stocks.get(symbol).getQuote().getPrice());
                }
            }

            else
                EventBus.getDefault().post(new AppMessageEvent("Failed to retrieve quote data"));

        } catch (IOException e) {
            Timber.e(e, "Failed to retrieve quote data: %s", e.getMessage());
            EventBus.getDefault().post(new AppMessageEvent("Failed to retrieve quote data"));
        }
    }


}
