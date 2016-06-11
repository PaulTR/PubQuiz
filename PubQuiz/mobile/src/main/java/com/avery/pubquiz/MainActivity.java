package com.avery.pubquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.avery.networking.model.beer.Beer;
import com.avery.networking.model.beer.BeerList;
import com.avery.networking.model.beer.BeerResult;
import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.NearbyHostCallback;
import com.avery.networking.nearby.NearbyManager;
import com.avery.networking.services.AveryNetworkAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NearbyHostCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Call<BeerList> call = AveryNetworkAdapter.getInstance().getService().getBeers();
        call.enqueue(new Callback<BeerList>() {
            @Override
            public void onResponse(Call<BeerList> call, Response<BeerList> response) {
                Log.e(TAG, "beer list returned : " + response.body().beers.size());
            }

            @Override
            public void onFailure(Call<BeerList> call, Throwable t) {
                Log.e(TAG, "error getting beer list");
                t.printStackTrace();
            }
        });

        Call<BeerList> barrelCall = AveryNetworkAdapter.getInstance().getService().getBarrelAgedBeers();
        barrelCall.enqueue(new Callback<BeerList>() {
            @Override
            public void onResponse(Call<BeerList> call, Response<BeerList> response) {
                Log.e(TAG, "barrel aged beer list returned : " + response.body().beers.size());
            }

            @Override
            public void onFailure(Call<BeerList> call, Throwable t) {
                Log.e(TAG, "error getting barrel aged beer list");
                t.printStackTrace();
            }
        });

        Call<BeerResult> singleBeer = AveryNetworkAdapter.getInstance().getService().getBeer("ipa");
        singleBeer.enqueue(new Callback<BeerResult>() {
            @Override
            public void onResponse(Call<BeerResult> call, Response<BeerResult> response) {
                Log.e(TAG, "beer returned : " + response.body().getBeer().getName());
            }

            @Override
            public void onFailure(Call<BeerResult> call, Throwable t) {
                Log.e(TAG, "error getting single beer");
                t.printStackTrace();
            }
        });

        */

        NearbyManager.getInstance(this).startAdvertising("avery", this);
    }

    @Override
    public void onAdvertisingSuccess() {

    }

    @Override
    public void onAdvertisingFailed(int statusCode) {

    }

    @Override
    public void onConnectionAccepted(Client client) {

    }

    @Override
    public void onConnectionFailed(int statusCode) {

    }
}
