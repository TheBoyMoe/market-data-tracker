package com.example.marketdatatracker.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.service.GetStockQuoteThread;
import com.example.marketdatatracker.ui.fragments.AboutFragment;
import com.example.marketdatatracker.ui.fragments.CurrencyFragment;
import com.example.marketdatatracker.ui.fragments.NewsFragment;
import com.example.marketdatatracker.ui.fragments.SettingsFragment;
import com.example.marketdatatracker.ui.fragments.StockFragment;
import com.example.marketdatatracker.util.ScreenUtility;

import de.greenrobot.event.EventBus;
import timber.log.Timber;


/**
 *  References:
 *  [[1] https://guides.codepath.com/android/Fragment-Navigation-Drawer
 */
public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_TITLE = "current_title";
    private CoordinatorLayout mCoordinatorLayout;
    private DrawerLayout mDrawerLayout;
    private String mCurrentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            // fetch stock data
            //new GetStockQuoteThread(this).start();
        }

        // cache the req'd layout elements
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // setup the navigation drawer
        NavigationView navigationDrawer = (NavigationView) findViewById(R.id.navigation_drawer);
        setupDrawerContent(navigationDrawer);


        // setup the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigation_menu_white);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // set the initial fragment if first time in
        if(savedInstanceState == null)
            displayInitialFragment();
        else {
            // otherwise restore the title
            mCurrentTitle = savedInstanceState.getString(CURRENT_TITLE);
            setTitle(mCurrentTitle);
        }

        // setup the FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(mCoordinatorLayout, "Clicked FAB", Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, StockPrefsActivity.class));
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_TITLE, mCurrentTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_screen_dimensions:
                // determine the devices dimensions in dp
                ScreenUtility utility = new ScreenUtility(this);
                String output = String.format("Width: %d Height: %d",
                        (int)utility.getWidth(), (int)utility.getHeight());

                // display them on screen
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(output)
                        .setTitle("Dimensions")
                        .create()
                        .show();
                return true;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        // open the drawer on clicking the 'hamburger'
//        if(item.getItemId() == android.R.id.home) {
//            mDrawerLayout.openDrawer(GravityCompat.START);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    selectDrawerItem(item);
                    return true;
                }
            }
        );
    }


    private void selectDrawerItem(MenuItem item) {

        // select the fragment to instantiate based on the nav item clicked
        Fragment fragment = null;
        Class fragmentClass;

        switch(item.getItemId()) {
            case R.id.action_stock:
                fragmentClass = StockFragment.class;
                break;
            case R.id.action_currency:
                fragmentClass = CurrencyFragment.class;
                break;
            case R.id.action_news:
                fragmentClass = NewsFragment.class;
                break;
            case R.id.action_about:
                fragmentClass = AboutFragment.class;
                break;
            case R.id.action_settings:
                fragmentClass = SettingsFragment.class;
                break;
            default:
                fragmentClass = StockFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Timber.e(e, "Could not instantiate the %s", fragmentClass.getName());
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        item.setChecked(true);
        switch (item.getTitle().toString()) {
            case "Stocks":
                mCurrentTitle = "Current prices";
                break;
            case "Currencies":
                mCurrentTitle = "Foreign exchange";
                break;
            case "News":
                mCurrentTitle = "Financial headlines";
                break;
            case "About":
                mCurrentTitle = "App info";
                break;
            case "Settings":
                mCurrentTitle = "App settings";
                break;
            default:
                mCurrentTitle = "";
        }

        // on selecting the option, set the activity title and close the drawer
        setTitle(mCurrentTitle);
        mDrawerLayout.closeDrawers();

    }


    private void displayInitialFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, StockFragment.newInstance())
                .commit();
        mCurrentTitle = "Current prices";
        setTitle(mCurrentTitle);
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


    @SuppressWarnings("unused")
    public void onEventMainThread(AppMessageEvent event) {
        // display any posted messages to the user
        Snackbar.make(mCoordinatorLayout, event.getMessage(), Snackbar.LENGTH_LONG).show();
    }

}
