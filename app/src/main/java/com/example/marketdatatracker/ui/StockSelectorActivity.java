package com.example.marketdatatracker.ui;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.ui.fragments.StockSelectorFragment;
import com.example.marketdatatracker.ui.fragments.StockSelectorModelFragment;
import com.example.marketdatatracker.util.Constants;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class StockSelectorActivity extends AppCompatActivity{

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

        mStockSelectorFragment = (StockSelectorFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
        if(mStockSelectorFragment == null) {
            mStockSelectorFragment = StockSelectorFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mStockSelectorFragment)
                    .commit();
        }

        mModelFragment = (StockSelectorModelFragment) getFragmentManager().findFragmentByTag(DATA_MODEL);
        if(mModelFragment == null) {
            mModelFragment = StockSelectorModelFragment.newInstatnce();
            getFragmentManager().beginTransaction()
                    .add(mModelFragment, DATA_MODEL)
                    .commit();
        }
        Timber.i("onCreate: Selector fragment: %s, model fragment: %s", mStockSelectorFragment, mModelFragment);
        updateDataModel();

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
            // TODO additional messages with regards to symbol download
        }
    }

    private void updateDataModel() {
        Timber.i("Data model update called");
        Timber.i("Selector fragment: %s, model fragment: %s", mStockSelectorFragment, mModelFragment);
        if(mStockSelectorFragment != null && mModelFragment != null)
            mStockSelectorFragment.setDataModel(mModelFragment.getDataModel());
    }

}
