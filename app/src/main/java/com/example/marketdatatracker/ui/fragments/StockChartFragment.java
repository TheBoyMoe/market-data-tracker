package com.example.marketdatatracker.ui.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.Stock;
import com.example.marketdatatracker.model.StockDataCache;
import com.example.marketdatatracker.network.GetStockChartThread;

import de.greenrobot.event.EventBus;

public class StockChartFragment extends Fragment{

    private static final String INTERVAL = "5d";
    private String mSymbol;
    private ImageView mStockChart;
    private ProgressBar mProgressBar;
    private View mView;

    public StockChartFragment() {}


    public static StockChartFragment newInstance(String symbol) {
        StockChartFragment fragment = new StockChartFragment();
        Bundle args = new Bundle();
        args.putString(StockDataCache.STOCK_OBJECT, symbol);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.stock_item_chart, container, false);

        mProgressBar = (ProgressBar) mView.findViewById(R.id.progress_bar);
        mStockChart = (ImageView) mView.findViewById(R.id.stock_chart);

        mProgressBar.setVisibility(View.VISIBLE);
        mStockChart.setVisibility(View.INVISIBLE);

        // fetch the stock obj from the data cache & download the
        mSymbol = getArguments().getString(StockDataCache.STOCK_OBJECT);
        new GetStockChartThread(mSymbol, INTERVAL).start();

        return mView;
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
        String message = event.getMessage();
        mProgressBar.setVisibility(View.GONE);

        if(message.equals(AppMessageEvent.STOCK_DOWNLOAD_COMPLETE)) {
            Drawable chart = StockDataCache.getStockDataCache().getStock(mSymbol).getPriceChart();
            mStockChart.setImageDrawable(chart);
            mStockChart.setVisibility(View.VISIBLE);
        } else if(message.equals(AppMessageEvent.STOCK_DOWNLOAD_FAILED)) {
            Snackbar.make(mView, message, Snackbar.LENGTH_SHORT).show();
        }
    }


}
