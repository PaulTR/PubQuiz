package com.avery.pubquiz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.NearbyHostCallback;
import com.avery.networking.nearby.NearbyManager;
import com.avery.networking.nearby.messages.BaseMessage;
import com.avery.pubquiz.R;
import com.avery.pubquiz.fragments.FormTeamsFragment;

public class MainActivity extends Activity implements NearbyHostCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();

        NearbyManager manager = NearbyManager.getInstance();

        manager.setNearbyHostCallback(this);

        manager.initialize(this);

    }

    private void initFragment() {
        getFragmentManager().beginTransaction().replace(R.id.container, FormTeamsFragment.getInstance()).commit();
    }

    @Override
    public void onConnectedSuccess() {
        Log.e("Nearby", "onConnectedSuccess");
        NearbyManager.getInstance().advertise();
    }

    @Override
    public void onConnectedFailed() {
        Log.e("Nearby", "onConnectedFailed");
    }

    @Override
    public void onAdvertisingSuccess() {
        Log.e("Nearby", "onAdvertisingSuccess");
    }

    @Override
    public void onAdvertisingFailed(int statusCode) {
        Log.e("Nearby", "onAdvertisingFailed: " + statusCode);
    }

    @Override
    public void onConnectionAccepted(Client client, BaseMessage message) {
        Log.e("Nearby", "onConnectionAccepted: " + message.messageType );
    }

    @Override
    public void onConnectionFailed(int statusCode) {
        Log.e("Nearby", "onConnectionFailed");
    }
}
