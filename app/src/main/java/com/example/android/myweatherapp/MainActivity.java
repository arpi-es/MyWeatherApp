package com.example.android.myweatherapp;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.widget.Adapter;
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
import java.util.PriorityQueue;

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


    private String BaseURL = "https://api.weatherbit.io/v2.0/forecast/daily?days=6&key=d38c0bd9cf044b6f9b884d50869073db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mDrawerLayout = findViewById(R.id.drawer_layout);

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


    public void Search(String sSearch) {
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
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MainActivity.this, "Error Loading data." + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }

        });


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
            txtTemperature.setText( sTemperature  );
            txtDescription.setText(TodayData.getWeather().getDescription());


            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(TodayData.getDatetime());

            String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
            String day          = (String) DateFormat.format("dd",   date); // 20
            String monthString  = (String) DateFormat.format("MMM",  date); // Jun

            txtDay.setText(dayOfTheWeek + ", " + monthString + " " + day );


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