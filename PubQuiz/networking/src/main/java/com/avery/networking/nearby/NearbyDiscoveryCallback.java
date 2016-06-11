package com.avery.networking.nearby;

/**
 * Created by mark on 6/10/16.
 */
public interface NearbyDiscoveryCallback {

    void onDiscoveringSuccess();
    void onDiscoveringFailed(int statusCode);
    void onEndpointLost(String endpointId);
    void onEndpointFound(String endpointId, String endpointName);
}
