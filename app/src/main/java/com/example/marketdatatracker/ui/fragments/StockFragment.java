package com.example.marketdatatracker.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.event.FetchStockQuoteEvent;
import com.example.marketdatatracker.model.Stock;
import com.example.marketdatatracker.service.GetStockQuoteThread;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;


public class StockFragment extends Fragment{

    private static final String STOCK_LIST = "stock_list";
    private List<Stock> mStocks;
    private ListView mListView;
    private StockAdapter mStockAdapter;

    public StockFragment() {}

    public static StockFragment newInstance() {
        return new StockFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new GetStockQuoteThread().start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.stock_list_view, container, false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stock stock = (Stock) parent.getItemAtPosition(position);
                // TODO post the stock obj to the EventBus
                EventBus.getDefault().post(new AppMessageEvent(String
                        .format("Clicked item %d, %s", position, stock.getName())));
            }
        });

        // restore the array adapter on device rotation
        if(savedInstanceState != null) {
            Timber.i("Stocks list is %s", mStocks);
            mStocks = savedInstanceState.getParcelableArrayList(STOCK_LIST);
            if(mStocks != null) {
                mStockAdapter = new StockAdapter(mStocks);
                mListView.setAdapter(mStockAdapter);
            }
        }

        return mListView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STOCK_LIST, (ArrayList<? extends Parcelable>) mStocks);
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
    public void onEventMainThread(FetchStockQuoteEvent event) {

        // TODO fetch the stocks from persistent storage instead of posting/fetching from eventbus
        // display the stock data
        mStocks = event.getStocks();
        if(mStocks != null) {
            mStockAdapter = new StockAdapter(mStocks);
            mListView.setAdapter(mStockAdapter);
        }
    }


    // custom adapter and view holder pattern displays the data
    private class StockAdapter extends ArrayAdapter<Stock> {

        public StockAdapter(List<Stock> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.stock_item, parent, false);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            if(holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            // populate the holder elements
            Stock stock = getItem(position);
            holder.stockName.setText(stock.getName());
            holder.stockPrice.setText(String.valueOf(stock.getPrice()));

            return convertView;
        }
    }


    private class ViewHolder {
        TextView stockName;
        TextView stockPrice;

        public ViewHolder(View listItem) {
            stockName = (TextView) listItem.findViewById(R.id.stock_name);
            stockPrice = (TextView) listItem.findViewById(R.id.stock_price);
        }
    }


}
