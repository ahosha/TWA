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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SpecificTableActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EntityOrder>>{

    private final String LOG_TAG = SpecificTableActivity.class.getSimpleName();
    String token = null;
    AdapterOrders ordersAdapter;
    ArrayAdapter<String> mSpecTableListAdapter = null;
    Button AddButton;
    String tableId = "0";
    int ORDER_LIST_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_table);

        addListenerOnButton();

        SharedPreferences preferences = getSharedPreferences(MainActivity.SHARED_PREF_KEY, MODE_PRIVATE);
        token = preferences.getString(MainActivity.TOKEN_KEY, "");

        Bundle extras = getIntent().getExtras();
        String tablename = (String) extras.get(MainActivity.ACTIVITY_TABLE_NAME);
        tableId = (String) extras.get(MainActivity.ACTIVITY_TABLE_ID);

        TextView tableNameTextView = (TextView) findViewById(R.id.tableNameTextView);
        tableNameTextView.setText(tablename);


        ordersAdapter = new AdapterOrders(this, new ArrayList<EntityOrder>());
        ListView orderListView = (ListView) findViewById(R.id.specifictablelistview);
        orderListView.setAdapter(ordersAdapter);

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EntityOrder order = (EntityOrder) ordersAdapter.getItem(position);

                Toast.makeText(getApplicationContext(), "order: " + order.tableNameID, Toast.LENGTH_SHORT).show();

/*                Intent intent = SpecificTableActivity.createIntent(getApplicationContext(), table.tablename, table.tableid);
                startActivity(intent);*/

            }
        });
        getSupportLoaderManager().initLoader(ORDER_LIST_LOADER_ID, null, this);
    }

    public void addListenerOnButton() {

        AddButton = (Button) findViewById(R.id.newOrderButton);

        AddButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                Intent intent = NewOrderActivity.createIntent(getApplicationContext(), tableId);
                startActivity(intent);


            }

        });
    }



    public static Intent createIntent(Context context, String tableName, String tableId){
        Log.v("SpecificTableActivity", "table -> " + tableName + " tableId:"+ tableId );
        Intent intent = new Intent(context, SpecificTableActivity.class);
        intent.putExtra(MainActivity.ACTIVITY_TABLE_NAME, tableName);
        intent.putExtra(MainActivity.ACTIVITY_TABLE_ID, tableId);
        return intent;
    }


    @Override
    public Loader<List<EntityOrder>> onCreateLoader(int id, Bundle args) {
        Bundle bundle = new Bundle();
        bundle.putString("TOKEN_KEY", token);
        bundle.putString(MainActivity.ACTIVITY_TableId, tableId);

        return new LoaderOrders(getApplicationContext(),bundle);
    }
    @Override
    public void onLoadFinished(Loader<List<EntityOrder>> loader, List<EntityOrder> data) {
        ordersAdapter.setTables(data);
    }
    @Override
    public void onLoaderReset(Loader<List<EntityOrder>> loader) {
        ordersAdapter.setTables(new ArrayList<EntityOrder>());
    }

}