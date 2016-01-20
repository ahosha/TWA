package com.olga.twa;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by olga on 06/01/2016.
 */



public class LoaderDishes extends CursorLoader {


    protected String authorization_value = null;
    private final String LOG_TAG = LoaderDishes.class.getSimpleName();
    Cursor list = null;

    ForceLoadContentObserver mObserver;

    public LoaderDishes(Context context, Bundle bundle) {
        super(context);
        Log.v(LOG_TAG, "initLoader start");
        authorization_value = bundle.getString(MainActivity.TOKEN_KEY);
        mObserver = new ForceLoadContentObserver();
        Log.v(LOG_TAG, "initLoader done");
    }

    public LoaderDishes(Context context, Uri uri,
                                   String[] projection, String selection, String[] selectionArgs,
                                   String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
        mObserver = new ForceLoadContentObserver();

    }

    @Override
    public Cursor loadInBackground() {

/*        list = new Cursor();
        Log.v(LOG_TAG, "authorization_value ->  " + authorization_value);
        EntityDishes de = new EntityDishes();
        return  de.getDishesListFormURL(authorization_value);*/

 //       ContentValues values = new ContentValues();
/*        values.put(MyProvider.name, ((EditText) findViewById(R.id.txtName))
                .getText().toString());*/

        Log.v(LOG_TAG, "getContext().getContentResolver().query");

        return getContext().getContentResolver().query(ContractDishes.CONTENT_URI,
                new String[]{
                    ContractDishes.Columns._ID,
                    ContractDishes.Columns.DISHESNAME,
                    ContractDishes.Columns.DISHESDESCRIPTION,
                    ContractDishes.Columns.DISHESPRICE,
                    ContractDishes.Columns.DISHESURL},
                null, null, null, null);
    }

    void registerObserver(Cursor cursor, ContentObserver observer) {
        cursor.registerContentObserver(mObserver);
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
    public void deliverResult(Cursor data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            //releaseResources(data);
            return;
        }
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        Cursor oldData = list;
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