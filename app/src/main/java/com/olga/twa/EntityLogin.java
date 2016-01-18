package com.olga.twa;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olga on 06/01/2016.
 */
public class EntityLogin {

    private final String LOG_TAG = EntityLogin.class.getSimpleName();

    public String access_token;
    public String token_type;
    public String expires_in;
    public String userName;
    public String userEmail;
    public String userId;
    public String userRestaurantId;
    public String userPhoneNumber;
    public String userFirstName;
    public String userLastName;
    public String userRole1;
    public String userRole2;
    public String userRole3;
    public String issued;
    public String expires;

    final String accessTokenNamestr = "access_token";
    final String userNamestr = "userName";
    final String userIdstr = "userId";

    public EntityLogin(String id, String name, String access_token) {
        this.userId = id;
        this.userName = name;
        this.access_token = access_token;
    }

    @Nullable
    public String getLoginDataJson(String username_value, String password_value) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String loginJsonStr = null;

        try {
            Uri builtUri = Uri.parse(MainActivity.TAMNOON_BASE_LOGIN_URL + MainActivity.LOGIN_URL).buildUpon()
                    .appendQueryParameter(MainActivity.PARAM_USER_NAME, username_value)
                    .appendQueryParameter(MainActivity.PARAM_USER_PASSWORD, password_value)
                    .build();

            Log.v(LOG_TAG, "builtUri: "+builtUri);

            GetJsonStringFromUrl getStringFromUrl = new GetJsonStringFromUrl(urlConnection, reader, builtUri).invoke();
            Log.v(LOG_TAG, "getStringFromUrl: "+getStringFromUrl);

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

    private List<EntityLogin> getLoginDataFromJson(String JsonStr)
            throws JSONException {

        List<EntityLogin> listtoreturn = new ArrayList<EntityLogin>();


        JSONObject loginObj = new JSONObject(JsonStr);

        listtoreturn.add(new EntityLogin(loginObj.getString(userIdstr),
                                            loginObj.getString(userNamestr),
                                            loginObj.getString(accessTokenNamestr) ));


        return listtoreturn;

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


    public List<EntityLogin> getLoginDataFromURL(String username_value, String password_value)
    {
        Log.v(LOG_TAG, "getLoginDataFromURL start -" );

        List<EntityLogin> listtoreturn  = null;

        String loginJsonStr = getLoginDataJson(username_value,password_value);
        if (loginJsonStr == null) return null;

        Log.v(LOG_TAG, "LoginResult ->  login JSON String:" + loginJsonStr);

        try {
            listtoreturn = getLoginDataFromJson(loginJsonStr);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        finally {
           //Log.v(LOG_TAG, "LoginResult ->  token :" + token);
            return listtoreturn;
        }
    }
}
