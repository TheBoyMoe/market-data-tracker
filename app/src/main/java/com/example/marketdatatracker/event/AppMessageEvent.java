package com.example.marketdatatracker.event;

public class AppMessageEvent {

    public static final String STOCK_DOWNLOAD_COMPLETE = "Stock download complete";
    public static final String STOCK_DOWNLOAD_FAILED = "Failed to retrieve stock data";

    private String mMessage;

    public AppMessageEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }


}
