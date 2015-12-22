package com.example.marketdatatracker.event;

public class AppMessageEvent {

    private String mMessage;

    public AppMessageEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }


}
