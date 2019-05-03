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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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


public class DisplayRestaurant extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences preferences;
    private TextView profile;
    private RestaurantApi restaurantApi;
    private UserApi userApi;
    private Retrofit retrofit;
    private RestauAdapter restauAdapter;
    private List<Restaurant> restaurants;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_restaurant);

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
        authentication();
        listView = (ListView) findViewById(R.id.listView);

        final EditText searchRestaurant = (EditText) findViewById(R.id.search);
        Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                search(searchRestaurant.getText().toString());
            }
        });
        Button display = findViewById(R.id.display);
        display.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                display(listView);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(DisplayRestaurant.this, MenuActivity.class);
                //System.out.println(id);
                Restaurant restaurant = restauAdapter.getItem(position);
                startMenuActivity(restaurant);
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
                login();
                authentication();
                break;
        }
        return false;
    }

    public void login() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void authentication() {
        preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        if (preferences.getString("token", "") != "") {
            String token = preferences.getString("token", "");
            token = token.replace("\"", "");
            userApi = retrofit.create(UserApi.class);
            Call<JsonObject> call = userApi.getUserProfile(token);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        //profile.setText();
                        //String username = response.body().get("User");
                        //profile.setText("Welcome " + username + " !");
                        profile = (TextView) findViewById(R.id.profile);
                        if (response.body().get("success").toString().equals("true"))
                            profile.setText(response.body().get("User").getAsJsonObject().get("username").toString().replace("\"", ""));
                    } else {
                        System.out.println(response.body());
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    System.out.println(t.toString());
                }
            });
        }
    }

    public void startMenuActivity(Restaurant restaurant) {
        Gson gson = new Gson();
        String jsonMenus = gson.toJson(restaurant.getMenus());
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("extra_id", restaurant.getId());
        intent.putExtra("extra_name", restaurant.getName());
        intent.putExtra("extra_description", restaurant.getDescription());
        intent.putExtra("extra_location", restaurant.getLocation());
        intent.putExtra("extra_phone", restaurant.getPhone());
        intent.putExtra("extra_website", restaurant.getWebsite());
        intent.putExtra("extra_rating", restaurant.getRating());
        intent.putExtra("extra_weekEndHour", restaurant.getWeekEndHour());
        intent.putExtra("extra_weekHour", restaurant.getWeekHour());

        intent.putExtra("extra_listMenus", jsonMenus);
        startActivity(intent);
    }

    public void search(String search) {
        restaurantApi = retrofit.create(RestaurantApi.class);
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmR5IiwiaXNzIjoid2Vic2VydmljZSIsIm5hbWUiOiJhbmR5IGtob3VyeSIsImFkbWluIjpmYWxzZX0.o6sPn671suVxtraHMaavMRVOtLL6-OAnDVlRgiTUDBk";
        Call<JsonObject> call = restaurantApi.getRestaurantsByName(search, token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null)
                    displayRestaurants(response.body().get("ArrayList").getAsJsonArray(), listView);
                else
                    Toast.makeText(DisplayRestaurant.this, "No found restaurant", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    public void display(final ListView listView) {
        restaurantApi = retrofit.create(RestaurantApi.class);
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmR5IiwiaXNzIjoid2Vic2VydmljZSIsIm5hbWUiOiJhbmR5IGtob3VyeSIsImFkbWluIjpmYWxzZX0.o6sPn671suVxtraHMaavMRVOtLL6-OAnDVlRgiTUDBk";
        Call<JsonObject> call = restaurantApi.getRestaurants(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    displayRestaurants(response.body().get("ArrayList").getAsJsonArray(), listView);
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

    private void displayRestaurants(JsonArray jarr, ListView listView) {
        String id, name, description, location, phone, website, rating, weekEndHour, weekHour;
        JsonObject jobj;
        Gson gson = new Gson();
        restaurants = new ArrayList<>();
        for (int i = 0; i < jarr.size(); ++i) {
            jobj = jarr.get(i).getAsJsonObject();
            id = jobj.get("id").toString();
            name = jobj.get("name").toString();
            description = jobj.get("description").toString();
            location = jobj.get("location").toString();
            phone = jobj.get("phone").toString();
            website = jobj.get("website").toString();
            rating = jobj.get("rating").toString();
            weekEndHour = jobj.get("weekEndHour").toString();
            weekHour = jobj.get("weekHour").toString();
            Restaurant restaurant = new Restaurant(id, name, description, location, phone, website, rating, weekEndHour, weekHour);
            if (!jobj.get("menu").toString().equals("null")) {
                JsonArray menus = jobj.get("menu").getAsJsonArray();
                for (int j = 0; j < menus.size(); j++) {
                    JsonObject menuObject = menus.get(j).getAsJsonObject();
                    Menu menu = new Menu(menuObject.get("id").toString(), menuObject.get("name").toString(), menuObject.get("description").toString(),
                            menuObject.get("price").toString(), menuObject.get("restaurant_id").toString());
                    restaurant.addMenu(menu);
                }
            }
            if (restaurant != null)
                restaurants.add(restaurant);
        }
        this.restauAdapter = new RestauAdapter(getApplicationContext(), restaurants);
        restauAdapter.notifyDataSetChanged();
        listView.setAdapter(restauAdapter);
    }


}
