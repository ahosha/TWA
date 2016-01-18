package com.olga.twa;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by olga on 29/12/2015.
 */

public class TamnoonTablesListTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = TamnoonTablesListTask.class.getSimpleName();


    String authorization_value = "";

    @Override
    protected String[] doInBackground(String... token) {

        authorization_value = token[0];
        Log.v(LOG_TAG, "authorization_value ->  " + authorization_value);

        TableEntity te = new TableEntity(null,null);

        String tableListJsonStr = te.getTableListDataJson(authorization_value);
        if (tableListJsonStr == null) return null;

        String[] tableList = null;
        try {
            tableList = te.getTableListDataFromJson(tableListJsonStr);
            return tableList;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return tableList;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null) {


/*            mTableListAdapter.clear();
            for(String dayForecastStr : result) {
                mTableListAdapter.add(dayForecastStr);
            }*/
            // New data is back from the server.  Hooray!
        }
    }



}