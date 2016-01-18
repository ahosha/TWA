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
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by olga on 06/01/2016.
 */
public class EntityOrder
{
    private final String LOG_TAG = EntityOrder.class.getSimpleName();

    String disheId = "";
    String TypeId = "";;
    String TableId = "";
    String Details = "";
    String Quantity = "";
    String DishName = "";
    String Price = "";
    String OrderStatus = "New";
    SimpleDateFormat OrderDateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date OrderDate =  new Date (System.currentTimeMillis());

    final String OWM_LIST = "list";

    final String tableNameID = "Id";
    final String tableNameDishType = "DishType";
    final String tableNameName = "Name";
    final String tableNamePrice = "Price";
    final String tableNamePriceCurrency = "PriceCurrency";
    final String tableNameDescription = "Description";
    final String tableNameImageUrl = "ImageUrl";



    public EntityOrder(){}


    public EntityOrder(    String disheId ,
                            String TypeId ,
                            String TableId ,
                            String Details ,
                            String Quantity,
                            String DishName ,
                            String Price)
    {
        this.disheId = disheId;
        this.TypeId = TypeId;;
        this.TableId = TableId;
        this.Details = Details;
        this.Quantity = Quantity;
        this.DishName = DishName;
        this.Price = Price;
        this.OrderDateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm");//new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
        this.OrderStatus = "New" ;
        this.OrderDate = new Date (System.currentTimeMillis());

    }

    public EntityOrder(    String disheId ,
                           String TypeId ,
                           String TableId ,
                           String Details ,
                           String Quantity,
                           String DishName ,
                           String Price,
                           long OrderTime,
                           String OrderStatus)
    {
        this.disheId = disheId;
        this.TypeId = TypeId;;
        this.TableId = TableId;
        this.Details = Details;
        this.Quantity = Quantity;
        this.DishName = DishName;
        this.Price = Price;
        this.OrderDate = new Date (OrderTime);
        this.OrderStatus = OrderStatus ;

    }


    public void postOrderToURL(String authorization_value,
                                             String disheId ,
                                             String TypeId ,
                                             String TableId ,
                                             String Details ,
                                             String Quantity)
    {
        String insertURL = MainActivity.ORDERS_URL + "/" + MainActivity.INSERT_NEWORDERS_URL ;
        Uri builtUri = Uri.parse(MainActivity.TAMNOON_BASE_URL + insertURL).buildUpon().build();
        String urltopost =builtUri.toString();
        Log.v(LOG_TAG, "urltopost-> " + urltopost);
        Log.v(LOG_TAG, "authorization_value-> " + authorization_value);

        Hashtable<String, String> postDataParams = new Hashtable<String, String>();
        postDataParams.put(MainActivity.PARAM_OrderDishId, disheId);
        postDataParams.put(MainActivity.PARAM_OrderDishTypeId, TypeId);
        postDataParams.put(MainActivity.PARAM_OrderTableId, TableId);
        postDataParams.put(MainActivity.PARAM_OrderAdditionalDetails, Details);
        postDataParams.put(MainActivity.PARAM_OrderQuantity, Quantity);


        Log.v(LOG_TAG, "onCreateLoader before performPostCall-> ");
        performPostCall(urltopost, postDataParams, "bearer " + authorization_value );
        Log.v(LOG_TAG, "onCreateLoader after performPostCall-> ");


      }


    public String  performPostCall(String requestURL,
                                   Hashtable<String, String> postDataParams, String token) {

        URL url;
        String response = "";
        try {
            Log.v(LOG_TAG, "performPostCall 01 -> ");
            url = new URL(requestURL);
            Log.v(LOG_TAG, "performPostCall 011 -> ");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            //Content-Type: application/x-www-form-urlencoded
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", token);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostParamString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                Log.v(LOG_TAG, "performPostCall ok -> ");

                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";
                Log.v(LOG_TAG, "performPostCall not ok -> responseCode: "+ responseCode);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    private String getPostParamString(Hashtable<String, String> params) {
        if(params.size() == 0)
            return "";

        StringBuffer buf = new StringBuffer();
        Enumeration<String> keys = params.keys();
        while(keys.hasMoreElements()) {
            buf.append(buf.length() == 0 ? "" : "&");
            String key = keys.nextElement();
            buf.append(key).append("=").append(params.get(key));
        }
        return buf.toString();
    }

// ****************************************************************************************************

    public List<EntityOrder> getOrdersListFormURL(String token, String tableId)
    {
        List<EntityOrder> list = null;
        String orderListJsonStr = getOrdersListDataJson(token,tableId);
        if (orderListJsonStr == null) return list;
        try {
            list =  GetDishesEntities(orderListJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Nullable
    private String getOrdersListDataJson(String authorization_value, String tableid) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String loginJsonStr = null;

       /* if (authorization_value == null){
            throw new Exception("Received empty authorization_value");}*/

        try {

            String getURL = MainActivity.ORDERS_URL + "/" + MainActivity.GET_ORDERSLIST_URL ;
            Uri builtUri = Uri.parse(MainActivity.TAMNOON_BASE_URL + getURL).buildUpon()
                    .appendQueryParameter(MainActivity.PARAM_TableId, tableid)
                    .appendQueryParameter(MainActivity.PARAM_Authorization, authorization_value)
                    .build();

            Log.v(LOG_TAG, " -> builtUri:" + builtUri.toString());

            GetJsonStringFromUrl getStringFromUrl = new GetJsonStringFromUrl(urlConnection, reader, builtUri).invoke();
            if (getStringFromUrl.is()) return null;

            loginJsonStr = getStringFromUrl.getReturnJsonStr();
            Log.v(LOG_TAG, " ->  JSON String:" + loginJsonStr);

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

    private List<EntityOrder> GetDishesEntities(String JsonStr) throws JSONException {

        JSONArray dishesListJson = new JSONArray(JsonStr);
        List<EntityOrder> resultStrs = new ArrayList<EntityOrder>();
        //Log.v(LOG_TAG, "dishesListJson.length -> " +dishesListJson.length());

        for(int i = 0; i < dishesListJson.length(); i++) {
            /*
            JSONObject disheObj = dishesListJson.getJSONObject(i);
            //Log.v(LOG_TAG, "disheObj.toString() -> " + disheObj.toString());
            ID = disheObj.getString(tableNameID);
            DishType = disheObj.getString(tableNameDishType);
            Name = disheObj.getString(tableNameName);
            Price = disheObj.getString(tableNamePrice);
            PriceCurrency = disheObj.getString(tableNamePriceCurrency);
            Description = disheObj.getString(tableNameDescription);
            ImageUrl = disheObj.getString(tableNameImageUrl);
            resultStrs.add(new EntityOrder(ID, DishType, Name, Price, PriceCurrency, Description, ImageUrl));
            */
        }
        return resultStrs;
    }


}