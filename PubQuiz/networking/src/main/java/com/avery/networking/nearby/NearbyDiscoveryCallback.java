package com.avery.networking.nearby;

/**
 * Created by mark on 6/10/16.
 */
public interface NearbyDiscoveryCallback {

    void onConnectedSuccess();
    void onConnectedFailed();
    void onDiscoveringSuccess();
    void onDiscoveringFailed(int statusCode);
    void onEndpointLost(String endpointId);
    void onEndpointFound(Host host, Client client);
    void onConnectionResponse();
}
