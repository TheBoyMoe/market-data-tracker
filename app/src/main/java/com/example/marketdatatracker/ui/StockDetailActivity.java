package com.example.marketdatatracker.ui;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.StockItem;
import com.example.marketdatatracker.model.data.StockDataCache;
import com.example.marketdatatracker.custom.CustomViewPagerAdapter;
import com.example.marketdatatracker.util.Constants;
import com.example.marketdatatracker.util.Utils;
import com.viewpagerindicator.CirclePageIndicator;

public class StockDetailActivity extends BaseActivity {

    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_detail_container);

        String symbol = getIntent().getStringExtra(StockDataCache.STOCK_OBJECT);
        StockItem stockItem = StockDataCache.getStockDataCache().getStock(symbol);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        // set the actionbar title
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);


        // display stockItem overview data
        //displayStockOverview();

        // setup the detail and graph fragments
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        if(mViewPager == null) {
            // device >= 600dp, display the detail & graph fragments simultaneously
            if(getSupportFragmentManager().findFragmentById(R.id.frame_left) == null) {
                FragmentPagerAdapter adapter = new CustomViewPagerAdapter(getSupportFragmentManager(), stockItem.getSymbol());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_left, adapter.getItem(0))
                        .add(R.id.frame_right, adapter.getItem(1))
                        .commit();
            }

        } else {
            // on a phone, use ViewPager and ViewPageIndicator
            CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.page_indicator);

            // set the page indicator and adapter
            mViewPager.setAdapter(new CustomViewPagerAdapter(getSupportFragmentManager(), stockItem.getSymbol()));
            mIndicator.setViewPager(mViewPager);

            // set and display the page indicator
            final float density = getResources().getDisplayMetrics().density;
            mIndicator.setRadius(6 * density);
            mIndicator.setStrokeColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            mIndicator.setFillColor(ContextCompat.getColor(this, R.color.colorPrimary));
            mIndicator.setStrokeWidth(1.4f * density);
            mIndicator.setPageColor(ContextCompat.getColor(this, android.R.color.white));
            mIndicator.setSnap(true);

            // disable updating activity title to display the current fragment loaded
//            mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//                // update the page title to reflect the change in fragment displayed
//                @Override
//                public void onPageSelected(int position) {
//
//                    if (getSupportActionBar() != null) {
//                        switch (position) {
//                            case CustomViewPagerAdapter.STOCK_DETAIL_FRAGMENT:
//                                getSupportActionBar().setTitle(R.string.stock_detail_title);
//                                break;
//                            case CustomViewPagerAdapter.STOCK_GRAPH_FRAGMENT:
//                                getSupportActionBar().setTitle(R.string.stock_graph_title);
//                                break;
//                            default:
//                                getSupportActionBar().setTitle(R.string.stock_detail_title);
//                        }
//                    }
//                }
//
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                    // no-op
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//                    // no-op
//                }
//
//            });

        }

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
        switch (message){
            case Constants.FAILED_TO_CONNECT:
                Utils.showSnackbar(mCoordinatorLayout, Constants.FAILED_TO_CONNECT);
            case Constants.SERVER_ERROR:
                Utils.showSnackbar(mCoordinatorLayout, Constants.SERVER_ERROR);
                break;
        }
    }


}
