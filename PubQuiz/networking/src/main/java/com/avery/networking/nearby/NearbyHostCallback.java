package com.avery.networking.nearby;

import com.avery.networking.nearby.messages.BaseMessage;

/**
 * Created by mark on 6/10/16.
 */
public interface NearbyHostCallback {

    void onConnectedSuccess();
    void onConnectedFailed();
    void onAdvertisingSuccess();
    void onAdvertisingFailed(int statusCode);
    void onConnectionAccepted(Client client, BaseMessage message);
    void onConnectionFailed(int statusCode);
    void handleMessage(String endpointId, BaseMessage message);

}
