package com.olga.twa;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

    public static final String ACTIVITY_SEC_TOKEN = "TOKEN";
    public static final String ACTIVITY_TABLE_NAME = "TABLENAME";
    public static final String ACTIVITY_TABLE_ID = "TABLEID";
    public static final String SHARED_PREF_KEY = "TWA_KEY_FOR_SHARED_PREF";
    public static final String TOKEN_KEY = "TOKEN_KEY";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PASSWORD = "USER_PASSWORD";
    public static final String ACTIVITY_disheId = "disheId";
    public static final String ACTIVITY_TypeId = "TypeId";
    public static final String ACTIVITY_TableId = "TableId";
    public static final String ACTIVITY_Details = "Details";
    public static final String ACTIVITY_Quantity = "Quantity";
    public static final String ACTIVITY_DishName = "DishName";
    public static final String ACTIVITY_Price = "Price";


    public static final String TAMNOON_BASE_LOGIN_URL ="http://192.168.56.1/Ordersws/";
    public static final String TAMNOON_BASE_URL ="http://192.168.56.1/Ordersws/api/";

    public static final String LOGIN_URL ="login.ashx";
    public static final String TABLELIST_URL ="Tables";
    public static final String DISHES_URL ="Dishes";
    public static final String ORDERS_URL ="orders";
    public static final String INSERT_NEWORDERS_URL ="InsertNewOrder";
    public static final String GET_ORDERSLIST_URL ="GetListOfOrdersByTableId";



    public static final String PARAM_USER_NAME = "username";
    public static final String PARAM_USER_PASSWORD = "password";
    public static final String PARAM_Authorization = "Authorization";
    public static final String PARAM_OrderDishId = "OrderDishId";
    public static final String PARAM_OrderDishTypeId = "OrderDishTypeId";
    public static final String PARAM_OrderAdditionalDetails = "OrderAdditionalDetails";
    public static final String PARAM_OrderTableId = "OrderTableId";
    public static final String PARAM_OrderQuantity = "Quantity";
    public static final String PARAM_TableId = "TableId";

    public static final int TABLE_LIST_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }





}
