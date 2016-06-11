package com.avery.pubquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.avery.networking.model.beer.Beer;
import com.avery.networking.model.beer.BeerList;
import com.avery.networking.model.beer.BeerResult;
import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.NearbyDiscoveryCallback;
import com.avery.networking.nearby.NearbyHostCallback;
import com.avery.networking.nearby.NearbyManager;
import com.avery.networking.services.AveryNetworkAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    protected void onStart() {
        super.onStart();

        NearbyManager manager = NearbyManager.getInstance();
        manager.initialize(this);
        manager.startDiscovery(30, new NearbyDiscoveryCallback() {
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

        /*manager.startAdvertising("averytv", new NearbyHostCallback() {
            @Override
            public void onAdvertisingSuccess() {
                Log.e(TAG, "onadvertising success");
            }

            @Override
            public void onAdvertisingFailed(int statusCode) {
                Log.e(TAG, "onadvertising failed : " + statusCode);
            }

            @Override
            public void onConnectionAccepted(Client client) {
                Log.e(TAG, "onconnectionaccpeted : " + client.getName());
            }

            @Override
            public void onConnectionFailed(int statusCode) {
                Log.e(TAG, "on connection failed : " + statusCode);
            }
        });*/
    }
}
