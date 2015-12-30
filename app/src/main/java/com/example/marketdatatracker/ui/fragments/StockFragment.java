package com.example.marketdatatracker.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.Stock;
import com.example.marketdatatracker.model.StockDataCache;
import com.example.marketdatatracker.ui.recycler.StockAdapter;

import java.util.List;

import de.greenrobot.event.EventBus;


public class StockFragment extends Fragment{


    private List<Stock> mStocks;
    private RecyclerView mRecyclerView;
    private StockAdapter mStockAdapter;

    public StockFragment() {}

    public static StockFragment newInstance() {
        return new StockFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stock_recycler_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
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


    @SuppressWarnings("unused")
    public void onEventMainThread(AppMessageEvent event) {
        // fetch data from the cache when confirmation of successful download received
        if(event.getMessage().equals(AppMessageEvent.STOCK_DOWNLOAD_COMPLETE)) {
            updateUI();
        }
    }


    private void updateUI() {
        mStocks = StockDataCache.getStockDataCache().getStocks();
        if(mStocks != null) {
            mStockAdapter = new StockAdapter(mStocks, getActivity());
            mRecyclerView.setAdapter(mStockAdapter);
        }
    }



}
