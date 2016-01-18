package com.olga.twa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewOrderActivity extends AppCompatActivity {

    Button bFOOD = null;
    Button bFOODMILK = null;
    Button bSUSHI = null;
    Button bDRINK = null;
    Button bDRINKALCOHOL = null;

    String tableID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        addListenerOnButtons();

        Bundle extras = getIntent().getExtras();
        tableID = (String) extras.get(MainActivity.ACTIVITY_TableId);

    }

    public void addListenerOnButtons() {

        bFOOD = (Button) findViewById(R.id.bFOOD);
        bFOODMILK = (Button) findViewById(R.id.bFOODMILK);
        bSUSHI = (Button) findViewById(R.id.bSUSHI);
        bDRINK = (Button) findViewById(R.id.bDRINK);
        bDRINKALCOHOL = (Button) findViewById(R.id.bDRINKALCOHOL);

        bFOOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = DishesActivity.createIntent(getApplicationContext(), tableID);
                startActivity(intent);
            }

        });
        bFOODMILK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = DishesActivity.createIntent(getApplicationContext(), tableID);
                startActivity(intent);
            }

        });
        bSUSHI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = DishesActivity.createIntent(getApplicationContext(), tableID);
                startActivity(intent);
            }

        });
        bDRINK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = DishesActivity.createIntent(getApplicationContext(), tableID);
                startActivity(intent);
            }

        });
        bDRINKALCOHOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = DishesActivity.createIntent(getApplicationContext(), tableID);
                startActivity(intent);
            }

        });
    }

    public static Intent createIntent(Context context, String tableId)
    {
        //Log.v(LOG_TAG, "table -> " + tableName + " tableId:" + tableId);
        Intent intent = new Intent(context, NewOrderActivity.class);
        intent.putExtra(MainActivity.ACTIVITY_TableId, tableId);
        return intent;

    }
}
