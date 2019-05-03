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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private Retrofit retrofit;
    private List<Review> reviews;
    private RestauReviewAdapter reviewAdapter;
    private SharedPreferences preferences;
    private DrawerLayout drawerLayout;
    private ReviewApi reviewApi;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
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
        String id = getIntent().getStringExtra("extra_restaurant_id");
        displayReviewOfRestaurant(listView, id);
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

    public void displayReviewOfRestaurant(final ListView listView, String id) {
        reviewApi = retrofit.create(ReviewApi.class);
        preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        //if (preferences.getString("token", "") != "") {
        String token = preferences.getString("token", "");
        token = token.replace("\"", "");
        Call<JsonObject> call = reviewApi.getReviewByRestaurant(token, id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                displayReviews(response.body().get("ArrayList").getAsJsonArray(), listView);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void displayReviews(JsonArray jarr, ListView listView) {
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
        this.reviewAdapter = new RestauReviewAdapter(getApplicationContext(), reviews);
        reviewAdapter.notifyDataSetChanged();
        listView.setAdapter(reviewAdapter);
    }
}
