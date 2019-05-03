package com.example.restauadvisor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static UserApi userApi;
    private Retrofit retrofit;
    private User user;
    private EditText nameValue;
    private EditText passwordValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApiController controller = new ApiController();
        retrofit = controller.configureRetrofit();

        Button buttonRegister = (Button) findViewById(R.id.button);
        TextView buttonLogin = (TextView) findViewById(R.id.login);
        Button buttonDisplay = (Button) findViewById(R.id.display);
        nameValue = (EditText)findViewById(R.id.name);
        passwordValue = (EditText)findViewById(R.id.password);
        buttonRegister.setOnClickListener(new View.OnClickListener(){
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
                register(user);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startLoginActivity();
            }
        });
        buttonDisplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startDisplayActivity();
            }
        });
    }

    public void startLoginActivity() {
        Intent intent = new Intent(this, Login.class);
        //intent.putExtra("extra_name", nameValue.getText().toString());
        //intent.putExtra("extra_password", passwordValue.getText().toString());
        startActivity(intent);
    }

    public void startDisplayActivity() {
        Intent intent = new Intent(this, DisplayRestaurant.class);
        startActivity(intent);
    }

    public void register(User user) {
        userApi = retrofit.create(UserApi.class);
        Call<JsonObject> call = userApi.registerUser(user.getUsername(), user.getPassword());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    String name = response.body().getAsJsonObject("User").get("username").toString();
                    String password = response.body().getAsJsonObject("User").get("password").toString();
                    User user = new User(name, password);
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
}
