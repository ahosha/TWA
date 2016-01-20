package com.olga.twa;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by olga on 19/01/2016.
 */
public class ContractDishes {


    public static final String PROVIDER_NAME = "twa.disheprovider.dishes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/list");
    public static final int DISHES = 1;
    public static final int DISHES_ID = 2;


    public static final String DATABASE_NAME = "com.example.twa.db.TWADatabase";
    public static final int DB_VERSION = 3;
    public static final String DISHES_TABLE_NAME = "dishes";



    public class Columns {
        public static final String DISHESNAME = "DISHESNAME";
        public static final String _ID = BaseColumns._ID;
        public static final String DISHESTYPE = "DISHESTYPE";
        public static final String DISHESPRICE ="DISHESPRICE";
        public static final String DISHESDESCRIPTION = "DISHESDESCRIPTION";
        public static final String DISHESURL = "DISHESURL";


    }

}
