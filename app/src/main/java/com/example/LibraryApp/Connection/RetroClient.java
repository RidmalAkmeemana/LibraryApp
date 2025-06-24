package com.example.LibraryApp.Connection;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



//Singleton Connection

public class RetroClient {

    //private static final String BASE_URL="https://qrattendancemarking.000webhostapp.com/QR_AttendanceWeb/";
    private static final String BASE_URL="http://192.168.8.105/LibraryApp/";


    private static RetroClient myClient;
    private Retrofit retrofit;


    Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    public RetroClient(){
        retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    public static synchronized RetroClient getInstance(){
        if (myClient==null){
            myClient=new RetroClient();
        }
        return myClient;
    }

    public API getApi(){
        return retrofit.create(API.class);

    }


}
