package com.olga.twa;

/**
 * Created by olga on 06/01/2016.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;

import javax.net.ssl.HttpsURLConnection;

public class LoaderAddOrder extends AsyncTaskLoader<List<EntityDishes>> {

    protected String authorization_value = null;
    private final String LOG_TAG = LoaderAddOrder.class.getSimpleName();
    List<EntityDishes> list = null;
    protected String disheId = "";
    protected String TypeId = "";;
    protected String TableId = "";
    protected String Details = "";
    protected String Quantity = "";


    public LoaderAddOrder(Context context, Bundle bundle) {
        super(context);
        disheId = bundle.getString("disheId");
        TypeId = bundle.getString("TypeId");
        TableId = bundle.getString("TableId");
        Details = bundle.getString("Details");
        Quantity = bundle.getString("Quantity");

        authorization_value = bundle.getString("TOKEN_KEY");
    }

    @Override
    public List<EntityDishes> loadInBackground() {



        EntityOrder order = new EntityOrder();

        Log.v(LOG_TAG, "authorization_value-> " + authorization_value);

        order.postOrderToURL(authorization_value,
                                disheId,
                                TypeId,
                                TableId,
                                Details,
                                Quantity);

        return null;
    }





    @Override
    protected void onStartLoading() {
        if (list != null) {
            deliverResult(list);
        }
        // TODO: register an observer
        if (takeContentChanged() || list == null) {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<EntityDishes> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            //releaseResources(data);
            return;
        }
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<EntityDishes> oldData = list;
        list = data;
        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the client.
            super.deliverResult(data);
        }
        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            //releaseResources(oldData);
        }
    }

}