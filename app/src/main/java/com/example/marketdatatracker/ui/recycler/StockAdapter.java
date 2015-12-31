package com.example.marketdatatracker.ui.recycler;

import android.content.Context;
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
    private StockViewHolder mStockViewHolder;

    public StockAdapter(List<Stock> stocks, Context context) {
        mStocks = stocks;
        mContext = context;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.stock_item, parent, false);
        mStockViewHolder = new StockViewHolder(view);

        return mStockViewHolder;
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        Stock mStock = mStocks.get(position);
        mStockViewHolder.bindStock(mStock, mContext);
    }

    @Override
    public int getItemCount() {
        return mStocks.size();
    }


}
