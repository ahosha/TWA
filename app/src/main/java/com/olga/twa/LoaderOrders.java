package com.olga.twa;

/**
 * Created by olga on 06/01/2016.
 */

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;

public class LoaderOrders extends AsyncTaskLoader<List<EntityOrder>> {

    protected String authorization_value = null;
    protected String tableId_value = null;

    private final String LOG_TAG = LoaderOrders.class.getSimpleName();
    List<EntityOrder> list = null;

    public LoaderOrders(Context context, Bundle bundle) {
        super(context);
        authorization_value = bundle.getString("TOKEN_KEY");
        tableId_value = bundle.getString(MainActivity.ACTIVITY_TableId);
    }

    @Override
    public List<EntityOrder> loadInBackground() {
        list = new ArrayList<EntityOrder>();
        Log.v(LOG_TAG, "authorization_value ->  " + authorization_value);
        EntityOrder orderList = new EntityOrder();
        return orderList.getOrdersListFormURL(authorization_value,tableId_value );
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
    public void deliverResult(List<EntityOrder> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            //releaseResources(data);
            return;
        }
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<EntityOrder> oldData = list;
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