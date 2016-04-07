package com.example.marketdatatracker.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.marketdatatracker.model.StockItem;
import com.example.marketdatatracker.model.data.StockDataCache;
import com.example.marketdatatracker.network.GetStockQuoteThread;
import com.example.marketdatatracker.custom.CustomItemDecoration;
import com.example.marketdatatracker.ui.recycler.StockAdapter;
import com.example.marketdatatracker.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * References
 *
 * RecyclerView and ModalMultiSelect
 * [1] https://www.bignerdranch.com/blog/recyclerview-part-1-fundamentals-for-listview-experts/
 * [2] https://www.bignerdranch.com/blog/recyclerview-part-2-choice-modes/
 * [3] https://github.com/bignerdranch/recyclerview-multiselect
 *
 * Pull-to-refresh
 * https://guides.codepath.com/android/Implementing-Pull-to-Refresh-Guide
 */
public class StockFragment extends BaseFragment{

    private static final String STOCK_FRAGMENT_TAG = "stock_fragment";
    private static final String DELETE_STOCK_DIALOG = "delete_stock_dialog";
    private List<StockItem> mStockItems;
    private RecyclerView mRecyclerView;
    private StockAdapter mStockAdapter;
    private ProgressBar mProgressBar;
    private boolean mRefreshing;
    private SwipeRefreshLayout mSwipeLayout;
    private View mView;

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
                        List<String> symbols = new ArrayList<>();
                        String symbol;
                        for (int i = 0; i < mStockItems.size(); i++) {
                            if(mMultiSelector.isSelected(i, 0)) {
                                symbol = mStockItems.get(i).getSymbol();
                                symbols.add(symbol);
                            }
                        }

                        // display confirmation dialog
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        DeleteStockItemsDialog deleteStockItemsDialog = DeleteStockItemsDialog.newInstance(symbols);
                        deleteStockItemsDialog.show(fm, DELETE_STOCK_DIALOG);

                        mMultiSelector.clearSelections();
                        mode.finish();
                        return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {
                    super.onDestroyActionMode(actionMode);
                    mMultiSelector.clearSelections();
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

        mView = inflater.inflate(R.layout.stock_recycler_view, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.progress_bar);
        mSwipeLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);

        // set the layout for the appropriate size
        Configuration config = getResources().getConfiguration();
        if(config.screenWidthDp >= 800) {
            GridLayoutManager gridLayoutManager = null;
            if(config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                // two col layout on screens width >= 800dp and in portrait
                gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                mRecyclerView.setLayoutManager(gridLayoutManager);
            } else {
                // else use a three col grid
                gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                mRecyclerView.setLayoutManager(gridLayoutManager);
            }
        }
        else if(config.screenWidthDp >= 540){
            // use a two col grid layout on all screens with a width >= 540dp
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutMgr = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(linearLayoutMgr);
        }

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new CustomItemDecoration(getResources().getDimensionPixelSize(R.dimen.list_item_spacer)));

        // update stock quotes on swipe
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshing = true;
                new GetStockQuoteThread(getActivity()).start();
            }
        });
        // set refresh colors
        mSwipeLayout.setColorSchemeResources(
                R.color.pullToFreshOne,
                R.color.pullToFreshTwo,
                R.color.pullToFreshThree,
                R.color.pullToFreshFour
        );

        // populate and bind the adapter to the view
        updateUI();

        return mView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        // restore the selection state on device rotation
        if (mMultiSelector != null) {
            if (savedInstanceState != null) {
                Bundle bundle = savedInstanceState.getBundle(STOCK_FRAGMENT_TAG);
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
        outState.putBundle(STOCK_FRAGMENT_TAG, mMultiSelector.saveSelectionStates());
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(AppMessageEvent event) {
        String message = event.getMessage();
        switch (message) {
            case Constants.CONFIRM_STOCK_ITEM_DELETION:
            case Constants.STOCK_DOWNLOAD_COMPLETE:
                if(mRefreshing) {
                    mRefreshing = false;
                    mSwipeLayout.setRefreshing(false);
                }
                updateUI();
                break;
            case Constants.STOCK_PORTFOLIO_NOT_DEFINED:
                mProgressBar.setVisibility(View.GONE);
                break;
        }
    }

    private void updateUI() {
        mStockItems = StockDataCache.getStockDataCache().getStocks();
        if(mStockItems == null) {
            mStockItems = new ArrayList<>();
        }
        mStockAdapter = new StockAdapter(mStockItems, getActivity(), mMultiSelector, mSaveSelectionMode);
        mRecyclerView.setAdapter(mStockAdapter);
        mProgressBar.setVisibility(View.GONE);
    }

}
