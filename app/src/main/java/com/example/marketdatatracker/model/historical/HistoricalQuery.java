package com.example.marketdatatracker.model.historical;

/**
{
    "query":{
        "count":252,
        "created":"2016-04-03T17:38:12Z",
        "lang":"en-GB",
        "diagnostics":{  },
        "results":{
            "quote":[
                {
                    "Symbol":"GRPN",
                    "Date":"2016-03-31",
                    "Open":"3.90",
                    "High":"4.07",
                    "Low":"3.90",
                    "Close":"3.99",
                    "Volume":"6862700",
                    "Adj_Close":"3.99"
                },
                {
                    "Symbol":"GRPN",
                    "Date":"2016-03-30",
                    "Open":"3.97",
                    "High":"4.01",
                    "Low":"3.87",
                    "Close":"3.90",
                    "Volume":"8015200",
                    "Adj_Close":"3.90"
                },
                {
                    "Symbol":"GRPN",
                    "Date":"2016-03-29",
                    "Open":"3.88",
                    "High":"4.01",
                    "Low":"3.81",
                    "Close":"3.97",
                    "Volume":"8076700",
                    "Adj_Close":"3.97"
                },
                {},
                {}
            ]
        }
    }
 }
*/

public class HistoricalQuery {

    private Query query;

    public Query getQuery() {
        return query;
    }
}
