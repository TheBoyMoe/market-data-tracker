package com.example.marketdatatracker.ui.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.example.marketdatatracker.R;
import com.example.marketdatatracker.model.Stock;

import java.util.List;

/**
 * References
 * [1] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s03.html
 * [2] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s04.html
 * [3] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s05.html
 */
public class StockAdapter extends RecyclerView.Adapter<StockViewHolder>{

    private List<Stock> mStocks;
    private Context mContext;
    private MultiSelector mMultiSelector;
    private ModalMultiSelectorCallback mSaveSelectionMode;

    public StockAdapter(List<Stock> stocks, Context context, MultiSelector multiSelector, ModalMultiSelectorCallback saveSelectionMode) {
        mStocks = stocks;
        mContext = context;
        mMultiSelector = multiSelector;
        mSaveSelectionMode = saveSelectionMode;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.stock_item, parent, false);
        StockViewHolder holder = new StockViewHolder(view, mMultiSelector, mSaveSelectionMode);

        return holder;
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        Stock mStock = mStocks.get(position);
        holder.bindStock(mStock, mContext);
    }

    @Override
    public int getItemCount() {
        return mStocks.size();
    }


}
