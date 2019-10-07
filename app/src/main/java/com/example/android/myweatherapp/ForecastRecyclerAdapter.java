package com.example.android.myweatherapp;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myweatherapp.Weather.Datum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.ForecastRecyclerAdapterHolder> {

    private List<Datum> data  ;
    private Context context;

    public  ForecastRecyclerAdapter( List<Datum> data, Context context){
            this.data = data;
            this.context = context;
    }
    @NonNull
    @Override
    public ForecastRecyclerAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forcast_item, viewGroup, false);
        ForecastRecyclerAdapterHolder forecastRecyclerAdapterHolder = new ForecastRecyclerAdapterHolder(v);
        return forecastRecyclerAdapterHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ForecastRecyclerAdapterHolder forcastRecyclerAdapterHolder, int i) {


        Datum data1 = data.get(i) ;

        String dayName = "SAT" ;
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(data1.getDatetime());
            SimpleDateFormat outFormat = new SimpleDateFormat("E");
            dayName = outFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        forcastRecyclerAdapterHolder.txtDay.setText( dayName.toUpperCase());
        forcastRecyclerAdapterHolder.txtInfo.setText(data1.getHighTemp().toString() + "º/" + data1.getLowTemp().toString() + "º" );

        String uri = "@drawable/" + data1.getWeather().getIcon();

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable res = context.getResources().getDrawable(imageResource);
        forcastRecyclerAdapterHolder.imgIcon.setImageDrawable(res);



    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    class ForecastRecyclerAdapterHolder extends RecyclerView.ViewHolder {

        TextView txtDay;
        ImageView imgIcon;
        TextView txtInfo;

        public ForecastRecyclerAdapterHolder(@NonNull View itemView) {
            super(itemView);
            txtDay = itemView.findViewById(R.id.txtDay);
            imgIcon = itemView.findViewById(R.id.imgWeatherSmallIcon);
            txtInfo = itemView.findViewById(R.id.txtInfo);
        }

    }

}