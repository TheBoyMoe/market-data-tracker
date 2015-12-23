package com.example.marketdatatracker.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.event.FetchStockQuoteEvent;
import com.example.marketdatatracker.service.GetQuoteThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;
import timber.log.Timber;
import yahoofinance.Stock;

public class QuoteListFragment extends Fragment{

    private HashMap<String, Stock> mQuotes;
    private List<Stock> mStocks = new ArrayList<>();

    public QuoteListFragment() {}

    public static QuoteListFragment newInstance() {
        return new QuoteListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new GetQuoteThread().start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().post(new AppMessageEvent("Executing quote thread"));
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

        // TODO populate the adapter

    }


    // TODO create a custom adapter and view holder to display the data

}
