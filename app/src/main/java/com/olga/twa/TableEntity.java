package com.olga.twa;

import android.net.Uri;
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
import java.util.List;

import java.util.ArrayList;


/**
 * Created by olga on 06/01/2016.
*/
public class TableEntity
{

    private final String LOG_TAG = TableEntity.class.getSimpleName();

    public String tableid;
    public String tablename;

    final String Authorization = "Authorization";

    public TableEntity(String id, String name) {
        this.tableid = id;
        this.tablename = name;
    }

    @Nullable
    public String getTableListDataJson(String authorization_value) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String loginJsonStr = null;

       /* if (authorization_value == null){
            throw new Exception("Received empty authorization_value");}*/

        try {
            Uri builtUri = Uri.parse(MainActivity.TAMNOON_BASE_URL + MainActivity.TABLELIST_URL).buildUpon()
                    .appendQueryParameter(Authorization, authorization_value)
                    .build();

            GetJsonStringFromUrl getStringFromUrl = new GetJsonStringFromUrl(urlConnection, reader, builtUri).invoke();
            if (getStringFromUrl.is()) return null;

            loginJsonStr = getStringFromUrl.getReturnJsonStr();
            urlConnection = getStringFromUrl.getUrlConnection();
            reader = getStringFromUrl.getReader();

            Log.v(LOG_TAG, " ->  login JSON String:" + loginJsonStr);
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

    public String[] getTableListDataFromJson(String JsonStr)
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

            //URL url = new URL( BasicURL + "login.ashx?username=maxim.langman@gmail.com&password=12345");

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

    public List<TableEntity> GetTableEntities(String JsonStr) throws JSONException {

        final String OWM_LIST = "list";
        final String tableNameJsonName = "TableName";
        final String tableIdJsonName = "TableId";
        JSONArray tableListJson = new JSONArray(JsonStr);
        List<TableEntity> resultStrs = new ArrayList<TableEntity>();
        Log.v(LOG_TAG, "TamnoonTablesListTask -> " +tableListJson.length());
        for(int i = 0; i < tableListJson.length(); i++) {
            String tableId = "";
            String tableName = "";

            JSONObject tableObj = tableListJson.getJSONObject(i);
            tableId = tableObj.getString(tableIdJsonName);
            tableName = tableObj.getString(tableNameJsonName);
            resultStrs.add(new TableEntity(tableId, tableName));
        }
        return resultStrs;
    }
}