package com.olga.twa;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by olga on 18/01/2016.
 */
    public class ProviderDishes extends ContentProvider {

    private TWADataBaseDishes twaDatabaseDishes = null;

    private static final String PROVIDER_NAME = "twa.disheprovider.dishes";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/list");
    private static final int DISHES = 1;
    private static final int DISHES_ID = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "dishes", DISHES);
        uriMatcher.addURI(PROVIDER_NAME, "dishes/#", DISHES_ID);
        return uriMatcher;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case DISHES:
                return "vnd.android.cursor.dir/vnd.com.twa.disheprovider.provider.images";
            case DISHES_ID:
                return "vnd.android.cursor.item/vnd.com.twa.disheprovider.provider.images";

        }
        return "";
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        twaDatabaseDishes = new TWADataBaseDishes(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String id = null;
        if(uriMatcher.match(uri) == DISHES_ID) {
            //Query is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }
        return twaDatabaseDishes.getDishes(id, projection, selection, selectionArgs, sortOrder);

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long id = twaDatabaseDishes.addNewDish(values);
            Uri returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            return returnUri;
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == DISHES_ID) {
            //Delete is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        return twaDatabaseDishes.deleteDishes(id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == DISHES_ID) {
            //Update is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        return twaDatabaseDishes.updateDishes(id, values);
    }
}
