package com.example.marketdatatracker.ui.fragments;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.Stock;
import com.example.marketdatatracker.model.StockDataCache;
import com.example.marketdatatracker.ui.recycler.StockAdapter;

import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;


public class StockFragment extends Fragment{


    private List<Stock> mStocks;
    private RecyclerView mRecyclerView;
    private StockAdapter mStockAdapter;
    private ProgressBar mProgressBar;

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
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        // set the layout for the appropriate size
        Configuration config = getResources().getConfiguration();

        if(config.screenWidthDp >= 540) {
            // use a two col grid layout on all screens with a width >= 540dp
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }
        else {
            LinearLayoutManager linearLayoutMgr = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(linearLayoutMgr);
        }

        mRecyclerView.setHasFixedSize(true);

        // populate and bind the adapter to the view
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
        } else if(event.getMessage().equals(AppMessageEvent.STOCK_PORTFOLIO_NOT_DEFINED)) {
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private void updateUI() {
        mStocks = StockDataCache.getStockDataCache().getStocks();
        if(mStocks != null) {
            mStockAdapter = new StockAdapter(mStocks, getActivity());
            mRecyclerView.setAdapter(mStockAdapter);
            mProgressBar.setVisibility(View.GONE);
        }
    }



}
