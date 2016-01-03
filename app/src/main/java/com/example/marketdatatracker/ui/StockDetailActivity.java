package com.example.marketdatatracker.ui;

import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.model.Stock;
import com.example.marketdatatracker.model.StockDataCache;
import com.example.marketdatatracker.ui.adapter.CustomViewPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import timber.log.Timber;

public class StockDetailActivity extends AppCompatActivity {

    private Stock mStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_detail_container);

        String symbol = getIntent().getStringExtra(StockDataCache.STOCK_OBJECT);
        mStock = StockDataCache.getStockDataCache().getStock(symbol);

        // set the actionbar title
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(mStock.getName());

        // display stock overview data
        //displayStockOverview();

        // setup the detail and graph fragments
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        if(mViewPager == null) {
            // device >= 600dp, display the detail & graph fragments simultaneously
            if(getFragmentManager().findFragmentById(R.id.frame_left) == null) {
                FragmentPagerAdapter adapter = new CustomViewPagerAdapter(getFragmentManager(), mStock.getSymbol());
                getFragmentManager().beginTransaction()
                        .add(R.id.frame_left, adapter.getItem(0))
                        .add(R.id.frame_right, adapter.getItem(1))
                        .commit();
            }

        } else {
            // on a phone, use ViewPager and ViewPageIndicator
            CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.page_indicator);

            // set the page indicator and adapter
            mViewPager.setAdapter(new CustomViewPagerAdapter(getFragmentManager(), mStock.getSymbol()));
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


//    private void displayStockOverview() {
//        TextView mStockName = (TextView) findViewById(R.id.stock_name);
//        TextView mStockExchange = (TextView) findViewById(R.id.stock_exchange);
//        TextView mStockSymbol = (TextView) findViewById(R.id.stock_symbol);
//        TextView mStockPrice = (TextView) findViewById(R.id.stock_price);
//        TextView mStockChange = (TextView) findViewById(R.id.stock_change);
//        TextView mStockChangeInPercent = (TextView) findViewById(R.id.stock_change_in_percent);
//        TextView mStockDayHi = (TextView) findViewById(R.id.stock_day_hi);
//        TextView mStockDayLo = (TextView) findViewById(R.id.stock_day_lo);
//
//        // determine currency
//        String currencySymbol;
//        String currency = mStock.getCurrency();
//
//        switch (currency) {
//            case "EUR":
//                currencySymbol = "€";
//                break;
//            case "USD":
//                currencySymbol = "$";
//                break;
//            default:
//                currencySymbol = "£";
//        }
//
//        String changeSymbol;
//        if(mStock.getPrice().doubleValue() > mStock.getPreviousClose().doubleValue())
//            changeSymbol = "+";
//        else
//            changeSymbol = "";
//
//        mStockName.setText(mStock.getName());
//        mStockExchange.setText(mStock.getStockExchange());
//        mStockSymbol.setText(mStock.getSymbol());
//        mStockPrice.setText(String.format("%s%.2f", currencySymbol, mStock.getPrice()));
//        mStockChange.setText(String.format("%s%.2f", changeSymbol, mStock.getChange()));
//        mStockChangeInPercent.setText(String.format("%s%.2f", changeSymbol, mStock.getChangeInPercent()));
//        mStockDayHi.setText(String.valueOf(mStock.getDayHigh()));
//        mStockDayLo.setText(String.valueOf(mStock.getDayLow()));
//    }


}
