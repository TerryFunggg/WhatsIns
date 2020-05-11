package com.terryyessfung.whatsins;


import com.terryyessfung.whatsins.API.APIService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataManager {
    private static DataManager instance = null;
    private static Retrofit mRetrofit;
    private static APIService mAPIService;
    private static final String BASR_URL = "http://192.168.31.237:3000/";

    public DataManager() { }

    public static  DataManager getInstance(){
        if(instance == null){
            instance = new DataManager();
            mRetrofit = new Retrofit.Builder().baseUrl(BASR_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            mAPIService = mRetrofit.create(APIService.class);
        }
        return instance;
    }

    public APIService getAPIService(){
        return mAPIService;
    }





}
