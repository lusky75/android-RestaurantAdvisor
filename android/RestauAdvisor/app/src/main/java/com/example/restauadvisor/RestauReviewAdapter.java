package com.example.restauadvisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RestauReviewAdapter extends BaseAdapter {
    private Context context;
    private List<Review> reviews;

    public RestauReviewAdapter(Context context, List reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() { return reviews.size(); }

    @Override
    public Review getItem(int position) { return reviews.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.review_row, null);
        Review review = reviews.get(position);

        TextView textRestaurant = (TextView) convertView.findViewById(R.id.restaurant);
        TextView textRating = (TextView) convertView.findViewById(R.id.rating);
        TextView textMessage = (TextView) convertView.findViewById(R.id.message);

        textRestaurant.setText(review.getUsername().replace("\"", ""));
        textRating.setText(review.getRating());
        textMessage.setText(review.getMessage().replace("\"", ""));
        return convertView;
    }
}
