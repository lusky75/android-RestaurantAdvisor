package com.example.restauadvisor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetReviewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences preferences;
    private List<Review> reviews;
    private Retrofit retrofit;
    private ReviewAdapter reviewAdapter;
    private ReviewApi reviewApi;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_review);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ApiController controller = new ApiController();
        retrofit = controller.configureRetrofit();
        listView = (ListView) findViewById(R.id.listView);
        display(listView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.home:
                intent = new Intent(this, DisplayRestaurant.class);
                startActivity(intent);
            case R.id.create_restaurant:
                intent = new Intent(this, CreateRestaurantActivity.class);
                startActivity(intent);
                break;
            case R.id.my_review:
                intent = new Intent(this, GetReviewActivity.class);
                startActivity(intent);
                break;
            case R.id.disconnect:
                preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                if (preferences.getString("token", "") != "") {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("token");
                    editor.remove("admin");
                    editor.remove("username");
                    editor.commit();
                }
                startActivity(new Intent(this, Login.class));
                break;
        }
        return false;
    }

    public void display(final ListView listView) {
        reviewApi = retrofit.create(ReviewApi.class);
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmR5IiwiaXNzIjoid2Vic2VydmljZSIsIm5hbWUiOiJhbmR5IGtob3VyeSIsImFkbWluIjpmYWxzZX0.o6sPn671suVxtraHMaavMRVOtLL6-OAnDVlRgiTUDBk";
        Call<JsonObject> call = reviewApi.getReviews(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    displayReviews(response.body().get("ArrayList").getAsJsonArray(), listView);
                    //displayRestaurants(response.body().get("ArrayList").getAsJsonArray(), listView);
                } else {
                    System.out.println(response.errorBody().source());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void displayReviews(JsonArray jarr, ListView listView) {
        String username, restaurant, rating, message;
        Gson gson = new Gson();
        reviews = new ArrayList<>();
        for (int i = 0; i < jarr.size(); ++i) {
            JsonObject jobj = jarr.get(i).getAsJsonObject();
            username = jobj.get("username").toString();
            restaurant = jobj.get("restaurant").toString();
            rating = jobj.get("rating").toString();
            message = jobj.get("message").toString();
            preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String usernameShared = preferences.getString("username", "");
            if (username.equals(usernameShared)) {
                Review review = new Review(username, restaurant, message, rating);
                if (review != null)
                    reviews.add(review);
            }
        }
        this.reviewAdapter = new ReviewAdapter(getApplicationContext(), reviews);
        reviewAdapter.notifyDataSetChanged();
        listView.setAdapter(reviewAdapter);
    }
}
