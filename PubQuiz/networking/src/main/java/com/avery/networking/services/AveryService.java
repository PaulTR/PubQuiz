package com.avery.networking.services;

import com.avery.networking.model.beer.BeerList;
import com.avery.networking.model.beer.BeerResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by mark on 6/10/16.
 */
public interface AveryService {

    @GET("beers")
    Call<BeerList> getBeers();


    @GET("barrel-aged-beers")
    Call<BeerList> getBarrelAgedBeers();


    @GET("beers/{beerId}")
    Call<BeerResult> getBeer(@Path("beerId") String beerId);
}
