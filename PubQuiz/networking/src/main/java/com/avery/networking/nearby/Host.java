package com.avery.networking.nearby;

import java.io.Serializable;

/**
 * Created by mark on 6/10/16.
 */
public class Host implements Serializable {
    private final String mEndpointId;
    private final String mEndpointName;

    public Host(final String endpointId, final String endpointName) {
        mEndpointId = endpointId;
        mEndpointName = endpointName;
    }

    public String getEndpointId() {
        return mEndpointId;
    }

    public String getEndpointName() {
        return mEndpointName;
    }
}
