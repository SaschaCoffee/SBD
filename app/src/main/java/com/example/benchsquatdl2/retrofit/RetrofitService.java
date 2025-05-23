package com.example.benchsquatdl2.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private Retrofit retrofit;

    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.74:9998")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        UserApi userApi = retrofit.create(UserApi.class);





    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}