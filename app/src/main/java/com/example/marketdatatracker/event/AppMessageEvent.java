package com.example.marketdatatracker.event;

public class AppMessageEvent {

    public static final String STOCK_DOWNLOAD_COMPLETE = "Stock download complete";
    public static final String STOCK_DOWNLOAD_FAILED = "Failed to retrieve stock data";
    public static final String STOCK_PORTFOLIO_NOT_DEFINED = "Create a portfolio of stocks to follow";
    public static final String STOCK_PORTFOLIO_HAS_CHANGED = "Stock portfolio has changed";

    private String mMessage;

    public AppMessageEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }


}
