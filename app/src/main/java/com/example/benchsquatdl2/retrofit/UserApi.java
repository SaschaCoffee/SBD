package com.example.benchsquatdl2.retrofit;

import com.example.benchsquatdl2.model.modelApi.Country.germancountry;
import com.example.benchsquatdl2.model.modelApi.Country.countryname;
import com.example.benchsquatdl2.model.modelApi.Trainingdate;
import com.example.benchsquatdl2.model.modelApi.State.germanstate;
import com.example.benchsquatdl2.model.modelApi.State.statename;
import com.example.benchsquatdl2.model.modelApi.trainingdto;
import com.example.benchsquatdl2.model.modelApi.trainingsdaten;
import com.example.benchsquatdl2.model.modelApi.lilWorldWide;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @POST("/api/trainingsdaten")
    Call<trainingsdaten> savedata(@Body trainingsdaten trainingsdaten);

    @POST("/api/testemich")
    Call<trainingsdaten> testemich(@Body trainingsdaten trainingsdaten);

    @POST("/api/deleterecord")
    Call<trainingsdaten> deleteitem(@Body trainingsdaten trainingsdaten);

    @POST("/api/checkifexist")
    Call<trainingsdaten> checkData(@Body trainingsdaten trainingsdaten);

    @POST("/api/sortdata")
    Call<statename> sortData(@Body statename state);

    @POST("/api/getDatabyIdandDate")
    Call<Trainingdate> geTrainingDatabyId(@Body Trainingdate trainingdate);

    @POST("/api/germanstate")
    Call<List<germanstate>> getgermanstate(@Body statename state);

    @POST("/api/sortgermanstate")
    Call<List<germanstate>> getsortgermanstate(@Body statename state);




    @GET("/api/getAlltrainingData")
    Call<List<trainingdto>> geTrainingData();

    @GET("/api/getgermancountry")
    Call<List<germancountry>> getgermancountry();


    @GET("/api/world")
    Call<List<lilWorldWide>> getWorldData();

    @GET("/api/getDatabyId")
    Call<List<trainingdto>> getDatabyID();



}
