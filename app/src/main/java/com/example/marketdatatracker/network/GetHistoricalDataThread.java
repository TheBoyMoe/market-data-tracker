package com.example.marketdatatracker.network;


import com.example.marketdatatracker.util.Constants;

import timber.log.Timber;

public class GetHistoricalDataThread extends Thread{

    /**
            https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22YHOO%22%20
            and%20startDate%20%3D%20%222009-09-11%22%20and%20endDate%20%3D%20%222010-03-10%22&format=json&diagnostics=true
            &env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys

    */
    private static final String BASE_URL = "https://query.yahooapis.com/v1/public/yql?";
    private static final String QUERY = "q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22";
    private static final String FORMAT = "&format=json";
    private static final String DIAGNOSTICS = "&diagnostics=true";
    private static final String ENV = "&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    private String mSymbol;
    private String mTimeFrame;

    public GetHistoricalDataThread(String symbol, String timeFrame) {
        super("Historical data");
        mTimeFrame = timeFrame;
        mSymbol = symbol;
    }

    @Override
    public void run() {

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        if(!isInterrupted()) {
            Timber.i("Executing historical data thread");

            String timeFrameString = null;
            switch (mTimeFrame) {
                case Constants.HISTORICAL_STOCK_QUERY_FOR_ONE_MONTH:
                    timeFrameString = "%22%20and%20startDate%20%3D%20%222016-03-01%22%20and%20endDate%20%3D%20%222016-03-31%22";
                    break;
                case Constants.HISTORICAL_STOCK_QUERY_FOR_THREE_MONTH:
                    timeFrameString = "%22%20and%20startDate%20%3D%20%222016-01-01%22%20and%20endDate%20%3D%20%222016-03-31%22";
                    break;
                case Constants.HISTORICAL_STOCK_QUERY_FOR_SIX_MONTH:
                    timeFrameString = "%22%20and%20startDate%20%3D%20%222015-09-01%22%20and%20endDate%20%3D%20%222016-03-31%22";
                    break;
                case Constants.HISTORICAL_STOCK_QUERY_FOR_TWELVE_MONTH:
                    timeFrameString = "%22%20and%20startDate%20%3D%20%222015-04-01%22%20and%20endDate%20%3D%20%222016-03-31%22";
                    break;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(BASE_URL);
            sb.append(QUERY);
            sb.append(mSymbol);
            sb.append(timeFrameString);
            sb.append(FORMAT);
            sb.append(DIAGNOSTICS);
            sb.append(ENV);

            Timber.i("Url: %s", sb);

        }
    }
}
