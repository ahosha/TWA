package com.olga.twa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class AddOrderActivity extends AppCompatActivity {

    private final String LOG_TAG = AddOrderActivity.class.getSimpleName();
    String disheId = "";
    String TypeId = "";;
    String TableId = "";
    String Details = "";
    String Quantity = "";
    String DishName = "";
    String Price = "";
    String token = "" ;

    Button AddOrderButton = null;
    EditText OrderCOmment = null;
    EditText orderQuantity = null;
    TextView orderdetails = null;

    int AddOrder_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        Bundle extras = getIntent().getExtras();
        token = (String) extras.get(MainActivity.ACTIVITY_SEC_TOKEN);
        disheId = (String) extras.get(MainActivity.ACTIVITY_disheId);
        TypeId = (String) extras.get(MainActivity.ACTIVITY_TypeId);
        TableId = (String) extras.get(MainActivity.ACTIVITY_TableId);
        Details = (String) extras.get(MainActivity.ACTIVITY_Details);
        Quantity = (String) extras.get(MainActivity.ACTIVITY_Quantity);
        DishName = (String) extras.get(MainActivity.ACTIVITY_DishName);
        Price = (String) extras.get(MainActivity.ACTIVITY_Price);

        SharedPreferences preferences = getSharedPreferences(MainActivity.SHARED_PREF_KEY, MODE_PRIVATE);
        token = preferences.getString(MainActivity.TOKEN_KEY,"");
        Log.v(LOG_TAG, "token:"+token);

        AddOrderButton = (Button) findViewById(R.id.bAddOrder);
        OrderCOmment = (EditText) findViewById(R.id.dishcomment);
        orderQuantity = (EditText) findViewById(R.id.dishQuantity);
        orderdetails = (TextView) findViewById(R.id.orderdetails);

        orderdetails.setText("Name: " + DishName + " Price:" + Price);




        AddOrderButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //isLoading = true;
                Details = OrderCOmment.getText().toString();
                Quantity = orderQuantity.getText().toString();
                Toast.makeText(getApplicationContext(), "add order disheId:" + disheId + " TypeId: "
                        + TypeId + " TableId: " + TableId
                        + " Details: " + OrderCOmment.getText()
                        + " Quantity: " + orderQuantity.getText(), Toast.LENGTH_SHORT).show();

                getSupportLoaderManager().restartLoader(AddOrder_LOADER_ID, null, AddOrderCallback).forceLoad();

            }

        });

    }





    public static Intent createIntent(Context context, String disheId,
                                      String TypeId, String TableId,
                                      String Details, String Quantity,
                                      String DishName, String Price) {
        //Log.v(LOG_TAG, "table -> " + tableName + " tableId:" + tableId);
        Intent intent = new Intent(context, AddOrderActivity.class);
        intent.putExtra(MainActivity.ACTIVITY_disheId, disheId);
        intent.putExtra(MainActivity.ACTIVITY_TypeId, TypeId);
        intent.putExtra(MainActivity.ACTIVITY_TableId, TableId);
        intent.putExtra(MainActivity.ACTIVITY_Details, Details);
        intent.putExtra(MainActivity.ACTIVITY_Quantity, Quantity);
        intent.putExtra(MainActivity.ACTIVITY_DishName, DishName);
        intent.putExtra(MainActivity.ACTIVITY_Price, Price);

        return intent;

    }


    private LoaderManager.LoaderCallbacks<List<EntityDishes>> AddOrderCallback = new LoaderManager.LoaderCallbacks<List<EntityDishes>>() {




        @Override
        public Loader<List<EntityDishes>> onCreateLoader(int id, Bundle args) {
            Bundle bundle = new Bundle();
            bundle.putString("TOKEN_KEY", token);
            bundle.putString("disheId", disheId);
            bundle.putString("TypeId", TypeId);
            bundle.putString("TableId", TableId);
            bundle.putString("Details", Details);
            bundle.putString("Quantity", Quantity);

            Log.v(LOG_TAG, "onCreateLoader -> " );

            return new LoaderAddOrder(getApplicationContext(), bundle);
        }

        @Override
        public void onLoadFinished(Loader<List<EntityDishes>> loader, List<EntityDishes> data) {
            //tableAdapter.setTables(data);
        }

        @Override
        public void onLoaderReset(Loader<List<EntityDishes>> loader) {
            //tableAdapter.setTables(new ArrayList<TableEntity>());
        }
    } ;
}
