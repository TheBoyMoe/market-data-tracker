package com.example.marketdatatracker.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;

import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.event.FetchStockSymbolsEvent;
import com.example.marketdatatracker.model.StockSymbol;
import com.example.marketdatatracker.util.Constants;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class StockSelectorModelFragment extends Fragment{

    private List<StockSymbol> mDataModel = new ArrayList<>();
    private boolean mIsStarted;

    public StockSelectorModelFragment() {}

    public static StockSelectorModelFragment newInstatnce() {
        return new StockSelectorModelFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public List<StockSymbol> getDataModel() {
        return new ArrayList<>(mDataModel);
    }


    public void onEventMainThread(FetchStockSymbolsEvent event) {
        // replace the current data model
        mDataModel.clear();
        mDataModel.addAll(event.getList());
        Timber.i("Data model updated: %s", mDataModel);
        EventBus.getDefault().post(new AppMessageEvent(Constants.STOCK_SYMBOL_DATA_MODEL_UPDATED));
    }

}
