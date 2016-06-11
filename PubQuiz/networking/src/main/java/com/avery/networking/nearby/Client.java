package com.avery.networking.nearby;

/**
 * Created by mark on 6/10/16.
 */
public class Client {

    private String mName;
    private String mClientId;
    private String mEndpointId;

    public Client(final String endpointId, final String clientId) {
        mClientId = clientId;
        mEndpointId = endpointId;
    }

    public Client(final String endpointId, final String clientId, final String teamName) {
        mClientId = clientId;
        mEndpointId = endpointId;
        mName = teamName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(String mClientId) {
        this.mClientId = mClientId;
    }

    public String getEndpointId() {
        return mEndpointId;
    }

    public void setEndpointId(final String endpointId) {
        mEndpointId = endpointId;
    }
}
