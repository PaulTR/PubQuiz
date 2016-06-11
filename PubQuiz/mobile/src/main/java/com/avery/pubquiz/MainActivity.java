package com.avery.pubquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.Host;
import com.avery.networking.nearby.NearbyDiscoveryCallback;
import com.avery.networking.nearby.NearbyHostCallback;
import com.avery.networking.nearby.NearbyManager;

public class MainActivity extends AppCompatActivity implements NearbyDiscoveryCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private NearbyManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = NearbyManager.getInstance();
        mManager.setNearbyDiscoveryCallback(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mManager.initialize(this);
    }

    @Override
    public void onConnectedSuccess() {
        Log.e(TAG, "onConnected Success");
        mManager.discover();
    }

    @Override
    public void onConnectedFailed() {
        Log.e(TAG, "onConnected Failed");
    }

    @Override
    public void onDiscoveringSuccess() {
        Log.e(TAG, "onDiscovering success");
    }

    @Override
    public void onDiscoveringFailed(int statusCode) {
        Log.e(TAG, "onDiscovering failed : " + statusCode);
    }

    @Override
    public void onEndpointLost(String endpointId) {
        Log.e(TAG, "onEndpointLost : " + endpointId);
    }

    @Override
    public void onEndpointFound(Host host, Client client) {
        Log.e(TAG, "onEndpoint Found");
        if(host != null) {
            Log.e(TAG, "host : " + host.getEndpointId() + " : " + host.getEndpointName());
            client.setName("my stupid name");
            mManager.connectToHost(host, client);
        }
    }

    @Override
    public void onConnectionResponse() {
        Log.e(TAG, "onConnectionResponse!");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
