package com.example.marketdatatracker.ui;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.MenuItem;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.ui.fragments.StockSelectorFragment;
import com.example.marketdatatracker.ui.fragments.StockSelectorModelFragment;
import com.example.marketdatatracker.util.Constants;
import com.example.marketdatatracker.util.Utils;

import timber.log.Timber;

public class StockSelectorActivity extends BaseActivity{

    private static final String DATA_MODEL = "data_model";
    private StockSelectorFragment mStockSelectorFragment;
    private StockSelectorModelFragment mModelFragment;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_selector);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mStockSelectorFragment = (StockSelectorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(mStockSelectorFragment == null) {
            mStockSelectorFragment = StockSelectorFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mStockSelectorFragment)
                    .commit();
        }

        mModelFragment = (StockSelectorModelFragment) getSupportFragmentManager().findFragmentByTag(DATA_MODEL);
        if(mModelFragment == null) {
            mModelFragment = StockSelectorModelFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(mModelFragment, DATA_MODEL)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ensure the fragments are attached and the adapter created
        // ensures view is maintained between device rotations
        updateDataModel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onEventMainThread(AppMessageEvent event) {
        String message = event.getMessage();
        switch (message) {
            case Constants.STOCK_SYMBOL_DATA_MODEL_UPDATED:
                // update the stock selector fragments data model
                updateDataModel();
                break;
            case Constants.NO_MATCHING_RECORDS_FOUND:
                showUserMessage(Constants.NO_MATCHING_RECORDS_FOUND);
                break;
            case Constants.NO_RECORDS_FOUND:
                showUserMessage(Constants.NO_RECORDS_FOUND);
                break;
            case Constants.SERVER_ERROR:
                showUserMessage(Constants.SERVER_ERROR);
                break;
            case Constants.FAILED_TO_CONNECT:
                showUserMessage(Constants.FAILED_TO_CONNECT);
                break;
            case Constants.CHECK_NETWORK_CONNECTION:
                showUserMessage(Constants.CHECK_NETWORK_CONNECTION);
                break;
        }
    }

    private void updateDataModel() {
        if(mStockSelectorFragment != null && mModelFragment != null) {
            Timber.i("updateDataModel() called");
            mStockSelectorFragment.setDataModel(mModelFragment.getDataModel());
        }
    }

    private void showUserMessage(String message) {
        Utils.showSnackbar(mCoordinatorLayout, message);
    }

}
