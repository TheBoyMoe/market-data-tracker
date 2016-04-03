package com.example.marketdatatracker.network;

import android.net.Uri;

import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.event.FetchStockSymbolsEvent;
import com.example.marketdatatracker.model.suggestion.StockSymbol;
import com.example.marketdatatracker.model.suggestion.SuggestionQuery;
import com.example.marketdatatracker.util.Constants;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class GetSymbolSuggestionThread extends Thread{

    // http://autoc.finance.yahoo.com/autoc?query=ab&callback=YAHOO.Finance.SymbolSuggest.ssCallback&region=US&lang=en-US
    private static final String BASE_URL = "http://autoc.finance.yahoo.com/autoc?";
    private static final String QUERY = "query";
    private static final String CALLBACK = "callback";
    private static final String REGION = "region";
    private static final String LANGUAGE = "lang";

    private String mQuery;

    public GetSymbolSuggestionThread(String threadName, String query) {
        super(threadName);
        mQuery = query;
    }

    @Override
    public void run() {

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        if(!isInterrupted()) {

            Timber.i("Executing %s thread", super.getName());
            String callback = "YAHOO.Finance.SymbolSuggest.ssCallback";
            String region = "US";
            String language = "en-US";

            // build suggestion query uri
            Uri querySuggestionUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY, mQuery)
                    .appendQueryParameter(CALLBACK, callback)
                    .appendQueryParameter(REGION, region)
                    .appendQueryParameter(LANGUAGE, language)
                    .build();

            // execute the connection and retrieve the data
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(querySuggestionUri.toString()).build();
                Timber.i("Request: %s", request);
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Timber.i("Response: %s", response);
                    Reader in = response.body().charStream();
                    BufferedReader reader = new BufferedReader(in);

                    // convert the resultant response into an object gson can parse
                    // 1. convert the reader into a string
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    // 2. strip out Yahoo string at beginning of the response
                    Pattern pattern = Pattern.compile("YAHOO\\.Finance\\.SymbolSuggest\\.ssCallback\\((\\{.*?\\})\\)");
                    Matcher matcher = pattern.matcher(sb);
                    if(matcher.find()){

                        SuggestionQuery suggestionQuery = new Gson().fromJson(matcher.group(1),SuggestionQuery.class);
                        List<StockSymbol> symbols = suggestionQuery.getResultSet().getResult();
                        if (symbols != null) {
                            if (symbols.size() > 0) {
                                EventBus.getDefault().post(new FetchStockSymbolsEvent(new ArrayList<>(symbols)));
                            } else {
                                Timber.i("No matching records found");
                                EventBus.getDefault().post(new AppMessageEvent(Constants.NO_MATCHING_RECORDS_FOUND));
                            }
                        } else {
                            Timber.i("No results found");
                            EventBus.getDefault().post(new AppMessageEvent(Constants.NO_RECORDS_FOUND));
                        }
                    }

                } else {
                    Timber.e("Http response: %s", response.toString());
                    EventBus.getDefault().post(new AppMessageEvent(Constants.SERVER_ERROR));
                }

            } catch (IOException e) {
                Timber.e("Error executing connection: %s", e.getMessage());
                EventBus.getDefault().post(new AppMessageEvent(Constants.FAILED_TO_CONNECT));
            }
            // let the caller know you've finished
            EventBus.getDefault().post(new AppMessageEvent(Constants.THREAD_TASK_COMPLETE));
        }
    }


}
