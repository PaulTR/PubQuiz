package com.avery.networking.nearby;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.avery.networking.R;
import com.avery.networking.deserialize.BaseMessageDeserializer;
import com.avery.networking.nearby.messages.BaseMessage;
import com.avery.networking.nearby.messages.RegisterMessage;
import com.avery.networking.nearby.messages.RegisterResponseMessage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AppIdentifier;
import com.google.android.gms.nearby.connection.AppMetadata;
import com.google.android.gms.nearby.connection.Connections;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public static NearbyManager getInstance() {
        if (sInstance == null)
            sInstance = new NearbyManager();

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


    private NearbyManager() {

    }


    public void initialize(Context context) {
        mContext = context.getApplicationContext();

        mApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Nearby.CONNECTIONS_API)
                .build();

        mServiceId = context.getString(R.string.service_id);
        connect();
    }


    public void connect() {
        mApiClient.connect();
    }


    public void disconnect() {
        if( mApiClient != null && mApiClient.isConnected() ) {
            if(mIsHost) {
                Nearby.Connections.stopAdvertising(mApiClient);
            }
            mApiClient.disconnect();
        }
    }


    public void setNearbyClientCallback(NearbyClientCallback nearbyClientCallback) {
        mNearbyClientCallback = nearbyClientCallback;
    }

    public void setNearbyHostCallback(NearbyHostCallback nearbyHostCallback) {
        mNearbyHostCallback = nearbyHostCallback;
        mIsHost = true;
    }

    public void setNearbyDiscoveryCallback(NearbyDiscoveryCallback nearbyDiscoveryCallback) {
        mNearbyDiscoveryCallback = nearbyDiscoveryCallback;
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
        Log.e(TAG, "onConnected");
        if (mNearbyHostCallback != null) {
            mNearbyHostCallback.onConnectedSuccess();
        } else if (mNearbyDiscoveryCallback != null) {
            mNearbyDiscoveryCallback.onConnectedSuccess();
        } else if (mNearbyClientCallback != null) {
            mNearbyClientCallback.onConnectedSuccess();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called with: " + "i = [" + i + "]");
        if (mNearbyHostCallback != null) {
            mNearbyHostCallback.onConnectedFailed();
        } else if (mNearbyDiscoveryCallback != null) {
            mNearbyDiscoveryCallback.onConnectedFailed();
        } else if (mNearbyClientCallback != null) {
            mNearbyClientCallback.onConnectedFailed();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called with: " + "connectionResult = [" + connectionResult + "]");
        if (mNearbyHostCallback != null) {
            mNearbyHostCallback.onConnectedFailed();
        } else if (mNearbyDiscoveryCallback != null) {
            mNearbyDiscoveryCallback.onConnectedFailed();
        } else if (mNearbyClientCallback != null) {
            mNearbyClientCallback.onConnectedFailed();
        }
    }


    @Override
    public void onConnectionRequest(final String remoteEndpointId, final String remtoeClientId, final String remoteEndpointName, final byte[] payload) {
        if( mIsHost ) {
            Nearby.Connections.acceptConnectionRequest( mApiClient, remoteEndpointId, payload, this ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    if( status.isSuccess() ) {

                        if(mNearbyHostCallback != null) {
                            String payloadStr = new String(payload);
                            BaseMessage message = getBaseMessage(payloadStr);

                            mNearbyHostCallback.onConnectionAccepted(new Client(remoteEndpointId, remtoeClientId), message);
                        }
                    }
                }
            });
        } else {
            Nearby.Connections.rejectConnectionRequest(mApiClient, remoteEndpointId );
        }
    }

    @Override
    public void onEndpointFound(final String endpointId, String deviceId, String serviceId, final String endpointName) {
        Log.e(TAG, "onEndPointFound : " + endpointId + " : deviceId : " + deviceId + " : serviceId : " + serviceId + " : endpointName : " + endpointName);
        if (mNearbyDiscoveryCallback != null) {
            Host host = new Host(endpointId, endpointName);
            Client client = new Client(endpointId, deviceId);
            mNearbyDiscoveryCallback.onEndpointFound(host, client);
        }
    }

    @Override
    public void onEndpointLost(final String endpointId) {
        Log.e(TAG, "onEndpointLost : " + endpointId);
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

    public void advertise() {
        if( !isConnectedToNetwork() )
            return;

        Nearby.Connections.startAdvertising( mApiClient, mServiceId, null, NO_TIMEOUT, this ).setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {
            @Override
            public void onResult(Connections.StartAdvertisingResult result) {
                if (result.getStatus().isSuccess()) {
                    if(mNearbyHostCallback != null) {
                        mNearbyHostCallback.onAdvertisingSuccess();
                    }
                }else {
                    if(mNearbyHostCallback != null) {
                        mNearbyHostCallback.onAdvertisingFailed(result.getStatus().getStatusCode());
                    }
                }
            }
        });
    }

    public void discover() {
        if( !isConnectedToNetwork() )
            return;

        Nearby.Connections.startDiscovery(mApiClient, mServiceId, 30000L, this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.isSuccess()) {
                    if(mNearbyDiscoveryCallback != null) {
                        mNearbyDiscoveryCallback.onDiscoveringSuccess();
                    }
                }else {
                    if(mNearbyDiscoveryCallback != null) {
                        mNearbyDiscoveryCallback.onDiscoveringFailed(status.getStatusCode());
                    }
                }
            }
        });
    }


    public void connectToHost(Host host, Client client) {
        Log.e(TAG, "connect to host!");
        Gson gson = new Gson();
        RegisterMessage message = new RegisterMessage();
        message.teamName = client.getName();
        String payloadString = gson.toJson(message);

        byte[] payload = payloadString.getBytes();

        Nearby.Connections.sendConnectionRequest( mApiClient, client.getClientId(), host.getEndpointId(), payload, new Connections.ConnectionResponseCallback() {

            @Override
            public void onConnectionResponse(String s, Status status, byte[] bytes) {
                if( status.isSuccess() ) {
                    Nearby.Connections.stopDiscovery(mApiClient, mServiceId);
                    mNearbyDiscoveryCallback.onConnectionResponse();

                    if( !mIsHost ) {
                        mIsConnected = true;
                    }
                } else {
                    if( !mIsHost ) {
                        mIsConnected = false;
                    }
                }
            }
        }, this );
    }


    public void sendTeamRegisteredResponse(Client client, RegisterResponseMessage message) {

    }


    private void sendMessage( String message, Client client ) {
        /*if( mIsHost ) {
            Nearby.Connections.sendReliableMessage(mApiClient, mRemotePeerEndpoints, message.getBytes());
            mMessageAdapter.add(message);
            mMessageAdapter.notifyDataSetChanged();
        } else {
            Nearby.Connections.sendReliableMessage( mGoogleApiClient, mRemoteHostEndpoint, ( Nearby.Connections.getLocalDeviceId( mGoogleApiClient ) + " says: " + message ).getBytes() );
        }*/
    }


    public BaseMessage getBaseMessage(String payload) {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(BaseMessage.class, new BaseMessageDeserializer());

        Gson gson = gb.create();

        return gson.fromJson(payload, BaseMessage.class);
    }
}
