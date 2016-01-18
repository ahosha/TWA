package com.olga.twa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ListView;



public class TablesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<TableEntity>> {

    String token = null;
    AdapterTables tableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables_list);

        SharedPreferences preferences = getSharedPreferences(MainActivity.SHARED_PREF_KEY, MODE_PRIVATE);
        token = preferences.getString(MainActivity.TOKEN_KEY,"");

        tableAdapter = new AdapterTables(this, new ArrayList<TableEntity>());
        ListView tableListView = (ListView) findViewById(R.id.tablelistview);
        tableListView.setAdapter(tableAdapter);

        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TableEntity table = (TableEntity) tableAdapter.getItem(position);

                //Toast.makeText(getApplicationContext(), "tableName to send: " + table.tablename, Toast.LENGTH_SHORT).show();

                Intent intent = SpecificTableActivity.createIntent(getApplicationContext(), table.tablename, table.tableid);
                startActivity(intent);

            }
        });
        getSupportLoaderManager().initLoader(MainActivity.TABLE_LIST_LOADER_ID, null, this);
   }

    public static Intent createIntent(Context context){

        Intent intent = new Intent(context, TablesListActivity.class);
        return intent;
    }

    @Override
    public Loader<List<TableEntity>> onCreateLoader(int id, Bundle args) {
        Bundle bundle = new Bundle();
        bundle.putString("TOKEN_KEY", token);
        return new LoaderTables(getApplicationContext(),bundle);
    }
    @Override
    public void onLoadFinished(Loader<List<TableEntity>> loader, List<TableEntity> data) {
        tableAdapter.setTables(data);
    }
    @Override
    public void onLoaderReset(Loader<List<TableEntity>> loader) {
        tableAdapter.setTables(new ArrayList<TableEntity>());
    }


}
