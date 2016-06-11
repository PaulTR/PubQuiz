package com.avery.networking.nearby;

/**
 * Created by mark on 6/10/16.
 */
public class Client {

    private final String mName;
    private String mEndpointId;

    public Client(final String endpointId, final String name) {
        mName = name;
        mEndpointId = endpointId;
    }

    public String getName() {
        return mName;
    }

    public String getEndpointId() {
        return mEndpointId;
    }

    public void setEndpointId(final String endpointId) {
        mEndpointId = endpointId;
    }
}
