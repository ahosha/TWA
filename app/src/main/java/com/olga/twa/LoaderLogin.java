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

public class LoaderLogin extends AsyncTaskLoader<List<EntityLogin>> {

    protected String username = null;
    protected String password = null;
    private final String LOG_TAG = LoaderLogin.class.getSimpleName();
    List<EntityLogin> list = null;

    public LoaderLogin(Context context, Bundle bundle) {
        super(context);
        Log.v(LOG_TAG, "username :" + username + ": password:" + password);

        username = bundle.getString(MainActivity.USER_NAME);
        password = bundle.getString(MainActivity.USER_PASSWORD);

        Log.v(LOG_TAG, "getted username :" + username + ": password:" + password);

    }

    @Override
    public List<EntityLogin> loadInBackground() {
        Log.v(LOG_TAG, "loadInBackground username :" + username + ": password:" + password);
        list = new ArrayList<EntityLogin>();
        EntityLogin entity = new EntityLogin(null,null,null);
        list = entity.getLoginDataFromURL(username,password);
        Log.v(LOG_TAG, "loadInBackground done  list:" + list );
        return list;

    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "onStartLoading list: "+list+"username :" + username + ": password:" + password);

        if (list != null) {
            deliverResult(list);
        }
        // TODO: register an observer
        if (takeContentChanged() || list == null) {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<EntityLogin> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            //releaseResources(data);
            return;
        }
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<EntityLogin> oldData = list;
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