package com.example.android.myweatherapp;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        TextView txtLocation = findViewById(R.id.txtLocation);
        TextView txtDay = findViewById(R.id.txtDay);
        TextView txtTemperature = findViewById(R.id.txtTemperature);
        TextView txtDescription = findViewById(R.id.txtDescription);

        txtLocation.setText("Tehran, IR");
        txtDay.setText("Today");
        txtTemperature.setText("54º");
        txtDescription.setText("Few clouds");


        RecyclerView recyclerView = findViewById(R.id.RecyclerForcast);
        ForecastRecyclerAdapter adapter = new ForecastRecyclerAdapter();

        recyclerView.removeAllViews();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));

    }
}
