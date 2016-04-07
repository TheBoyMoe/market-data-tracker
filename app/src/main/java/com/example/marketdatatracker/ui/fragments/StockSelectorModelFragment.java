package com.example.marketdatatracker.ui.fragments;


import android.os.Bundle;

import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.event.FetchStockSymbolsEvent;
import com.example.marketdatatracker.event.QueryStockSymbolsEvent;
import com.example.marketdatatracker.model.suggestion.StockSymbol;
import com.example.marketdatatracker.network.GetSymbolSuggestionThread;
import com.example.marketdatatracker.util.Constants;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class StockSelectorModelFragment extends BaseFragment{

    private List<StockSymbol> mDataModel = new ArrayList<>();
    private GetSymbolSuggestionThread mCurrentTask;
    private boolean mIsRunning;

    public StockSelectorModelFragment() {}

    public static StockSelectorModelFragment newInstance() {
        return new StockSelectorModelFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public List<StockSymbol> getDataModel() {
        Timber.i("Retrieving the data model");
        return new ArrayList<>(mDataModel);
    }

    public void onEventMainThread(QueryStockSymbolsEvent event) {
        if(mCurrentTask != null && mIsRunning) {
            mCurrentTask.interrupt();
        }
        mCurrentTask = new GetSymbolSuggestionThread("StockItem symbol", event.getQuery());
        mCurrentTask.start();
        mIsRunning = true;
    }


    public void onEventMainThread(FetchStockSymbolsEvent event) {
        // replace the current data model
        mDataModel.clear();
        mDataModel.addAll(event.getList());
        mIsRunning = false;
        Timber.i("Downloaded updated data model");
        EventBus.getDefault().post(new AppMessageEvent(Constants.STOCK_SYMBOL_DATA_MODEL_UPDATED));
    }

}
