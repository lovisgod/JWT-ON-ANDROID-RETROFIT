package com.example.ayo.retrofitpostbody;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import Adapter.userAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Post;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import serviceClient.PostClient;

public class GetActivity extends AppCompatActivity {

    public static final String MY_PREFRENCE = "myPrefs";
    public static final String TOKEN = "myToken";
    public static final String BASE_URL = "https://safe-plateau-54104.herokuapp.com/api/v1/";

    public TextView textView;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    Retrofit retrofit;
    userAdapter adapter;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        recyclerView = findViewById(R.id.userlist);
        adapter = new userAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);


        sharedPreferences = getSharedPreferences(MY_PREFRENCE, Context.MODE_PRIVATE);
        str = sharedPreferences.getString(TOKEN, "");

        Log.d("TAG", "love u : " + str);


        makeRequest();
    }

    private void makeRequest() {
        build();
        PostClient client = retrofit.create(PostClient.class);
        Call<ResponseBody> call = client.getUsers( "Bearer" + " " + str);
        Log.d("TAG", "myLove: " + str);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //the responseBody here gets all the response from our request
                if(response.isSuccessful()){
                    Log.d("TAG", "love33 : " + response.body());
                    String result = null;
                    try {
                        result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adapter.setResult(extractJsonResult(result));
                }else{
                    Toast.makeText(GetActivity.this, "Error happened please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("TAG", "errord : " + t);
                Toast.makeText(GetActivity.this, "Error happened: " + t.getCause().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private List<Post> extractJsonResult(String result) {

        List<Post> postList = new ArrayList<>();

        try {
            //here what we did is to get JSONObject from the String we has from the responseBody
            JSONObject jsonObject = new JSONObject(result);
            //since our object is an Array we want to get a JSONArray from the JSONOBJECT
            //the "my "
            JSONArray jsonArray = jsonObject.getJSONArray("my");
            //here we loop through the Jsonarray gotten fom the json object
            for (int i = 0; i < jsonArray.length(); i++) {
                //a json object is gotten from from the json array at position i
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                // the Post model class is used to map the values inside the objects
                Post post = new Post(jsonObject1.getString("name"),
                                     jsonObject1.getString("password"),
                                     jsonObject1.getString("about"));

                //the object is now added to the ArrayList
                postList.add(post);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    //we return the ArrayList
        return postList;
    }


    private void build() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
