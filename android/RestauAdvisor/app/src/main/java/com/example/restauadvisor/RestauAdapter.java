package com.example.restauadvisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RestauAdapter extends BaseAdapter {

    private Context context;
    private List<Restaurant> restaurants;

    public RestauAdapter(Context context, List restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @Override
    public int getCount() { return restaurants.size(); }

    @Override
    public Restaurant getItem(int position) { return restaurants.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.restaurant_row, null);
        Restaurant restaurant = restaurants.get(position);

        TextView textViewRestaurantName = (TextView) convertView.findViewById(R.id.restaurant_name);
        //TextView textViewRestaurantId = (TextView) convertView.findViewById((R.id.restaurant_id));

        textViewRestaurantName.setText(restaurant.getName().replace("\"", ""));
        //textViewRestaurantId.setText(restaurant.getId());
        return convertView;
    }
}