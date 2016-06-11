package com.avery.pubquiz.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.avery.networking.nearby.NearbyDiscoveryCallback;
import com.avery.networking.nearby.NearbyManager;
import com.avery.pubquiz.R;
import com.avery.pubquiz.fragment.LoadingFragment;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    static final String CONTENT_FRAME_TAG = "contentFrameTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertLoadingFragment();

    }


    private void insertLoadingFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new LoadingFragment(), CONTENT_FRAME_TAG).commit();
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
