package com.example.marketdatatracker.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.event.QueryStockSymbolsEvent;
import com.example.marketdatatracker.model.suggestion.StockSymbol;
import com.example.marketdatatracker.util.Constants;
import com.example.marketdatatracker.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        final View view = inflater.inflate(R.layout.stock_selector_content, container, false);
        final ListView suggestionList = (ListView) view.findViewById(R.id.suggestion_list);
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

        // implement context action bar
        suggestionList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        suggestionList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // inflate the context resource, displayed in the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_add, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_addition) {
                    // retrieve the currently saved stock portfolio
                    Set<String> symbols = PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .getStringSet(Constants.PREFS_STOCK_PORTFOLIO_SET, null);

                    if (symbols == null) {
                        symbols = new HashSet<>();
                    }

                    String symbol;
                    // retrieve the symbol for each item selected and add to the symbol set
                    for (int i = 0; i < mAdapter.getCount(); i++) {
                        if (suggestionList.isItemChecked(i)) {
                            symbol = mAdapter.getItem(i).getSymbol();
                            symbols.add(symbol);
                        }
                    }

                    // save the symbol set to shared preferences
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                            .clear()
                            .putStringSet(Constants.PREFS_STOCK_PORTFOLIO_SET, symbols)
                            .apply();


                    mode.finish();
                    Utils.showSnackbar(view, Constants.STOCK_PORTFOLIO_UPDATED);
                }

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

        });

        return view;
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.stock_symbol_item, null);
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
