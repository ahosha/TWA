package com.olga.twa;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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


public class TamnoonLoginTask extends AsyncTask<Void, Void, String> {

    private final String LOG_TAG = TamnoonLoginTask.class.getSimpleName();

    final String username = "username";
    final String username_value = "maxim.langman";
    final String password = "password";
    final String password_value = "12345";

    public String[] tableList;


    private Context context;

    public TamnoonLoginTask(Context context) {
        this.context = context;
    }

    @Nullable
    private String getLoginDataJson() {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String loginJsonStr = null;

        try {
            Uri builtUri = Uri.parse(MainActivity.TAMNOON_BASE_LOGIN_URL + MainActivity.LOGIN_URL).buildUpon()
                    .appendQueryParameter(username, username_value)
                    .appendQueryParameter(password, password_value)
                    .build();


            GetJsonStringFromUrl getStringFromUrl = new GetJsonStringFromUrl(urlConnection, reader, builtUri).invoke();
            if (getStringFromUrl.is()) return null;

            loginJsonStr = getStringFromUrl.getReturnJsonStr();
            urlConnection = getStringFromUrl.getUrlConnection();
            reader = getStringFromUrl.getReader();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
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


    private String getLoginDataFromJson(String JsonStr)
            throws JSONException {

        final String accessTokenName = "access_token";
        final String accessTokenJson ;

        JSONObject loginObj = new JSONObject(JsonStr);
        accessTokenJson = loginObj.getString(accessTokenName);

        Log.v(LOG_TAG, "getLoginDataFromJson: " + accessTokenJson);
        return accessTokenJson;

    }

    @Override
    protected String doInBackground(Void... params) {

        String loginJsonStr = getLoginDataJson();
        if (loginJsonStr == null) return null;

        Log.v(LOG_TAG, "LoginResult ->  login JSON String:" + loginJsonStr);

        String token = null;
        try {
            token = getLoginDataFromJson(loginJsonStr);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        finally {
            Log.v(LOG_TAG, "LoginResult ->  token :" + token);

            return token;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {

            super.onPostExecute(result);
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

            //Log.v(LOG_TAG, "builtUri -> before open connection: " + builtUri);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Log.v(LOG_TAG, "builtUri -> after open connection: " + builtUri);

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();

            //Log.v(LOG_TAG, "builtUri after  ->  " + builtUri);

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
            Log.v(LOG_TAG, "builtUri after  ->  returnJsonStr " + returnJsonStr);

            myResult = false;
            return this;
        }
    }
}
