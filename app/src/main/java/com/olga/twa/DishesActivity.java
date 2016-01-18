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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DishesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EntityDishes>> {

    private final String LOG_TAG = DishesActivity.class.getSimpleName();
    String token = null;
    AdapterDishes dishesAdapter;
    public static final int DISHES_LIST_LOADER_ID = 1;
    String tableID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes);

        SharedPreferences preferences = getSharedPreferences(MainActivity.SHARED_PREF_KEY, MODE_PRIVATE);
        token = preferences.getString(MainActivity.TOKEN_KEY,"");
        Bundle extras = getIntent().getExtras();
        tableID = (String) extras.get(MainActivity.ACTIVITY_TableId);

        getSupportLoaderManager().initLoader(DISHES_LIST_LOADER_ID, null, this);
        Log.v(LOG_TAG, "initLoader done");

        dishesAdapter = new AdapterDishes(this, new ArrayList<EntityDishes>());
        ListView dishesListView = (ListView) findViewById(R.id.disheslistview);
        dishesListView.setAdapter(dishesAdapter);

        dishesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EntityDishes dish = (EntityDishes) dishesAdapter.getItem(position);
                //Toast.makeText(getApplicationContext(), "dish to send: " + Name + " + dish.ID"  + dish.ID, Toast.LENGTH_SHORT).show();
                Intent intent = AddOrderActivity.createIntent(getApplicationContext(), dish.ID,
                        dish.DishType, tableID, "", "", dish.Name, dish.Price);
                startActivity(intent);

            }
        });




    }

    public static Intent createIntent(Context context, String tableId)
    {
        //Log.v(LOG_TAG, "table -> " + tableName + " tableId:" + tableId);
        Intent intent = new Intent(context, DishesActivity.class);
        intent.putExtra(MainActivity.ACTIVITY_TableId, tableId);
        return intent;

    }

    @Override
    public Loader<List<EntityDishes>> onCreateLoader(int id, Bundle args) {
        Bundle bundle = new Bundle();
        bundle.putString("TOKEN_KEY", token);
        return new LoaderDishes(getApplicationContext(),bundle);
    }

    @Override
    public void onLoadFinished(Loader<List<EntityDishes>> loader, List<EntityDishes> data) {
        dishesAdapter.setTables(data);
    }

    @Override
    public void onLoaderReset(Loader<List<EntityDishes>> loader) {
        dishesAdapter.setTables(new ArrayList<EntityDishes>());
    }



}
