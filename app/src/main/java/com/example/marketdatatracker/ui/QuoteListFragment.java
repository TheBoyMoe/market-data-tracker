package com.example.marketdatatracker.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marketdatatracker.event.FetchStockQuoteEvent;
import com.example.marketdatatracker.service.GetQuoteThread;

import java.util.Map;

import de.greenrobot.event.EventBus;
import yahoofinance.Stock;

public class QuoteListFragment extends Fragment{

    private Map<String, Stock> mQuotes;

    public QuoteListFragment() {}

    public static QuoteListFragment newInstance() {
        return new QuoteListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new GetQuoteThread().start();
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
        EventBus.getDefault().post(this);
    }

    public void onEventMainThread(FetchStockQuoteEvent event) {
        mQuotes = event.getQuotes();

        // TODO populate the adapter
    }


    // TODO create a custom adapter and view holder to display the data

}
