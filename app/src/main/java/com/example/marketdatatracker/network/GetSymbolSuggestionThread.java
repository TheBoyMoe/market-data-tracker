package com.example.marketdatatracker.network;

import android.net.Uri;

import com.example.marketdatatracker.event.FetchStockSymbolsEvent;
import com.example.marketdatatracker.model.StockSymbol;
import com.example.marketdatatracker.model.SuggestionQuery;
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
            Timber.i("Query url: %s", querySuggestionUri);

            // execute the connection and retrieve the data
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(querySuggestionUri.toString()).build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Timber.i("Query: %s", response.toString());
                    Reader in = response.body().charStream();
                    BufferedReader reader = new BufferedReader(in);

                    // convert the resultant response into an object gson can parse
                    // 1. convert the reader into a string
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    Timber.i("String response: %s", sb);

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
                                // TODO post message & clear the adapter, hide progress bar
                            }
                        } else {
                            Timber.i("No results found");
                            // TODO post message
                        }
                    }




                } else {
                    Timber.e("Http response: %s", response.toString());
                    // TODO post message
                }

            } catch (IOException e) {
                Timber.e("Error executing connection: %s", e.getMessage());
            }
        }
    }


}
