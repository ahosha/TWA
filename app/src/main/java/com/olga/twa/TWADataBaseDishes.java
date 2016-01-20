package com.olga.twa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by olga on 18/01/2016.
 */
public class TWADataBaseDishes  extends SQLiteOpenHelper {

    private final String LOG_TAG = TWADataBaseDishes.class.getSimpleName();

    private static final String SQL_CREATE = "CREATE TABLE " + ContractDishes.DISHES_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY, DISHESNAME TEXT , DISHESTYPE INT , DISHESPRICE DOUBLE , DISHESDESCRIPTION TEXT , DISHESURL TEXT )";

    private static final String SQL_DROP = "DROP TABLE IF EXISTS " + ContractDishes.DISHES_TABLE_NAME ;

    TWADataBaseDishes(Context context) {
        super(context, ContractDishes.DATABASE_NAME, null, ContractDishes.DB_VERSION);
        Log.v(LOG_TAG, "ctor");
    }

    //*****************************************************************
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        onCreate(db);
    }

    //*****************************************************************

    public Cursor getDishes(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.v(LOG_TAG, "getDishes" );
        SQLiteQueryBuilder sqliteQueryBuilder = new SQLiteQueryBuilder();
        sqliteQueryBuilder.setTables(ContractDishes.DISHES_TABLE_NAME);

        if(id != null) {
            sqliteQueryBuilder.appendWhere("_id" + " = " + id);
        }

        if(sortOrder == null || sortOrder == "") {
            sortOrder = "DISHESNAME";
        }
        Cursor cursor;
        Log.v(LOG_TAG, "getDishes 2" );
        cursor = sqliteQueryBuilder.query(getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        Log.v(LOG_TAG, "getDishes 3" );
        return cursor;


    }

    public long addNewDish(ContentValues values) throws SQLException {
        long id = getWritableDatabase().insert(ContractDishes.DISHES_TABLE_NAME, "", values);
        if(id <=0 ) {
            throw new SQLException("Failed to add an dish");
        }

        return id;
    }

    public void bulkAddNewDish(Cursor cursor) throws SQLException {
        Log.v(LOG_TAG, "bulkAddNewDish" );
        if (cursor.getCount() != 0 || cursor.moveToFirst()) {
            while (cursor.moveToNext())
            {
                ContentValues values = new ContentValues();
                Log.v(LOG_TAG, "bulkAddNewDish:" + ContractDishes.Columns._ID + " -> s" + cursor.getString(cursor.getColumnIndexOrThrow(ContractDishes.Columns._ID)));
                values.put(ContractDishes.Columns._ID, cursor.getString(cursor.getColumnIndexOrThrow(ContractDishes.Columns._ID)));
                values.put(ContractDishes.Columns.DISHESNAME, cursor.getString(cursor.getColumnIndexOrThrow(ContractDishes.Columns.DISHESNAME)));
                values.put(ContractDishes.Columns.DISHESTYPE, cursor.getString(cursor.getColumnIndexOrThrow(ContractDishes.Columns.DISHESTYPE)));
                values.put(ContractDishes.Columns.DISHESPRICE, cursor.getString(cursor.getColumnIndexOrThrow(ContractDishes.Columns.DISHESPRICE)));
                values.put(ContractDishes.Columns.DISHESDESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(ContractDishes.Columns.DISHESDESCRIPTION)));
                values.put(ContractDishes.Columns.DISHESURL, cursor.getString(cursor.getColumnIndexOrThrow(ContractDishes.Columns.DISHESURL)));
                long i = addNewDish(values);
            }
        }
        cursor.close();
        return ;
    }


    public int deleteDishes(String id) {
        if(id == null) {
            return getWritableDatabase().delete(ContractDishes.DISHES_TABLE_NAME, null , null);
        } else {
            return getWritableDatabase().delete(ContractDishes.DISHES_TABLE_NAME, "_id=?", new String[]{id});
        }
    }

    public int updateDishes(String id, ContentValues values) {
        if(id == null) {
            return getWritableDatabase().update(ContractDishes.DISHES_TABLE_NAME, values, null, null);
        } else {
            return getWritableDatabase().update(ContractDishes.DISHES_TABLE_NAME, values, "_id=?", new String[]{id});
        }
    }

}
