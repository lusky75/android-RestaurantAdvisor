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
import android.widget.Toast;

import retrofit2.Retrofit;

public class UpdateRestauActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
        setContentView(R.layout.activity_update_restau);

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

        fillEditText();
        Button update = (Button) findViewById(R.id.update_restaurant);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRestau();
            }
        });
        retrofit = controller.configureRetrofit();
    }

    public void fillEditText() {
        Restaurant restaurant = new Restaurant(getIntent().getStringExtra("extra_id"),
                getIntent().getStringExtra("extra_name"),
                getIntent().getStringExtra("extra_descriptoin"),
                getIntent().getStringExtra("extra_location"),
                getIntent().getStringExtra("extra_phone"),
                getIntent().getStringExtra("extra_website"),
                getIntent().getStringExtra("extra_rating"),
                getIntent().getStringExtra("extra_weekEndHour"),
                getIntent().getStringExtra("extra_weekHour"));
        nameText = (EditText)findViewById(R.id.name);
        descriptionText = (EditText)findViewById(R.id.description);
        locationText = (EditText)findViewById(R.id.location);
        phoneText = (EditText)findViewById(R.id.location);
        websiteText = (EditText)findViewById(R.id.website);
        ratingText = (EditText)findViewById(R.id.website);
        weekhourText = (EditText)findViewById(R.id.weekhour);
        weekendhourText = (EditText)findViewById(R.id.weekendhour);
        if (!getIntent().getStringExtra("extra_name").equals("null"))
            nameText.setText(restaurant.getName());
        if (!getIntent().getStringExtra("extra_description").equals("null"))
            descriptionText.setText(restaurant.getDescription());
        if (!getIntent().getStringExtra("extra_location").equals("null"))
            locationText.setText(restaurant.getLocation());
        if (!getIntent().getStringExtra("extra_phone").equals("null"))
            phoneText.setText(restaurant.getPhone());
        if (!getIntent().getStringExtra("extra_website").equals("null"))
            websiteText.setText(restaurant.getWebsite());
        if (!getIntent().getStringExtra("extra_rating").equals("null"))
            ratingText.setText(restaurant.getRating());
        if (!getIntent().getStringExtra("extra_weekEndHour").equals("null"))
            weekendhourText.setText(restaurant.getWeekEndHour());
        if (!getIntent().getStringExtra("extra_weekHour").equals("null"))
            weekhourText.setText(restaurant.getWeekHour());
    }

    public void updateRestau() {
        restaurantApi = retrofit.create(RestaurantApi.class);

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
                Toast.makeText(this, "disconnect", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
