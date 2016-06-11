package com.avery.networking.model.tap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark on 6/11/16.
 */
public class TapBeer {

    @SerializedName("beer_name")
    private String beerName;

    @SerializedName("beer_id")
    private String beerId;

    @SerializedName("amount_remaining")
    private float amountRemaining;

    @SerializedName("amount_used")
    private float amountUsed;

    @SerializedName("current_keg")
    private int currentKeg;

    @SerializedName("total_kegs")
    private int totalKegs;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public String getBeerName() {
        return beerName;
    }

    public void setBeerName(String beerName) {
        this.beerName = beerName;
    }

    public String getBeerId() {
        return beerId;
    }

    public void setBeerId(String beerId) {
        this.beerId = beerId;
    }

    public float getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(float amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public float getAmountUsed() {
        return amountUsed;
    }

    public void setAmountUsed(float amountUsed) {
        this.amountUsed = amountUsed;
    }

    public int getCurrentKeg() {
        return currentKeg;
    }

    public void setCurrentKeg(int currentKeg) {
        this.currentKeg = currentKeg;
    }

    public int getTotalKegs() {
        return totalKegs;
    }

    public void setTotalKegs(int totalKegs) {
        this.totalKegs = totalKegs;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
