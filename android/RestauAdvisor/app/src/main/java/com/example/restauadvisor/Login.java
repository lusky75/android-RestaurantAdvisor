package com.example.restauadvisor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    SharedPreferences preferences;

    private EditText nameValue;
    private EditText passwordValue;
    private User user;
    private Retrofit retrofit;
    private UserApi userApi;
    private TextView buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ApiController controller = new ApiController();
        retrofit = controller.configureRetrofit();
        Button buttonLogin = (Button) findViewById(R.id.login);
        Button home = (Button) findViewById(R.id.home);
        nameValue = (EditText)findViewById(R.id.name);
        passwordValue = (EditText)findViewById(R.id.password);
        TextView buttonRegister = (TextView) findViewById(R.id.textRegister );

        buttonRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startRegisterActivity();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (nameValue.getText().toString().isEmpty()) {
                    nameValue.setError("Username field required");
                    return;
                }
                if (passwordValue.getText().toString().isEmpty()) {
                    passwordValue.setError("Password field required");
                    return;
                }
                user = new User(nameValue.getText().toString(), passwordValue.getText().toString());
                login(user);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home();
            }
        });
    }

    public void startRegisterActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void home() {
        Intent intent = new Intent(this, DisplayRestaurant.class);
        startActivity(intent);
    }

    public void login(User user) {
        userApi = retrofit.create(UserApi.class);
        Call<JsonObject> call = userApi.loginUser(user.getUsername(), user.getPassword());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    //System.out.println(response.body().get("token"));
                    preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                    String token = response.body().get("token").toString();
                    String admin = response.body().get("User").getAsJsonObject().get("admin").toString();
                    String username = response.body().get("User").getAsJsonObject().get("username").toString();
                    preferences.edit().putString("token", token).commit();
                    preferences.edit().putString("admin", admin).commit();
                    preferences.edit().putString("username", username).commit();
                    startDisplayActivity();
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

    public void startDisplayActivity() {
        Intent intent = new Intent(this, DisplayRestaurant.class);
        startActivity(intent);
    }

    public void configureRetrofit() {
        Gson gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8085/jersey/rest/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
