package com.example.marketdatatracker.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.network.GetHistoricalDataThread;
import com.example.marketdatatracker.util.Constants;

public class StockLineGraphFragment extends Fragment implements View.OnClickListener{

    private String mSymbol;

    public StockLineGraphFragment() {}

    public static StockLineGraphFragment newInstance(String symbol) {

        StockLineGraphFragment fragment = new StockLineGraphFragment();
        Bundle args = new Bundle();
        args.putString(Constants.STOCK_SYMBOL_ITEM, symbol);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stock_line_graph, container, false);
        setupTimeFrameButtons(view);

        mSymbol = getArguments().getString(Constants.STOCK_SYMBOL_ITEM);
        if(savedInstanceState == null) {
            executeQuery(mSymbol, Constants.HISTORICAL_STOCK_QUERY_FOR_ONE_MONTH);
        }

        return view;
    }

    private void setupTimeFrameButtons(View view) {
        Button oneMonth = (Button) view.findViewById(R.id.time_frame_one_month);
        Button threeMonths = (Button) view.findViewById(R.id.time_frame_three_months);
        Button sixMonths = (Button) view.findViewById(R.id.time_frame_six_months);
        Button twelveMonths = (Button) view.findViewById(R.id.time_frame_twelve_months);

        oneMonth.setOnClickListener(this);
        threeMonths.setOnClickListener(this);
        sixMonths.setOnClickListener(this);
        twelveMonths.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.time_frame_one_month:
                executeQuery(mSymbol, Constants.HISTORICAL_STOCK_QUERY_FOR_ONE_MONTH);
                break;
            case R.id.time_frame_three_months:
                executeQuery(mSymbol, Constants.HISTORICAL_STOCK_QUERY_FOR_THREE_MONTH);
                break;
            case R.id.time_frame_six_months:
                executeQuery(mSymbol, Constants.HISTORICAL_STOCK_QUERY_FOR_SIX_MONTH);
                break;
            case R.id.time_frame_twelve_months:
                executeQuery(mSymbol, Constants.HISTORICAL_STOCK_QUERY_FOR_TWELVE_MONTH);
                break;
        }
    }

    private void executeQuery(String symbol, String timeFrame) {
        new GetHistoricalDataThread(symbol, timeFrame).start();
    }


}
