package com.example.marketdatatracker.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.model.Stock;
import com.example.marketdatatracker.model.StockDataCache;
import com.example.marketdatatracker.ui.StockDetailActivity;

import timber.log.Timber;

public class StockGraphFragment extends Fragment{

    private Stock mStock;

    public StockGraphFragment() {}


    public static StockGraphFragment newInstance(String symbol) {
        StockGraphFragment fragment = new StockGraphFragment();
        Bundle args = new Bundle();
        args.putString(StockDataCache.STOCK_OBJECT, symbol);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stock_item_graph, container, false);

        // fetch the stock obj from the data cache
        String symbol = getArguments().getString(StockDataCache.STOCK_OBJECT);
        mStock = StockDataCache.getStockDataCache().getStock(symbol);
        Timber.i("Graph fragment: %s", mStock.toString());

        return view;
    }
}
