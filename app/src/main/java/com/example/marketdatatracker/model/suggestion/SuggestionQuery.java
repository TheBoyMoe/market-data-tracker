package com.example.marketdatatracker.model.suggestion;


/**
 * http://autoc.finance.yahoo.com/autoc?query=ab&callback=YAHOO.Finance.SymbolSuggest.ssCallback&region=US&lang=en-US
 *
 {
    "ResultSet":{
        "Query":"ab",
                "Result":[
        {
            "symbol":"AB",
                "name":"AllianceBernstein Holding L.P.",
                "exch":"NYQ",
                "type":"S",
                "exchDisp":"NYSE",
                "typeDisp":"Equity"
        },
        {
            "symbol":"ABC",
                "name":"AmerisourceBergen Corporation",
                "exch":"NYQ",
                "type":"S",
                "exchDisp":"NYSE",
                "typeDisp":"Equity"
        },
        {
            "symbol":"ABX",
                "name":"Barrick Gold Corporation",
                "exch":"NYQ",
                "type":"S",
                "exchDisp":"NYSE",
                "typeDisp":"Equity"
        },
        {
            "symbol":"ABBV",
                "name":"AbbVie Inc.",
                "exch":"NYQ",
                "type":"S",
                "exchDisp":"NYSE",
                "typeDisp":"Equity"
        },
        {
            "symbol":"ABT",
                "name":"Abbott Laboratories",
                "exch":"NYQ",
                "type":"S",
                "exchDisp":"NYSE",
                "typeDisp":"Equity"
        },

        {
            "symbol":"ABG.MC",
                "name":"ABENGOA -A-",
                "exch":"MCE",
                "type":"S",
                "exchDisp":"Madrid StockItem Exchange CATS",
                "typeDisp":"Equity"
        }
        ]
    }
 }

*/

public class SuggestionQuery {

    private com.example.marketdatatracker.model.suggestion.ResultSet ResultSet;

    public ResultSet getResultSet() {
        return ResultSet;
    }

}
