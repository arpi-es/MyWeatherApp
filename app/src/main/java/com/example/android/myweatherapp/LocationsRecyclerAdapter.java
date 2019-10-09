package com.example.android.myweatherapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class LocationsRecyclerAdapter extends RecyclerView.Adapter<LocationsRecyclerAdapter.LocationsRecyclerAdapterHolder> {
    private final OnItemClickListener listener;
    private List<String> data  ;

    TextView txtLastLocation;

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public LocationsRecyclerAdapter(List<String> data, OnItemClickListener listener){
        this.data = data;
         this.listener = listener;
    }


    @NonNull
    @Override
    public LocationsRecyclerAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_item, viewGroup, false);
        LocationsRecyclerAdapterHolder forecastRecyclerAdapterHolder = new LocationsRecyclerAdapterHolder(v);
        return forecastRecyclerAdapterHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull LocationsRecyclerAdapterHolder locationsRecyclerAdapterHolder, int i) {
        locationsRecyclerAdapterHolder.bind( data.get(i), listener);

        txtLastLocation.setText(data.get(i));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    class LocationsRecyclerAdapterHolder extends RecyclerView.ViewHolder {


        public LocationsRecyclerAdapterHolder(@NonNull View itemView) {
            super(itemView);
            txtLastLocation = itemView.findViewById(R.id.location);
        }


        public void bind(final String item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }

    }

}