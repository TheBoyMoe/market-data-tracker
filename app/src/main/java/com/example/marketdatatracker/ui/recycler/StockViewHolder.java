package com.example.marketdatatracker.ui.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.marketdatatracker.R;

public class StockViewHolder extends RecyclerView.ViewHolder{

    TextView mStockName;
    TextView mStockExchange;
    TextView mStockSymbol;
    TextView mStockPrice;
    TextView mStockChange;
    TextView mStockChangeInPercent;
    TextView mStockDayHi;
    TextView mStockDayLo;

    public StockViewHolder(View view) {
        super(view);

        mStockName = (TextView) view.findViewById(R.id.stock_name);
        mStockExchange = (TextView) view.findViewById(R.id.stock_exchange);
        mStockSymbol = (TextView) view.findViewById(R.id.stock_symbol);
        mStockPrice = (TextView) view.findViewById(R.id.stock_price);
        mStockChange = (TextView) view.findViewById(R.id.stock_change);
        mStockChangeInPercent = (TextView) view.findViewById(R.id.stock_change_in_percent);
        mStockDayHi = (TextView) view.findViewById(R.id.stock_day_hi);
        mStockDayLo = (TextView) view.findViewById(R.id.stock_day_lo);
    }
}
