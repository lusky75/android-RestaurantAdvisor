package com.example.restauadvisor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences preferences;
    private RestaurantApi restaurantApi;
    private Retrofit retrofit;
    private TextView name;
    private TextView description;
    private TextView location;
    private TextView phone;
    private TextView website;
    private TextView rating;
    private TextView weekEndHour;
    private TextView weekHour;
    private TextView review;
    private MenuAdapter menuAdapter;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
        Button delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                String admin = preferences.getString("admin", "");
                String token = preferences.getString("token", "");
                System.out.println("delete !");
                delete(getIntent().getStringExtra("extra_id"), admin, token);
            }
        });
        Button update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                String admin = preferences.getString("admin", "");
                String token = preferences.getString("token", "");
                if (token.equals("")) {
                    Toast.makeText(MenuActivity.this, "You must connect as admin to update !", Toast.LENGTH_SHORT).show();
                    return;
                } else if (admin.equals("false")) {
                    Toast.makeText(MenuActivity.this, "You are not admin, you cannot update !", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent intent = new Intent(MenuActivity.this, UpdateRestauActivity.class);
                    intent.putExtra("extra_id", getIntent().getStringExtra("extra_id"));
                    intent.putExtra("extra_name", getIntent().getStringExtra("extra_name"));
                    intent.putExtra("extra_description", getIntent().getStringExtra("extra_description"));
                    intent.putExtra("extra_location", getIntent().getStringExtra("extra_location"));
                    intent.putExtra("extra_phone", getIntent().getStringExtra("extra_phone"));
                    intent.putExtra("extra_website", getIntent().getStringExtra("extra_website"));
                    intent.putExtra("extra_rating", getIntent().getStringExtra("extra_rating"));
                    intent.putExtra("extra_weekHour", getIntent().getStringExtra("extra_weekHour"));
                    intent.putExtra("extra_weekEndHour", getIntent().getStringExtra("extra_weekEndHour"));
                    startActivity(intent);
                }
            }
        });
        review = (TextView)findViewById(R.id.restaurant_review);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ReviewActivity.class);
                intent.putExtra("extra_restaurant_id", getIntent().getStringExtra("extra_id"));
                startActivity(intent);
            }
        });
        name = findViewById(R.id.restaurant_name);
        description = findViewById(R.id.restaurant_description);
        location = findViewById(R.id.restaurant_location);
        phone = findViewById(R.id.restaurant_phone);
        website = findViewById(R.id.restaurant_website);
        rating = findViewById(R.id.restaurant_rating);
        weekHour = findViewById(R.id.restaurant_weekHour);
        weekEndHour = findViewById(R.id.restaurant_weekEndHour);
        listView = (ListView) findViewById(R.id.listView);
        if (getIntent().getExtras() != null) {
            if (!getIntent().getStringExtra("extra_name").equals("null"))
                name.setText("name : " + getIntent().getStringExtra("extra_name"));
            if (!getIntent().getStringExtra("extra_description").equals("null"))
                description.setText("description : " + getIntent().getStringExtra("extra_description"));
            if (!getIntent().getStringExtra("extra_location").equals("null"))
                location.setText("location : " + getIntent().getStringExtra("extra_location"));
            if (!getIntent().getStringExtra("extra_phone").equals("null"))
                phone.setText("phone : " + getIntent().getStringExtra("extra_phone"));
            if (!getIntent().getStringExtra("extra_website").equals("null"))
                website.setText("website : " + getIntent().getStringExtra("extra_website"));
            if (!getIntent().getStringExtra("extra_rating").equals("null"))
                rating.setText("rating : " + getIntent().getStringExtra("extra_rating"));
            if (!getIntent().getStringExtra("extra_weekHour").equals("null"))
                weekHour.setText("week open : " + getIntent().getStringExtra("extra_weekHour"));
            if (!getIntent().getStringExtra("extra_weekEndHour").equals("null"))
                weekEndHour.setText("week end open : " + getIntent().getStringExtra("extra_weekEndHour"));
            String MenusAsString = getIntent().getStringExtra("extra_listMenus");

            Gson gson = new Gson();
            Type type = new TypeToken<List<Menu>>(){}.getType();
            List<Menu> menus = gson.fromJson(MenusAsString, type);
            this.menuAdapter = new MenuAdapter(getApplicationContext(), menus);
            menuAdapter.notifyDataSetChanged();
            listView.setAdapter(menuAdapter);
        }
    }

    public void delete(String id, String admin, String token) {
        restaurantApi = retrofit.create(RestaurantApi.class);
        if (!admin.equals("false")) {
            Call<JsonObject> call = restaurantApi.deleteRestaurant(id, token.replace("\"", ""));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        System.out.println(response.body());
                        startActivity(new Intent(MenuActivity.this, DisplayRestaurant.class));
                    } else {
                        System.out.println("ok : " + response.errorBody().source());
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    System.out.println(t.toString());
                }
            });
        } else {
            Toast.makeText(this, "You are not admin, you cannot delete !", Toast.LENGTH_SHORT).show();
            return;
        }
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
}
