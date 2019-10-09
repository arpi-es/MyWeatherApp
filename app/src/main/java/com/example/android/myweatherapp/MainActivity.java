package com.example.android.myweatherapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myweatherapp.Weather.Datum;
import com.example.android.myweatherapp.Weather.WeatherClass;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private android.support.v7.widget.Toolbar toolbar;

    private TextView txtLocation;
    private TextView txtDay;
    private TextView txtTemperature;
    private TextView txtDescription;
    private ImageView imgIconWeather;
    private RecyclerView recyclerView;

    private ProgressDialog dialog;

    private String BaseURL = "https://api.weatherbit.io/v2.0/forecast/daily?days=6&key=d38c0bd9cf044b6f9b884d50869073db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        Button btnDrawer = findViewById(R.id.btnDrawer);
        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer();
            }
        });

        fillDrawer();


        txtLocation = findViewById(R.id.txtLocation);
        txtDay = findViewById(R.id.txtDay);
        txtTemperature = findViewById(R.id.txtTemperature);
        txtDescription = findViewById(R.id.txtDescription);
        imgIconWeather = findViewById(R.id.imgWeatherIcon);

        txtLocation.setText("Tehran, IR");
        txtDay.setText("Today");
        txtTemperature.setText("--");

        recyclerView = findViewById(R.id.RecyclerForcast);

        Search("Tehran,IR");
    }


    public void fillDrawer(){
        TextView menu_SelectCity =  (TextView) findViewById(R.id.menu_SelectCity);
        TextView menu_About = (TextView)  findViewById(R.id.menu_About);


        menu_SelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetLocationActivity.class);
                startActivityForResult(intent, 100);
                closeDrawer();
            }
        });



        menu_About.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "App Design and Development by Arpi Es", Toast.LENGTH_LONG).show();

            }

        });



    }


    public void openDrawer() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void Search(String sSearch) {

        if (!isNetworkAvailable()) {
            Toast.makeText(MainActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();

        } else {

            recyclerView.removeAllViews();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading Data, please wait...");
            dialog.show();

            AsyncHttpClient client = new AsyncHttpClient();

            String finalURL = BaseURL + "&city=" + sSearch;
            client.get(finalURL, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (statusCode == 200) {

                        try {
                            Gson gson = new Gson();
                            WeatherClass weatherClass = gson.fromJson(response.toString(), WeatherClass.class);
                            FillData(weatherClass);

                        } catch (Exception e) {
                            Log.e("MYTAG", e.getMessage());
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No Result Found", Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    Toast.makeText(MainActivity.this, "Error Loading data." + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }

            });

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                Search(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private void FillData(WeatherClass weatherClass) {

        txtLocation.setText(weatherClass.getCityName() + ", " + weatherClass.getCountryCode());
        txtDay.setText("Today");

        List<Datum> data = weatherClass.getData();

        if (data.size() != 0) {

            Datum TodayData = data.remove(0);

            FillTodayInfo(TodayData);

            FillRecyclerView(data);
        }

    }


    public void FillTodayInfo(Datum TodayData) {

        try {
            String sTemperature = " " + TodayData.getTemp().intValue() + "º";
            txtTemperature.setText(sTemperature);
            txtDescription.setText(TodayData.getWeather().getDescription());


            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(TodayData.getDatetime());

            String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
            String day = (String) DateFormat.format("dd", date); // 20
            String monthString = (String) DateFormat.format("MMM", date); // Jun

            txtDay.setText(dayOfTheWeek + ", " + monthString + " " + day);

            String uri = "@drawable/" + TodayData.getWeather().getIcon();
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            imgIconWeather.setImageDrawable(res);


        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }

    }

    public void FillRecyclerView(List<Datum> data) {

        try {
            ForecastRecyclerAdapter adapter = new ForecastRecyclerAdapter(data, this);

            recyclerView.removeAllViews();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));


        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }


    }
}