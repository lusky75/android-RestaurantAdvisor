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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateRestaurantActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Retrofit retrofit;
    private RestaurantApi restaurantApi;
    private SharedPreferences preferences;
    private EditText nameText;
    private EditText descriptionText;
    private EditText locationText;
    private EditText phoneText;
    private EditText websiteText;
    private EditText ratingText;
    private EditText weekhourText;
    private EditText weekendhourText;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

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
        Button create = (Button) findViewById(R.id.create_restaurant);
        nameText = findViewById(R.id.name);
        descriptionText = findViewById(R.id.description);
        locationText = findViewById(R.id.location);
        phoneText = findViewById(R.id.location);
        websiteText = findViewById(R.id.website);
        ratingText = findViewById(R.id.website);
        weekhourText = findViewById(R.id.weekhour);
        weekendhourText = findViewById(R.id.weekendhour);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameText.getText().toString().isEmpty()) {
                    nameText.setError("Restaurant'name field required");
                    return;
                }
                if (locationText.getText().toString().isEmpty()) {
                    locationText.setError("Restaurant's location field required");
                    return;
                }
                createRestaurant();
            }
        });
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
                break;
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

    public void createRestaurant() {
        preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String admin = preferences.getString("admin", "");
        if (admin.equals("false")) {
            Toast.makeText(this, "Your are not admin, you cannot create !", Toast.LENGTH_SHORT).show();
            return;
        }
        String token = preferences.getString("token", "");
        if (token.equals("")) {
            Toast.makeText(this, "You are not connect as admin !", Toast.LENGTH_SHORT).show();
            return;
        }
        token = token.replace("\"", "");
        String name = nameText.getText().toString();
        String description = descriptionText.getText().toString();
        String location = locationText.getText().toString();
        String phone = phoneText.getText().toString();
        String website = websiteText.getText().toString();
        String rating = ratingText.getText().toString();
        String weekhour = weekhourText.getText().toString();
        String weekendhour = weekendhourText.getText().toString();
        restaurantApi = retrofit.create(RestaurantApi.class);
        Call<JsonObject> call = restaurantApi.createRestaurant(token, name, description, location, phone, website, rating, weekhour, weekendhour);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    System.out.println(response.body());
                    Intent intent = new Intent(CreateRestaurantActivity.this, DisplayRestaurant.class);
                    startActivity(intent);
                } else {
                    System.out.println(response.errorBody().source());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

}
