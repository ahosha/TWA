package com.olga.twa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    Button LoginButton;
    Boolean isLoading = false;

    final int LOGIN_LOADER_ID = 1;
    final String IsLoadingStr = "isLoading";


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(IsLoadingStr, isLoading.toString());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton = (Button) findViewById(R.id.log_in_button);

        if (savedInstanceState != null) {
            isLoading = savedInstanceState.getBoolean(IsLoadingStr);
        }
        if (isLoading) {
            getSupportLoaderManager().initLoader(LOGIN_LOADER_ID, null, LoginCallback);
        }


        LoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                isLoading = true;


                getSupportLoaderManager().initLoader(LOGIN_LOADER_ID, null, LoginCallback);

            }

        });
    }


    private LoaderManager.LoaderCallbacks<List<EntityLogin>> LoginCallback = new LoaderManager.LoaderCallbacks<List<EntityLogin>>() {


        @Override
        public Loader<List<EntityLogin>> onCreateLoader(int id, Bundle args) {
/*            Bundle bundle = new Bundle();
            AutoCompleteTextView username = (AutoCompleteTextView) findViewById(R.id.email);
            bundle.putString(MainActivity.USER_NAME, username.getText().toString());
            EditText userid = (EditText) findViewById(R.id.login);
            bundle.putString(MainActivity.USER_NAME, userid.getText().toString());*/

            String un = "maxim.langman";
            String ui = "12345";

            Bundle bundle = new Bundle();

            AutoCompleteTextView usernametb = (AutoCompleteTextView) findViewById(R.id.email);
            if (usernametb != null) {
                un = usernametb.getText().toString();
            }
            bundle.putString(MainActivity.USER_NAME, un );

            EditText useridtb = (EditText) findViewById(R.id.login);
            if (useridtb != null) {
                ui=  useridtb.getText().toString();
            }
            bundle.putString(MainActivity.USER_PASSWORD,ui);

            //Toast.makeText(getApplicationContext(), "before loader  un:" + un + " ui: " + ui , Toast.LENGTH_SHORT).show();

            return new LoaderLogin(getApplicationContext(), bundle);
        }

        @Override
        public void onLoadFinished(Loader<List<EntityLogin>> loader, List<EntityLogin> data) {
            //Toast.makeText(getApplicationContext(), "after loader  data:" + data , Toast.LENGTH_SHORT).show();

            if (data != null) {
                String token = data.get(0).access_token;
                PutTokenToShare(token);
                //Toast.makeText(getApplicationContext(), "after loader  token:" + token , Toast.LENGTH_SHORT).show();

                Intent intent = TablesListActivity.createIntent(getApplicationContext());
                startActivity(intent);
            }
        }

        private void PutTokenToShare(String token) {
            SharedPreferences preferences = getSharedPreferences(MainActivity.SHARED_PREF_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(MainActivity.TOKEN_KEY, token);
            editor.apply();
        }

        @Override
        public void onLoaderReset(Loader<List<EntityLogin>> loader) {

        }

    };



}
