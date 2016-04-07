package com.example.marketdatatracker.ui.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.example.marketdatatracker.R;
import com.example.marketdatatracker.model.StockItem;
import com.example.marketdatatracker.model.data.StockDataCache;
import com.example.marketdatatracker.ui.StockDetailActivity;

/**
 * References
 * [1] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s03.html
 * [2] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s04.html
 * [3] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s05.html
 */

public class StockViewHolder extends SwappingHolder implements View.OnClickListener, View.OnLongClickListener{

    private StockItem mStockItem;
    private Context mContext;
    private MultiSelector mMultiSelector;
    private ModalMultiSelectorCallback mSaveSelectionMode;

    TextView mStockName;
    TextView mStockExchange;
    TextView mStockSymbol;
    TextView mStockPrice;
    TextView mStockChange;
    TextView mStockChangeInPercent;
    TextView mStockDayHi;
    TextView mStockDayLo;


    // enable the view holder to handle click events & cache references to the view's elements
    public StockViewHolder(View view, MultiSelector multiSelector, ModalMultiSelectorCallback saveSelectionMode) {
        super(view, multiSelector);
        view.setOnClickListener(this);
        view.setLongClickable(true);
        view.setOnLongClickListener(this);
        mMultiSelector = multiSelector;
        mSaveSelectionMode = saveSelectionMode;

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
    public void bindStock(StockItem stockItem, Context context) {
        mStockItem = stockItem;
        mContext = context;

        // determine currency
        String currencySymbol;
        String currency = mStockItem.getCurrency();

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
        if (mStockItem.getPrice() > mStockItem.getPreviousClose()) {
            changeSymbol = "+";
            mStockChange.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));
            mStockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_green_18dp, 0, 0, 0);
            mStockChangeInPercent.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));
        }
        else if (mStockItem.getPrice() < mStockItem.getPreviousClose()) {
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
        mStockName.setText(mStockItem.getName());
        mStockExchange.setText(mStockItem.getStockExchange());
        mStockSymbol.setText(mStockItem.getSymbol());
        mStockPrice.setText(String.format("%s%.2f", currencySymbol, mStockItem.getPrice()));
        mStockChange.setText(String.format("%s%.2f", changeSymbol, mStockItem.getChange()));
        mStockChangeInPercent.setText(String.format("%s%.2f", changeSymbol, mStockItem.getChangeInPercent()));
        mStockDayHi.setText(String.valueOf(mStockItem.getDayHigh()));
        mStockDayLo.setText(String.valueOf(mStockItem.getDayLow()));
    }


    // handle click events
    @Override
    public void onClick(View view) {
        if(!mMultiSelector.tapSelection(this)) {
            // selection off, handle click as normal
            // launch the StockDetailActivity passing in the stock symbol to id stock
            Intent intent = new Intent(mContext, StockDetailActivity.class);
            intent.putExtra(StockDataCache.STOCK_OBJECT, mStockItem.getSymbol());
            mContext.startActivity(intent);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        // launch the contextual actionbar
        ((AppCompatActivity)mContext).startSupportActionMode(mSaveSelectionMode);
        mMultiSelector.setSelected(this, true);
        return true;
    }
}
