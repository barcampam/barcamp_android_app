package com.barcampevn.data.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andranikas on 5/17/2017.
 */

public final class APIHelper {

    private static final String BASE_URL = "http://api.barcamp.am/";

    private APIHelper() {
    }

    private static Retrofit client() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static APIService getService() {
        return client().create(APIService.class);
    }
}