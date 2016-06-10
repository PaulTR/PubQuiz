package com.avery.networking.model.beer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mark on 6/10/16.
 */
public class BeerList {

    @SerializedName(value="beers", alternate={"beer_list", "barrel_aged_beers"})
    public List<Beer> beers;
}
