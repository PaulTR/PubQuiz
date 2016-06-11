package com.avery.networking.flowmeter;

import android.util.Log;

import com.avery.networking.model.tap.TapBeer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import java.util.List;

/**
 * Created by mark on 6/10/16.
 */
public class TapFlowManager {

    private static final String TAG = TapFlowManager.class.getSimpleName();
    private static final String API_KEY = "4fc24b61958c6d8b4e01";
    private static final String CHANNEL = "taproom";
    private static final String EVENT = "flowmeter-update";
    private static TapFlowManager sInstance;

    public static TapFlowManager getInstance() {
        if(sInstance == null) {
            sInstance = new TapFlowManager();
        }

        return sInstance;
    }

    private Pusher mPusher;
    private TapFlowEventListener mEventListener;


    public TapFlowManager() {
        mPusher = new Pusher(API_KEY);
    }

    public void connect() {
        mPusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                if(mEventListener == null) {
                    return;
                }

                if(ConnectionState.CONNECTED.equals(change.getCurrentState())) {
                    mEventListener.onConnected();
                }else if(ConnectionState.DISCONNECTED.equals(change.getCurrentState())) {
                    mEventListener.onDisconnected();
                }
            }

            @Override
            public void onError(String message, String code, Exception e) {
                if(mEventListener != null) {
                    mEventListener.onError(e);
                }
            }
        }, ConnectionState.ALL);

        Channel channel = mPusher.subscribe(CHANNEL);

        channel.bind(EVENT, new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Gson gson = new Gson();
                List<TapBeer> tapBeerList = gson.fromJson(data, new TypeToken<List<TapBeer>>(){}.getType());
                if(mEventListener != null) {
                    mEventListener.onBeersReceived(tapBeerList);
                }
            }
        });

    }


    public void disconnect() {
        mPusher.disconnect();
        mEventListener = null;
    }


    public void setTapFlowEventListener(TapFlowEventListener listener) {
        mEventListener = listener;
    }


    public interface TapFlowEventListener {
        void onConnected();
        void onDisconnected();
        void onError(Exception e);
        void onBeersReceived(List<TapBeer> tapBeerList);
    }

}
