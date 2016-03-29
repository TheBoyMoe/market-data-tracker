package com.example.marketdatatracker.ui.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.StockDataCache;
import com.example.marketdatatracker.network.GetStockChartThread;
import com.example.marketdatatracker.util.Constants;

import de.greenrobot.event.EventBus;

public class StockChartFragment extends Fragment implements View.OnClickListener{

    private static final String INTERVAL = "5d";
    private String mSymbol;
    private ImageView mStockChart;
    private ProgressBar mProgressBar;
    private View mView;
    private RadioGroup mRadioGroupVertical;
    private RadioGroup mRadioGroupHorizontal;
    private RadioButton mButton_1d;
    private RadioButton mButton_5d;
    private RadioButton mButton_1m;
    private RadioButton mButton_3m;
    private RadioButton mButton_1y;
    private RadioButton mButton_5y;

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

        // setup the radio buttons
        setupRadioButtons();
        mProgressBar = (ProgressBar) mView.findViewById(R.id.progress_bar);
        mStockChart = (ImageView) mView.findViewById(R.id.stock_chart);

        mProgressBar.setVisibility(View.VISIBLE);
        mStockChart.setVisibility(View.INVISIBLE);

        // fetch the stock obj from the data cache & download the
        mSymbol = getArguments().getString(StockDataCache.STOCK_OBJECT);
        if(savedInstanceState == null) {
            new GetStockChartThread(mSymbol, INTERVAL).start();
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            setStockChart();
        }

        return mView;
    }


    private void setupRadioButtons() {
        mButton_1d = (RadioButton) mView.findViewById(R.id.stock_1d);
        mButton_5d = (RadioButton) mView.findViewById(R.id.stock_5d);
        mButton_1m = (RadioButton) mView.findViewById(R.id.stock_1m);
        mButton_3m = (RadioButton) mView.findViewById(R.id.stock_3m);
        mButton_1y = (RadioButton) mView.findViewById(R.id.stock_1y);
        mButton_5y = (RadioButton) mView.findViewById(R.id.stock_5y);

        mButton_1d.setOnClickListener(this);
        mButton_5d.setOnClickListener(this);
        mButton_1m.setOnClickListener(this);
        mButton_3m.setOnClickListener(this);
        mButton_1y.setOnClickListener(this);
        mButton_5y.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stock_1d:
                new GetStockChartThread(mSymbol, "1d").start();
                break;
            case R.id.stock_5d:
                new GetStockChartThread(mSymbol, "5d").start();
                break;
            case R.id.stock_1m:
                new GetStockChartThread(mSymbol, "1m").start();
                break;
            case R.id.stock_3m:
                new GetStockChartThread(mSymbol, "3m").start();
                break;
            case R.id.stock_1y:
                new GetStockChartThread(mSymbol, "1y").start();
                break;
            case R.id.stock_5y:
                new GetStockChartThread(mSymbol, "5y").start();
                break;
        }
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

        if(message.equals(Constants.STOCK_DOWNLOAD_COMPLETE)) {
            setStockChart();
        } else if(message.equals(Constants.STOCK_DOWNLOAD_FAILED)) {
            Snackbar.make(mView, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void setStockChart() {
        Drawable chart = StockDataCache.getStockDataCache().getStock(mSymbol).getPriceChart();
        mStockChart.setImageDrawable(chart);
        mStockChart.setVisibility(View.VISIBLE);
    }

}
