package com.example.marketdatatracker.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.ui.adapter.CustomViewPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import timber.log.Timber;

public class StockDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_detail_view);

        // cache the layout's elements
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.page_indicator);

        // set the page indicator and adapter
        mViewPager.setAdapter(new CustomViewPagerAdapter(getFragmentManager()));
        mIndicator.setViewPager(mViewPager);

        // set and display the page indicator
        final float density = getResources().getDisplayMetrics().density;
        mIndicator.setRadius(6 * density);
        mIndicator.setStrokeColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mIndicator.setFillColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mIndicator.setStrokeWidth(1.4f * density);
        mIndicator.setPageColor(ContextCompat.getColor(this, android.R.color.white));
        mIndicator.setSnap(true);

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // update the page title to reflect the change in fragment displayed
            @Override
            public void onPageSelected(int position) {

                if (getSupportActionBar() != null) {
                    switch (position) {
                        case CustomViewPagerAdapter.STOCK_DETAIL_FRAGMENT:
                            getSupportActionBar().setTitle(R.string.stock_detail_title);
                            break;
                        case CustomViewPagerAdapter.STOCK_GRAPH_FRAGMENT:
                            getSupportActionBar().setTitle(R.string.stock_graph_title);
                            break;
                        default:
                            getSupportActionBar().setTitle(R.string.stock_detail_title);
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // no-op
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // no-op
            }

        });
    }
}
