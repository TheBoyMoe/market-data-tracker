package com.example.marketdatatracker.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marketdatatracker.event.FetchStockQuoteEvent;
import com.example.marketdatatracker.service.GetStockQuoteThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;
import timber.log.Timber;
import yahoofinance.Stock;

public class StockFragment extends Fragment{

    private HashMap<String, Stock> mQuotes;
    private List<Stock> mStocks = new ArrayList<>();

    public StockFragment() {}

    public static StockFragment newInstance() {
        return new StockFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new GetStockQuoteThread().start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

        // TODO set up the view

    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


    @SuppressWarnings("unused")
    public void onEventMainThread(FetchStockQuoteEvent event) {

        // convert a HashMap of Stock objects into an ArrayList
        mQuotes = (HashMap<String, Stock>) event.getQuotes();

        Set<String> keys = mQuotes.keySet();
        String[] symbols = keys.toArray(new String[keys.size()]);
        for (String symbol : symbols) {
            Stock stock = mQuotes.get(symbol);
            mStocks.add(stock);
        }
        Timber.i(mStocks.toString());
        // TODO populate the adapter

    }


    // TODO create a custom adapter and view holder to display the data

}
