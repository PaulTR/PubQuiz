package com.avery.networking.nearby;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.avery.networking.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AppIdentifier;
import com.google.android.gms.nearby.connection.AppMetadata;
import com.google.android.gms.nearby.connection.Connections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mark on 6/10/16.
 */
public class NearbyManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Connections.ConnectionRequestListener,
        Connections.MessageListener,
        Connections.EndpointDiscoveryListener {

    private static final String TAG = NearbyManager.class.getSimpleName();
    public static final long NO_TIMEOUT = 0L;
    private static int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_ETHERNET};

    private static NearbyManager sInstance;

    public static NearbyManager getInstance(Context context) {
        if(sInstance == null)
            sInstance = new NearbyManager(context);

        return sInstance;
    }

    private Context mContext;
    private GoogleApiClient mApiClient;
    private String mServiceId;
    private boolean mIsHost = false;
    private boolean mIsConnected = false;

    private Host mHost;
    private String mClientName;

    private NearbyHostCallback mNearbyHostCallback;
    private NearbyClientCallback mNearbyClientCallback;
    private NearbyDiscoveryCallback mNearbyDiscoveryCallback;
    private String mHostName;

    private long mTimeout;


    private NearbyManager(Context context) {

        mContext = context.getApplicationContext();

    }


    public void initialize(Context context) {
        mContext = context.getApplicationContext();

        mApiClient = new GoogleApiClient.Builder(context)
                .addApi(Nearby.CONNECTIONS_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mServiceId = context.getString(R.string.service_id);
    }



    public void onStart() {
        mApiClient.connect();
    }


    public void onStop() {
        if (mApiClient != null && mApiClient.isConnected()) {
            mApiClient.disconnect();
        }
    }

    private boolean isConnectedToNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        for (int networkType : NETWORK_TYPES) {
            NetworkInfo info = connManager.getNetworkInfo(networkType);
            if (info != null && info.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mNearbyHostCallback != null) {
            startAdvertisingAfterConnectionEstablished();
        } else if (mNearbyDiscoveryCallback != null) {
            startDiscoveryAfterConnectionEstablished();
        } else if (mNearbyClientCallback != null) {
            connectToAfterEstablished();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called with: " + "i = [" + i + "]");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called with: " + "connectionResult = [" + connectionResult + "]");
    }


    public void disconnect() {
        if (mIsHost) {
            Nearby.Connections.stopAdvertising(mApiClient);
        }

        if (mApiClient != null && mApiClient.isConnected()) {
            mApiClient.disconnect();
        }
    }


    @Override
    public void onConnectionRequest(final String remoteEndpointId, final String cliendId, final String clientName, final byte[] payload) {
        if (mIsHost) {
            Nearby.Connections
                    .acceptConnectionRequest(mApiClient, remoteEndpointId, payload, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (mNearbyHostCallback == null) {
                                return;
                            }

                            if (status.isSuccess()) {
                                mNearbyHostCallback.onConnectionAccepted(new Client(cliendId, clientName));
                            } else {
                                int statusCode = status.getStatusCode();
                                mNearbyHostCallback.onConnectionFailed(statusCode);
                            }
                        }
                    });
        } else {
            Nearby.Connections.rejectConnectionRequest(mApiClient, remoteEndpointId);
        }
    }

    @Override
    public void onEndpointFound(final String endpointId, String deviceId, String serviceId, final String endpointName) {
        if (mNearbyDiscoveryCallback != null) {
            mNearbyDiscoveryCallback.onEndpointFound(endpointId, endpointName);
        }
    }

    @Override
    public void onEndpointLost(final String endpointId) {
        if (mNearbyDiscoveryCallback != null) {
            mNearbyDiscoveryCallback.onEndpointLost(endpointId);
        }
    }

    @Override
    public void onMessageReceived(String endpointId, byte[] payload, boolean isReliable) {
        String data = Arrays.toString(payload);
    }

    @Override
    public void onDisconnected(final String endpointId) {
        mNearbyHostCallback = null;
        mNearbyDiscoveryCallback = null;
        mNearbyClientCallback = null;
    }


    private void stopAdvertising() {
        Nearby.Connections.stopAdvertising(mApiClient);
    }

    public void startAdvertising(final String hostName, final NearbyHostCallback callback) {
        if (!isConnectedToNetwork()) {
            return;
        }

        mIsHost = true;
        mNearbyHostCallback = callback;
        mHostName = hostName;
        mApiClient.connect();
    }


    public void startAdvertisingAfterConnectionEstablished() {
        List<AppIdentifier> appIdentifierList = new ArrayList<>();
        appIdentifierList.add(new AppIdentifier(mServiceId));
        AppMetadata appMetadata = new AppMetadata(appIdentifierList);

        Nearby.Connections
                .startAdvertising(mApiClient, mHostName, appMetadata, NO_TIMEOUT, this)
                .setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {
                    @Override
                    public void onResult(Connections.StartAdvertisingResult result) {
                        if (mNearbyHostCallback == null) {
                            return;
                        }

                        if (result.getStatus().isSuccess()) {
                            mNearbyHostCallback.onAdvertisingSuccess();
                        } else {
                            int statusCode = result.getStatus().getStatusCode();
                            mNearbyHostCallback.onAdvertisingFailed(statusCode);
                        }
                    }
                });
    }


    public void startDiscovery(final NearbyDiscoveryCallback clientCallback) {
        startDiscovery(NO_TIMEOUT, clientCallback);
    }

    public void startDiscovery(final long timeout, final NearbyDiscoveryCallback clientCallback) {
        if (!isConnectedToNetwork()) {
            return;
        }

        mTimeout = timeout;
        mNearbyDiscoveryCallback = clientCallback;
        mApiClient.connect();
    }

    private void startDiscoveryAfterConnectionEstablished() {
        mIsHost = false;

        Nearby.Connections
                .startDiscovery(mApiClient, mServiceId, mTimeout, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (mNearbyDiscoveryCallback == null) {
                            return;
                        }

                        if (status.isSuccess()) {
                            mNearbyDiscoveryCallback.onDiscoveringSuccess();
                        } else {
                            int statusCode = status.getStatus().getStatusCode();
                            mNearbyDiscoveryCallback.onDiscoveringFailed(statusCode);
                        }
                    }
                });
    }

    public void connectTo(final Host host, final String clientName, final NearbyClientCallback clientCallback) {
        if (!isConnectedToNetwork()) {
            return;
        }

        mHost = host;
        mClientName = clientName;
        mNearbyClientCallback = clientCallback;
        mApiClient.connect();
    }

    private void connectToAfterEstablished() {
        Nearby.Connections.sendConnectionRequest(mApiClient, mClientName, mHost.getEndpointId(), null,
                new Connections.ConnectionResponseCallback() {
                    @Override
                    public void onConnectionResponse(String remoteEndpointId, Status status,
                                                     byte[] bytes) {
                        if (status.isSuccess()) {
                            // Successful connection
                        } else {
                            // Failed connection
                        }
                    }
                }, this);
    }
}
