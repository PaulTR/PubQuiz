package com.avery.networking.nearby;

/**
 * Created by mark on 6/10/16.
 */
public interface NearbyHostCallback {

    void onAdvertisingSuccess();
    void onAdvertisingFailed(int statusCode);
    void onConnectionAccepted(Client client);
    void onConnectionFailed(int statusCode);

}
