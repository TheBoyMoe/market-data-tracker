package com.example.marketdatatracker.ui.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.model.Stock;
import com.example.marketdatatracker.model.StockDataCache;
import com.example.marketdatatracker.ui.StockDetailActivity;

/**
 * References
 * [1] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s03.html
 * [2] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s04.html
 * [3] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s05.html
 */

public class StockViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private Stock mStock;
    private Context mContext;

    TextView mStockName;
    TextView mStockExchange;
    TextView mStockSymbol;
    TextView mStockPrice;
    TextView mStockChange;
    TextView mStockChangeInPercent;
    TextView mStockDayHi;
    TextView mStockDayLo;


    // enable the view holder to handle click events & cache references to the view's elements
    public StockViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);

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
        mStock = stock;
        mContext = context;

        // determine currency
        String currencySymbol;
        String currency = mStock.getCurrency();

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
        if (mStock.getPrice().doubleValue() > mStock.getPreviousClose().doubleValue()) {
            changeSymbol = "+";
            mStockChange.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));
            mStockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_green_18dp, 0, 0, 0);
            mStockChangeInPercent.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));
        }
        else if (mStock.getPrice().doubleValue() < mStock.getPreviousClose().doubleValue()) {
            changeSymbol = "";
            mStockChange.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light));
            mStockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_red_18dp, 0, 0, 0);
            mStockChangeInPercent.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light));
        }
        else {
            changeSymbol = "";
            mStockChange.setPadding(18, 0, 0, 0);
        }

        // populate the holder elements
        mStockName.setText(mStock.getName());
        mStockExchange.setText(mStock.getStockExchange());
        mStockSymbol.setText(mStock.getSymbol());
        mStockPrice.setText(String.format("%s%.2f", currencySymbol, mStock.getPrice()));
        mStockChange.setText(String.format("%s%.2f", changeSymbol, mStock.getChange()));
        mStockChangeInPercent.setText(String.format("%s%.2f", changeSymbol, mStock.getChangeInPercent()));
        mStockDayHi.setText(String.valueOf(mStock.getDayHigh()));
        mStockDayLo.setText(String.valueOf(mStock.getDayLow()));
    }


    // handle click events
    @Override
    public void onClick(View view) {
        // launch the StockDetailActivity passing in the stock symbol to id stock
        Intent intent = new Intent(mContext, StockDetailActivity.class);
        intent.putExtra(StockDataCache.STOCK_OBJECT, mStock.getSymbol());
        mContext.startActivity(intent);
    }


}
