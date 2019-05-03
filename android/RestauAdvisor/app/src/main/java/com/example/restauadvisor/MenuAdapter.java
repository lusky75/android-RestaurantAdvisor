package com.example.restauadvisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private Context context;
    private List<Menu> menus;

    public MenuAdapter(Context context, List menus) {
        this.context = context;
        this.menus = menus;
    }

    @Override
    public int getCount() { return menus.size(); }

    @Override
    public Menu getItem(int position) { return menus.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.menu_row, null);
        Menu menu = menus.get(position);

        TextView textViewMenuName = (TextView) convertView.findViewById(R.id.menu_name);
        TextView textViewMenuPrice = (TextView) convertView.findViewById(R.id.menu_price);
        TextView textViewMenuDescription = (TextView) convertView.findViewById(R.id.menu_description);
        //TextView textViewRestaurantId = (TextView) convertView.findViewById((R.id.restaurant_id));

        if (!menu.getName().equals("null"))
            textViewMenuName.setText(menu.getName().toString().replace("\"", ""));
        if (!menu.getId().equals("null"))
            textViewMenuPrice.setText(menu.getPrice() + " euros");
        if (!menu.getDescription().equals("null"))
            textViewMenuDescription.setText(menu.getDescription().replace("\"", ""));
        //textViewRestaurantId.setText(restaurant.getId());
        return convertView;
    }
}