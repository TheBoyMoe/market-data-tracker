package com.example.marketdatatracker.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.event.QueryStockSymbolsEvent;
import com.example.marketdatatracker.model.StockSymbol;
import com.example.marketdatatracker.util.Constants;
import com.example.marketdatatracker.util.Utils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class StockSelectorFragment extends BaseFragment{

    private List<StockSymbol> mSymbols = new ArrayList<>();
    private StockSymbolAdapter mAdapter;
    private ProgressBar mInProgress;

    public StockSelectorFragment() {}

    public static StockSelectorFragment newInstance() {
        return new StockSelectorFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.i("onAttach() called");
        mAdapter = new StockSymbolAdapter(mSymbols);
        Timber.i("Adapter: %s, symbolList: %d", mAdapter, mSymbols.size());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stock_selector_content, container, false);
        ListView suggestionList = (ListView) view.findViewById(R.id.suggestion_list);
        EditText suggestion = (EditText) view.findViewById(R.id.symbol_suggestion);
        mInProgress = (ProgressBar) view.findViewById(R.id.in_progress);
        if (isAdded())
            suggestionList.setAdapter(mAdapter);

        // add text change listener to edit text view
        suggestion.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Utils.isClientConnected(getActivity())) {
                    EventBus.getDefault().post(new QueryStockSymbolsEvent(s.toString()));
                    mInProgress.setVisibility(View.VISIBLE);

                } else {
                    EventBus.getDefault().post(new AppMessageEvent(Constants.CHECK_NETWORK_CONNECTION));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // no-op
            }

        });

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    // set the data model and refresh the adapter
    public void setDataModel(List<StockSymbol> list) {
        Timber.i("Setting the data model");
        updateUi(list);
        hideProgressbar();
    }

    public void onEventMainThread(AppMessageEvent event) {
        String message = event.getMessage();
        switch (message) {
            case Constants.THREAD_TASK_COMPLETE:
                hideProgressbar();
                break;
            case Constants.NO_MATCHING_RECORDS_FOUND:
            case Constants.NO_RECORDS_FOUND:
                updateUi(new ArrayList<StockSymbol>());
                break;
        }
    }

    private void updateUi(List<StockSymbol> list) {
        Timber.i("UpdateUi() called");
        if (mAdapter != null) {
            mAdapter.clear();
            mAdapter.addAll(list);
            mAdapter.notifyDataSetChanged();
            Timber.i("updating adapter: %s", mAdapter);
        }
    }

    private void hideProgressbar() {
        if (mInProgress.getVisibility() == View.VISIBLE)
            mInProgress.setVisibility(View.GONE);
    }

    // Custom array adapter and view holder
    private class StockSymbolAdapter extends ArrayAdapter<StockSymbol> {

        public StockSymbolAdapter(List<StockSymbol> list) {
            super(getActivity(), 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StockSymbolViewHolder viewHolder = null;

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.stock_symbol_list_item, null);
                viewHolder = (StockSymbolViewHolder) convertView.getTag();
            }

            if(viewHolder == null) {
                viewHolder = new StockSymbolViewHolder(convertView);
                convertView.setTag(viewHolder);
            }

            // bind the object to the view holder
            viewHolder.bindView(getItem(position));

            return convertView;
        }
    }

    private class StockSymbolViewHolder {
        TextView symbol;
        TextView exch;
        TextView name;

        StockSymbolViewHolder(View row) {
            symbol = (TextView) row.findViewById(R.id.stock_symbol);
            exch = (TextView) row.findViewById(R.id.stock_exch);
            name = (TextView) row.findViewById(R.id.stock_name);
        }

        void bindView(StockSymbol item) {
            symbol.setText(item.getSymbol());
            exch.setText(item.getExch());
            name.setText(item.getName());
        }
    }


}
