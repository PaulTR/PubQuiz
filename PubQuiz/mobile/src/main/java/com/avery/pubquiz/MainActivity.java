package com.avery.pubquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.NearbyDiscoveryCallback;
import com.avery.networking.nearby.NearbyHostCallback;
import com.avery.networking.nearby.NearbyManager;

public class MainActivity extends AppCompatActivity implements NearbyHostCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private NearbyManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = NearbyManager.getInstance();
        mManager.initialize(this);

    }
        /*
        Call<BeerList> call = AveryNetworkAdapter.getInstance().getService().getBeers();
        call.enqueue(new Callback<BeerList>() {
        */


    @Override
    protected void onStart() {
        super.onStart();
        mManager.startDiscovery(30, new NearbyDiscoveryCallback() {
            @Override
            public void onDiscoveringSuccess() {
                Log.e(TAG, "onDiscoverying success");
            }

            @Override
            public void onDiscoveringFailed(int statusCode) {
                Log.e(TAG, "onDiscovering failed : " + statusCode);
            }

            @Override
            public void onEndpointLost(String endpointId) {
                Log.e(TAG, "onEndpointLog : " + endpointId);
            }

            @Override
            public void onEndpointFound(String endpointId, String endpointName) {
                Log.e(TAG, "endpointid : " + endpointId + " : " + endpointName);
            }
        });

        Log.e(TAG, "hello");

        /*mManager.startAdvertising("averytv", new NearbyHostCallback() {
            @Override
            public void onAdvertisingSuccess() {
                Log.e(TAG, "onadvertising success");
            }

            @Override
            public void onAdvertisingFailed(int statusCode) {
                Log.e(TAG, "onadvertising failed : " + statusCode);
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
        Log.e(TAG, "onconnectionaccpeted : " + client.getName());
    }

    @Override
    public void onConnectionFailed(int statusCode) {
        Log.e(TAG, "on connection failed : " + statusCode);
    }
}
