package com.example.marketdatatracker.ui.recycler;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.model.Stock;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder>{

    private List<Stock> mStocks;
    private Context mContext;

    public StockAdapter(List<Stock> stocks, Context context) {
        mStocks = stocks;
        mContext = context;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.stock_item, parent, false);

        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        Stock stock = mStocks.get(position);

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
            holder.mStockChange.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));
            holder.mStockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_green_18dp, 0, 0, 0);
            holder.mStockChangeInPercent.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_green_light));
        }
        else if (stock.getPrice().doubleValue() < stock.getPreviousClose().doubleValue()) {
            changeSymbol = "";
            holder.mStockChange.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light));
            holder.mStockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_red_18dp, 0, 0, 0);
            holder.mStockChangeInPercent.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light));
        }
        else {
            changeSymbol = "";
            holder.mStockChange.setPadding(18, 0, 0, 0);
        }

        // populate the holder elements
        holder.mStockName.setText(stock.getName());
        holder.mStockExchange.setText(stock.getStockExchange());
        holder.mStockSymbol.setText(stock.getSymbol());
        holder.mStockPrice.setText(String.format("%s%.2f", currencySymbol, stock.getPrice()));
        holder.mStockChange.setText(String.format("%s%.2f", changeSymbol, stock.getChange()));
        holder.mStockChangeInPercent.setText(String.format("%s%.2f", changeSymbol, stock.getChangeInPercent()));
        holder.mStockDayHi.setText(String.valueOf(stock.getDayHigh()));
        holder.mStockDayLo.setText(String.valueOf(stock.getDayLow()));

    }

    @Override
    public int getItemCount() {
        return mStocks.size();
    }


}
