package com.example.marketdatatracker.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.Stock;
import com.example.marketdatatracker.model.StockDataCache;
import com.example.marketdatatracker.network.GetStockQuoteThread;
import com.example.marketdatatracker.ui.recycler.StockAdapter;
import com.example.marketdatatracker.util.Constants;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * References
 *
 * RecyclerView and ModalMultiSelect
 * [1] https://www.bignerdranch.com/blog/recyclerview-part-1-fundamentals-for-listview-experts/
 * [2] https://www.bignerdranch.com/blog/recyclerview-part-2-choice-modes/
 * [3] https://github.com/bignerdranch/recyclerview-multiselect
 *
 */
public class StockFragment extends BaseFragment{

    private static final String TAG = "stock_fragment";
    private List<Stock> mStocks;
    private RecyclerView mRecyclerView;
    private StockAdapter mStockAdapter;
    private ProgressBar mProgressBar;

    private MultiSelector mMultiSelector = new MultiSelector();
    private ModalMultiSelectorCallback mSaveSelectionMode =
            new ModalMultiSelectorCallback(mMultiSelector) {

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    super.onCreateActionMode(actionMode, menu);
                    getActivity().getMenuInflater().inflate(R.menu.menu_remove, menu);
                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    if(item.getItemId() == R.id.action_remove) {
                        mode.finish();
                        List<Stock> removeList = new ArrayList<>();
                        Stock stock;
                        for (int i = 0; i < mStocks.size(); i++) {
                            if(mMultiSelector.isSelected(i, 0)) {
                                stock = mStocks.get(i);
                                removeList.add(stock);
                            }
                        }
                        // TODO confirmation dialog

                        // TODO iterate through list and remove items from the cache

                        Timber.i("Remove selection: %s", removeList);
                        mMultiSelector.clearSelections();
                        return true;
                    }
                    return false;
                }

            };

    public StockFragment() {}

    public static StockFragment newInstance() {
        return new StockFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if(savedInstanceState == null)
            new GetStockQuoteThread(getActivity()).start();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stock_recycler_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        // set the layout for the appropriate size
        Configuration config = getResources().getConfiguration();

        if(config.screenWidthDp >= 540) {
            // use a two col grid layout on all screens with a width >= 540dp
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }
        else {
            LinearLayoutManager linearLayoutMgr = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(linearLayoutMgr);
        }

        mRecyclerView.setHasFixedSize(true);

        // populate and bind the adapter to the view
        updateUI();

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        // restore the selection state on device rotation
        if (mMultiSelector != null) {
            if (savedInstanceState != null) {
                Bundle bundle = savedInstanceState.getBundle(TAG);
                if(bundle != null)
                mMultiSelector.restoreSelectionStates(bundle);
            }

            if (mMultiSelector.isSelectable()) {
                if (mSaveSelectionMode != null) {
                    mSaveSelectionMode.setClearOnPrepare(false);
                    ((AppCompatActivity) getActivity()).startSupportActionMode(mSaveSelectionMode);
                }
            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(TAG, mMultiSelector.saveSelectionStates());
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(AppMessageEvent event) {
        String message = event.getMessage();
        switch (message) {
            case Constants.STOCK_DOWNLOAD_COMPLETE:
                updateUI();
                break;
            case Constants.STOCK_PORTFOLIO_NOT_DEFINED:
                mProgressBar.setVisibility(View.GONE);
                break;
        }
    }


    private void updateUI() {
        mStocks = StockDataCache.getStockDataCache().getStocks();
        if(mStocks == null) {
            mStocks = new ArrayList<>();
        }
        mStockAdapter = new StockAdapter(mStocks, getActivity(), mMultiSelector, mSaveSelectionMode);
        mRecyclerView.setAdapter(mStockAdapter);
        mProgressBar.setVisibility(View.GONE);
    }


}
