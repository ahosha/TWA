package com.olga.twa;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

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

public class SpecificTableListTask extends AsyncTask<String[], Void, String[]> {

    private final String LOG_TAG = SpecificTableListTask.class.getSimpleName();
    private final String LOGIN_URL ="orders/getlistofordersbytableid";

//    http://localhost/Ordersws/api/orders/getlistofordersbytableid?tableid=1&authorization=/

    final String Authorization = "Authorization";
    String authorization_value = "";
    final String Tableid = "tableid";
    String tableid_value = "";

    public String[] tableList;


    @Override
    protected String[] doInBackground(String[]... token) {

        tableid_value = token[0][0];
        Log.v(LOG_TAG, "tableid_value ->  " + tableid_value);

        authorization_value = token[0][1];
        Log.v(LOG_TAG, "authorization_value ->  " + authorization_value);

        String tableListJsonStr = getTableListDataJson();
        if (tableListJsonStr == null) return null;

        String[] tableList = null;
        try {
            tableList = getTableListDataFromJson(tableListJsonStr);
            return tableList;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return tableList;
    }

    @Nullable
    private String getTableListDataJson() {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String loginJsonStr = null;

        try {
            Uri builtUri = Uri.parse(MainActivity.TAMNOON_BASE_URL + LOGIN_URL).buildUpon()
                    .appendQueryParameter(Tableid, tableid_value)
                    .appendQueryParameter(Authorization, authorization_value)
                    .build();

            GetJsonStringFromUrl getStringFromUrl = new GetJsonStringFromUrl(urlConnection, reader, builtUri).invoke();
            if (getStringFromUrl.is()) return null;

            loginJsonStr = getStringFromUrl.getReturnJsonStr();
            Log.v(LOG_TAG, "getTableListDataJson ->  login JSON String:" + loginJsonStr);

            urlConnection = getStringFromUrl.getUrlConnection();
            reader = getStringFromUrl.getReader();

            //Log.v(LOG_TAG, "TamnoonTablesListTask ->  login JSON String:" + loginJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return loginJsonStr;
    }


    private String[] getTableListDataFromJson(String JsonStr)
            throws JSONException {

        final String OWM_LIST = "list";
        final String tableNameJsonName = "TableName";
        final String tableIdJsonName = "TableId";


        JSONArray tableListJson = new JSONArray(JsonStr);


        String[] resultStrs = new String[tableListJson.length()];
        Log.v(LOG_TAG, "TamnoonTablesListTask -> " +tableListJson.length());
        for(int i = 0; i < tableListJson.length(); i++) {
            String tableId = "";
            String tableName = "";

            JSONObject tableObj = tableListJson.getJSONObject(i);
            tableId = tableObj.getString(tableIdJsonName);
            tableName = tableObj.getString(tableNameJsonName);
            //Log.v(LOG_TAG, "TamnoonTablesListTask -> tableName: " +tableName);
            resultStrs[i] = tableId + " - " + tableName;
            //resultStrs[i] = "Table: " + tableName;

        }

        for (String s : resultStrs) {
            //Log.v(LOG_TAG, "TamnoonTablesListTask -> resultStrs entry: " + s);
        }

        //Log.v(LOG_TAG,"TamnoonTablesListTask ->  resultStrs from Web:" + tableList);


        return resultStrs;

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


    private class GetJsonStringFromUrl {
        private boolean myResult;
        private HttpURLConnection urlConnection;
        private BufferedReader reader;
        private Uri builtUri;
        private String returnJsonStr;

        public GetJsonStringFromUrl(HttpURLConnection urlConnection, BufferedReader reader, Uri builtUri) {
            this.urlConnection = urlConnection;
            this.reader = reader;
            this.builtUri = builtUri;
        }

        boolean is() {
            return myResult;
        }

        public HttpURLConnection getUrlConnection() {
            return urlConnection;
        }

        public BufferedReader getReader() {
            return reader;
        }

        public String getReturnJsonStr() {
            return returnJsonStr;
        }

        public GetJsonStringFromUrl invoke() throws IOException {
            URL url = new URL(builtUri.toString());

           // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                myResult = true;
                return this;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                myResult = true;
                return this;
            }
            returnJsonStr = buffer.toString();
            myResult = false;
            return this;
        }
    }
}