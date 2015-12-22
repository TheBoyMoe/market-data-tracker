package com.example.marketdatatracker.service;


import android.util.Log;

import java.io.IOException;
import java.util.Map;

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
            for (String str : query) {
                Stock stock = stocks.get(str);
                Log.d("THREAD", stock.getName() + " price " + stock.getQuote().getPrice());
            }

        } catch (IOException e) {
            // TODO post error message to the bus
            Log.e("THREAD", "Failed to retrieve quote data");
        }
    }


}
