package com.avery.networking.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mark on 6/10/16.
 */
public class AveryNetworkAdapter {

    public static final String BASE_URL = "http://apis.mondorobot.com";

    private static AveryNetworkAdapter sNetworkInstance;

    private Retrofit mRetrofit;
    private AveryService mService;

    public static AveryNetworkAdapter getInstance() {
        if (sNetworkInstance == null)
            sNetworkInstance = new AveryNetworkAdapter();

        return sNetworkInstance;
    }

    private AveryNetworkAdapter() {
        mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(AveryService.class);
    }


    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public AveryService getService() {
        return mService;
    }

}
