package com.example.android.myweatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.ForecastRecyclerAdapterHolder> {


    @NonNull
    @Override
    public ForecastRecyclerAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forcast_item, viewGroup, false);
        ForecastRecyclerAdapterHolder forecastRecyclerAdapterHolder = new ForecastRecyclerAdapterHolder(v);
        return forecastRecyclerAdapterHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ForecastRecyclerAdapterHolder forcastRecyclerAdapterHolder, int i) {
        forcastRecyclerAdapterHolder.txtDay.setText( "SAT");
      //  Picasso.get().load(items.get(i).getPoster()).into(movieRecyclerAdapterHolder.imgPoster);
        forcastRecyclerAdapterHolder.txtInfo.setText("62/72");
        forcastRecyclerAdapterHolder.imgIcon.setImageResource(R.drawable.a04d);
    }

    @Override
    public int getItemCount() {
        return 5;
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