package com.example.marketdatatracker.network;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.Stock;
import com.example.marketdatatracker.model.StockDataCache;
import com.example.marketdatatracker.util.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.greenrobot.event.EventBus;
import timber.log.Timber;


/**
 *   References:
 *   [1] https://github.com/kevchou/SimpleStocks
 *   [2] https://github.com/udacity/Sunshine-Version-2/blob/2.07_build_url_with_params/app/src/main/java/com/example/android/sunshine/app/ForecastFragment.java
 */
public class GetStockChartThread extends Thread{

    /*
    * URL:
    * "http://chart.finance.yahoo.com/z?"
    *   + "s=" + symbol
        + "&t=" + interval      // Interval 1d, 5d, 3m, 6m, 1y, 2y, 5y
        + "&q=l"                // Chart type l, b, c (line/bar/candle)
        + "&l=on"               // Logarithmic scaling {on, off}
        + "&z=m"                // Size {m, l}
        + "&a=v"                // Volume
    * */

    private static final String BASE_URL = "http://chart.finance.yahoo.com/z?";

    private String mSymbol;
    private String mInterval;

    public GetStockChartThread(String symbol, String interval) {
        mSymbol = symbol;
        mInterval = interval;
    }

    @Override
    public void run() {
        super.run();
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        if(!isInterrupted()) {

            String chartType = "l";
            String scaling = "on";
            String size = "m";
            String volume = "v";

            HttpURLConnection urlConnection = null;

            try {

                try {
                    final String PRICE_CHART = "stock_chart";
                    final String SYMBOL_PARAM = "s";
                    final String INTERVAL_PARAM = "t";
                    final String CHART_TYPE_PARAM = "q";
                    final String SCALE_PARAM = "l";
                    final String SIZE_PARAM = "z";
                    final String VOLUME_PARAM = "a";

                    Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                            .appendQueryParameter(SYMBOL_PARAM, mSymbol)
                            .appendQueryParameter(INTERVAL_PARAM, mInterval)
                            .appendQueryParameter(CHART_TYPE_PARAM, chartType)
                            .appendQueryParameter(SCALE_PARAM, scaling)
                            .appendQueryParameter(SIZE_PARAM, size)
                            .appendQueryParameter(VOLUME_PARAM, volume)
                            .build();

                    URL url = new URL(builtUri.toString());

                    // open the connection and download the content
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream is = (InputStream) urlConnection.getContent();
                    Drawable chart = Drawable.createFromStream(is, PRICE_CHART);

                    // save the chart
                    if (chart != null) {
                        Stock stock = StockDataCache.getStockDataCache().getStock(mSymbol);
                        stock.setPriceChart(chart);
                        EventBus.getDefault().post(new AppMessageEvent(Constants.STOCK_DOWNLOAD_COMPLETE));
                    }

                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }

            } catch (IOException e) {
                Timber.e(e, "Error downloading stock chart: %s", e.getMessage());
                EventBus.getDefault().post(new AppMessageEvent(Constants.STOCK_DOWNLOAD_FAILED));
            }
        }

    }


}
