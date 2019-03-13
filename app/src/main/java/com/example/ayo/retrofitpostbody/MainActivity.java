package com.example.ayo.retrofitpostbody;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import serviceClient.PostClient;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://safe-plateau-54104.herokuapp.com/api/v1/";
    public static final String MY_PREFRENCE = "myPrefs";
    public static final String TOKEN = "myToken";
    Retrofit retrofit;
    Context context;

    public EditText nameText, passText, aboutText, userLogText, userPassText;
    public String name, password, about, fullResponse, username, userPassword, loginResponse;
    RelativeLayout relativeLayout, signUPLayout, loginLayout;
    public TextView responseText;
    public Button buttonSign, buttonLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameText = findViewById(R.id.name);
        passText = findViewById(R.id.password);
        aboutText = findViewById(R.id.about);
        relativeLayout = (RelativeLayout) findViewById(R.id.RelativeLayout);
        responseText = findViewById(R.id.response);
        signUPLayout = findViewById(R.id.signUp);
        loginLayout = findViewById(R.id.login);
        buttonSign = findViewById(R.id.button);
        buttonLog = findViewById(R.id.log);
        userLogText = findViewById(R.id.name_login);
        userPassText = findViewById(R.id.password_login);


    }

    public void sendData(View view) {
        makeString();
        postData();
    }

    private void makeString() {
        name = nameText.getText().toString();
        password = passText.getText().toString();
        about = aboutText.getText().toString();
    }

    private void postData() {
        build();
        PostClient client = retrofit.create(PostClient.class);
        Post post = new Post(name, password, about);


        Call<Post> call = client.postDetails(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (response.isSuccessful()) {

                    Post data = response.body();
                    String Dname = data.getName();
                    String Dpassowrd = data.getPassword();
                    String Dabout = data.getAbout();
                    fullResponse = Dname + " " + Dpassowrd + " " + Dabout;
                    responseText.setVisibility(View.VISIBLE);
                    responseText.setText(fullResponse);

                    Snackbar snackBar = Snackbar.make(
                            relativeLayout, fullResponse,
                            Snackbar.LENGTH_LONG);
                    snackBar.show();

                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("TAG", "error poor internet connection");

            }
        });
    }

    public void openLogin(View view) {
        if (signUPLayout.getVisibility() == View.VISIBLE) {
            signUPLayout.setVisibility(View.GONE);
            buttonSign.setVisibility(View.GONE);
            buttonLog.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.VISIBLE);

        }
    }

    public void openSignUp(View view) {

        if (signUPLayout.getVisibility() == View.GONE) {
            loginLayout.setVisibility(View.GONE);
            buttonLog.setVisibility(View.GONE);
            signUPLayout.setVisibility(View.VISIBLE);
            buttonSign.setVisibility(View.VISIBLE);


        }

    }

    public void loginUser(View view) {
        makeUserString();
        PutUser();

    }

    private void openGetActivity() {

        Intent openIntent = new Intent(this, GetActivity.class);
        startActivity(openIntent);
    }

    private void PutUser() {
        build();
        PostClient loginClient = retrofit.create(PostClient.class);
        Post loginPut = new Post(username, userPassword);
        Log.d("TAG", "check1: " + username + userPassword);
        Call<Post> loginCall = loginClient.putDetails(loginPut);
        loginCall.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.d("TAG", "error: " + response.body());
                if (response.isSuccessful()) {
                    Post token = response.body();
                    String userToken = token.getToken();
                    if (responseText.getVisibility() == View.INVISIBLE) {
                        responseText.setVisibility(View.VISIBLE);
                        responseText.setText(userToken);
                    } else {
                        responseText.setText(userToken);
                    }

                    SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFRENCE, context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(TOKEN, userToken);
                    editor.apply();

                    openGetActivity();
                } else if(response.code() == 400) {
                    Toast.makeText(MainActivity.this, "Invalid username or password",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Error happened maybe network", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void makeUserString() {
        username = userLogText.getText().toString();
        userPassword = userPassText.getText().toString();

    }

    private void build() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

}
