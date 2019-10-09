package com.example.android.myweatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class SetLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        Button btSearch = findViewById(R.id.btnSearch);
        final EditText edtSearchLocation = findViewById(R.id.edtSearchLocation) ;
        RecyclerView recyclerView = findViewById(R.id.recyclerLocations);



        final MySQLHelper mySQLHelper = new MySQLHelper(SetLocationActivity.this, "dbMovies", null, 1);
        List<String> lastLocations =  mySQLHelper.getLocations();

        LocationsRecyclerAdapter adapter = new LocationsRecyclerAdapter(lastLocations, new LocationsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                setResult(item);
            }
        });



        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SetLocationActivity.this, RecyclerView.VERTICAL, false));




        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                String search = edtSearchLocation.getText().toString().trim();

                if (!(mySQLHelper.isExist(search))){
                    mySQLHelper.inserToDB(search);
                }

                setResult(search);

            }
        });

    }


    private void setResult(String searchedLocation) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",searchedLocation);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
