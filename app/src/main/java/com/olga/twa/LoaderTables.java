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

public class LoaderTables extends AsyncTaskLoader<List<TableEntity>> {

        protected String authorization_value = null;
        private final String LOG_TAG = LoaderTables.class.getSimpleName();
        List<TableEntity> list = null;

        public LoaderTables(Context context, Bundle bundle) {
            super(context);
            authorization_value = bundle.getString(MainActivity.TOKEN_KEY);
        }

        @Override
        public List<TableEntity> loadInBackground() {
            list = new ArrayList<TableEntity>();
            Log.v(LOG_TAG, "authorization_value ->  " + authorization_value);
            TableEntity te = new TableEntity(null,null);
            String tableListJsonStr = te.getTableListDataJson(authorization_value);
            if (tableListJsonStr == null) return null;
            try {
                list =  te.GetTableEntities(tableListJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
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
        public void deliverResult(List<TableEntity> data) {
        if (isReset()) {
        // The Loader has been reset; ignore the result and invalidate the data.
        //releaseResources(data);
            return;
        }
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<TableEntity> oldData = list;
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