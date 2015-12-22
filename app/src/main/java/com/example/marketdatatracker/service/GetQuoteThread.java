package com.example.marketdatatracker.service;


import android.util.Log;

import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.event.FetchStockQuoteEvent;

import java.io.IOException;
import java.util.Map;

import de.greenrobot.event.EventBus;
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
            if(stocks != null)
                EventBus.getDefault().post(new FetchStockQuoteEvent(stocks));
            else
                EventBus.getDefault().post(new AppMessageEvent("Failed to retrieve quote data"));

        } catch (IOException e) {
            Log.e("THREAD", "Failed to retrieve quote data");
            EventBus.getDefault().post(new AppMessageEvent("Failed to retrieve quote data"));
        }
    }


}
