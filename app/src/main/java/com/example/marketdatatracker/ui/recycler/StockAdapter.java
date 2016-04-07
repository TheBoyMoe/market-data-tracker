package com.example.marketdatatracker.ui.recycler;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.example.marketdatatracker.R;
import com.example.marketdatatracker.model.StockItem;

import java.util.List;

/**
 * References
 * [1] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s03.html
 * [2] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s04.html
 * [3] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch09s05.html
 */
public class StockAdapter extends RecyclerView.Adapter<StockViewHolder>{

    private List<StockItem> mStockItems;
    private Context mContext;
    private MultiSelector mMultiSelector;
    private ModalMultiSelectorCallback mSaveSelectionMode;

    public StockAdapter(List<StockItem> stockItems, Context context, MultiSelector multiSelector, ModalMultiSelectorCallback saveSelectionMode) {
        mStockItems = stockItems;
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
        StockItem mStockItem = mStockItems.get(position);
        holder.bindStock(mStockItem, mContext);
        // override multi-select library defaults
        if(Build.VERSION.SDK_INT >= 21) {
            holder.setSelectionModeStateListAnimator(null);
            holder.setDefaultModeStateListAnimator(null);
        }
        //holder.setDefaultModeBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.touch_selector));
    }

    @Override
    public int getItemCount() {
        return mStockItems.size();
    }


}
