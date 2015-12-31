package com.example.marketdatatracker.ui.recycler;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.model.Stock;

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


    // populate the viewHolder
    public void bindStock(Stock stock, Context context) {

        // determine currency
        String currencySymbol;
        String currency = stock.getCurrency();

        switch (currency) {
            case "EUR":
                currencySymbol = "€";
                break;
            case "USD":
                currencySymbol = "$";
                break;
            default:
                currencySymbol = "£";
        }

        // comparing closing and current prices to determine +/- change
        String changeSymbol;
        if (stock.getPrice().doubleValue() > stock.getPreviousClose().doubleValue()) {
            changeSymbol = "+";
            mStockChange.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
            mStockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_green_18dp, 0, 0, 0);
            mStockChangeInPercent.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
        }
        else if (stock.getPrice().doubleValue() < stock.getPreviousClose().doubleValue()) {
            changeSymbol = "";
            mStockChange.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
            mStockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_red_18dp, 0, 0, 0);
            mStockChangeInPercent.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
        }
        else {
            changeSymbol = "";
            mStockChange.setPadding(18, 0, 0, 0);
        }

        // populate the holder elements
        mStockName.setText(stock.getName());
        mStockExchange.setText(stock.getStockExchange());
        mStockSymbol.setText(stock.getSymbol());
        mStockPrice.setText(String.format("%s%.2f", currencySymbol, stock.getPrice()));
        mStockChange.setText(String.format("%s%.2f", changeSymbol, stock.getChange()));
        mStockChangeInPercent.setText(String.format("%s%.2f", changeSymbol, stock.getChangeInPercent()));
        mStockDayHi.setText(String.valueOf(stock.getDayHigh()));
        mStockDayLo.setText(String.valueOf(stock.getDayLow()));
    }


}
