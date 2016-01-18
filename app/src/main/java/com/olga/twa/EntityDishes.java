package com.olga.twa;

/**
 * Created by olga on 06/01/2016.
 */
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
public class EntityDishes
{
    private final String LOG_TAG = EntityDishes.class.getSimpleName();

    public String ID ;
    public String DishType ;
    public String Name;
    public String Price;
    public String PriceCurrency;
    public String Description;
    public String ImageUrl;

    final String OWM_LIST = "list";

    final String tableNameID = "Id";
    final String tableNameDishType = "DishType";
    final String tableNameName = "Name";
    final String tableNamePrice = "Price";
    final String tableNamePriceCurrency = "PriceCurrency";
    final String tableNameDescription = "Description";
    final String tableNameImageUrl = "ImageUrl";



    public EntityDishes(){}


    public EntityDishes(String ID,
                        String DishType,
             String Name,String Price,
             String PriceCurrency,
             String Description,
             String ImageUrl)
    {
        this.ID = ID;
        this.DishType = DishType;
        this.Name = Name;
        this.Price = Price;
        this.PriceCurrency = PriceCurrency;
        this.Description = Description;
        this.ImageUrl = ImageUrl;
    }

    public List<EntityDishes> getDishesListFormURL(String authorization_value)
    {
        List<EntityDishes> list = null;
        String dishesListJsonStr = getDishesListDataJson(authorization_value);
        if (dishesListJsonStr == null) return list;
        try {
            list =  GetDishesEntities(dishesListJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Nullable
    private String getDishesListDataJson(String authorization_value) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String loginJsonStr = null;

       /* if (authorization_value == null){
            throw new Exception("Received empty authorization_value");}*/

        try {
            Uri builtUri = Uri.parse(MainActivity.TAMNOON_BASE_URL + MainActivity.DISHES_URL).buildUpon()
                    .appendQueryParameter(MainActivity.PARAM_Authorization, authorization_value)
                    .build();

            GetJsonStringFromUrl getStringFromUrl = new GetJsonStringFromUrl(urlConnection, reader, builtUri).invoke();
            if (getStringFromUrl.is()) return null;

            loginJsonStr = getStringFromUrl.getReturnJsonStr();
            urlConnection = getStringFromUrl.getUrlConnection();
            reader = getStringFromUrl.getReader();

            Log.v(LOG_TAG, " ->  JSON String:" + loginJsonStr);
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

    private List<EntityDishes> GetDishesEntities(String JsonStr) throws JSONException {

        JSONArray dishesListJson = new JSONArray(JsonStr);
        List<EntityDishes> resultStrs = new ArrayList<EntityDishes>();
        //Log.v(LOG_TAG, "dishesListJson.length -> " +dishesListJson.length());

        for(int i = 0; i < dishesListJson.length(); i++) {
            JSONObject disheObj = dishesListJson.getJSONObject(i);
            //Log.v(LOG_TAG, "disheObj.toString() -> " + disheObj.toString());
            ID = disheObj.getString(tableNameID);
            DishType = disheObj.getString(tableNameDishType);
            Name = disheObj.getString(tableNameName);
            Price = disheObj.getString(tableNamePrice);
            PriceCurrency = disheObj.getString(tableNamePriceCurrency);
            Description = disheObj.getString(tableNameDescription);
            ImageUrl = disheObj.getString(tableNameImageUrl);
            resultStrs.add(new EntityDishes(ID, DishType, Name, Price, PriceCurrency, Description, ImageUrl));
        }
        return resultStrs;
    }
}