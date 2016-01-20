package com.olga.twa;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by olga on 18/01/2016.
 */
    public class ProviderDishes extends ContentProvider {

    private final String LOG_TAG = ProviderDishes.class.getSimpleName();

    private TWADataBaseDishes twaDatabaseDishes = null;

    private static final UriMatcher uriMatcher = getUriMatcher();

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContractDishes.PROVIDER_NAME, "dishes", ContractDishes.DISHES);
        uriMatcher.addURI(ContractDishes.PROVIDER_NAME, "dishes/#",ContractDishes.DISHES_ID);
        return uriMatcher;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ContractDishes.DISHES:
                return "vnd.android.cursor.dir/vnd.com.twa.disheprovider.provider.images";
            case ContractDishes.DISHES_ID:
                return "vnd.android.cursor.item/vnd.com.twa.disheprovider.provider.images";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);

        }

    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        twaDatabaseDishes = new TWADataBaseDishes(context);
         return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.v(LOG_TAG, "query");
        String id = null;
        Log.v(LOG_TAG, "query uri:" + uri.toString());
        if(uriMatcher.match(uri) == ContractDishes.DISHES_ID) {
            Log.v(LOG_TAG, "query DISHES_ID uri:" + uri.toString());
            //Query is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }
        Log.v(LOG_TAG, "query before cursor uri:" + uri.toString());
        Cursor cursorDishes = twaDatabaseDishes.getDishes(id, projection, selection, selectionArgs, sortOrder);
        Log.v(LOG_TAG, "get cursor from DB" + uri.toString());
        if(!cursorDishes.moveToFirst() || cursorDishes.getCount() == 0)
        {
            // get data from URL
            SharedPreferences preferences = getContext().getSharedPreferences(MainActivity.SHARED_PREF_KEY, getContext().MODE_PRIVATE);
            String authorization_value = preferences.getString(MainActivity.TOKEN_KEY,"");
            Log.v(LOG_TAG, "authorization_value ->  " + authorization_value);
            EntityDishes de = new EntityDishes();
            cursorDishes = de.getDishesCursorFormURL(authorization_value);
            Log.v(LOG_TAG, "cursorDishes  from URL !!! ->  " + cursorDishes.getCount());
            if(cursorDishes.moveToFirst() || cursorDishes.getCount() != 0) {
                try {
                    twaDatabaseDishes.bulkAddNewDish(cursorDishes);
                    Log.v(LOG_TAG, "add data to DB ->  " + cursorDishes.getCount());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        cursorDishes.setNotificationUri(getContext().getContentResolver(), uri);
        return cursorDishes;


    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long id = twaDatabaseDishes.addNewDish(values);
            Uri returnUri = ContentUris.withAppendedId(ContractDishes.CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(uri, null);
            return returnUri;
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == ContractDishes.DISHES_ID) {
            //Delete is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        int count = twaDatabaseDishes.deleteDishes(id);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == ContractDishes.DISHES_ID) {
            //Update is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        int count = twaDatabaseDishes.updateDishes(id, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }
}
