package com.avery.pubquiz.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.Host;
import com.avery.networking.nearby.NearbyDiscoveryCallback;
import com.avery.networking.nearby.NearbyHostCallback;
import com.avery.networking.nearby.NearbyManager;
import com.avery.networking.nearby.messages.AnswerMessage;
import com.avery.networking.nearby.messages.BaseMessage;
import com.avery.networking.nearby.messages.QuestionMessage;
import com.avery.networking.nearby.messages.RegisterResponseMessage;
import com.avery.pubquiz.R;
import com.avery.pubquiz.fragment.LoadingFragment;
import com.avery.pubquiz.fragment.SelectAnswer;

public class MainActivity extends AppCompatActivity implements NearbyDiscoveryCallback,
        LoadingFragment.LoadingFragmentActions,
        SelectAnswer.SelectAnswerActions {

    private static final String TAG = MainActivity.class.getSimpleName();

    private NearbyManager mManager;
    private Client mClient;
    private Host mHost;


    private LoadingFragment mLoadingFragment;
    private SelectAnswer mSelectAnswerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = NearbyManager.getInstance();
        mManager.setNearbyDiscoveryCallback(this);

        mLoadingFragment = new LoadingFragment();
        mLoadingFragment.setLoadingFragmentActions(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mLoadingFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mManager.initialize(this);
    }

    @Override
    public void onConnectedSuccess() {
        Log.e(TAG, "onConnected Success");
        mManager.discover();
    }

    @Override
    public void onConnectedFailed() {
        Log.e(TAG, "onConnected Failed");
    }

    @Override
    public void onDiscoveringSuccess() {
        Log.e(TAG, "onDiscovering success");
    }

    @Override
    public void onDiscoveringFailed(int statusCode) {
        Log.e(TAG, "onDiscovering failed : " + statusCode);
    }

    @Override
    public void onEndpointLost(String endpointId) {
        Log.e(TAG, "onEndpointLost : " + endpointId);
    }

    @Override
    public void onEndpointFound(Host host, Client client) {
        Log.e(TAG, "onEndpoint Found");
        if(host != null) {
            mHost = host;
            Log.e(TAG, "host : " + host.getEndpointId() + " : " + host.getEndpointName());
            if(client != null) {
                mClient = client;
                mLoadingFragment.setConnected();
            }
        }
    }

    @Override
    public void onConnectionResponse() {
        Log.e(TAG, "onConnectionResponse!");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void handleMessage(String endpointId, BaseMessage message) {
        Log.e(TAG, "handle message!");
        if(message != null) {
            if(message instanceof RegisterResponseMessage) {
                boolean successful = ((RegisterResponseMessage) message).isSuccessful;
                if(successful) {
                    //show waiting for questions screen
                    mLoadingFragment.setWaitingForQuestion();
                }else {
                    mLoadingFragment.setTeamNameNotAvailable();
                }
            }else if(message instanceof QuestionMessage) {
                //show question screen
                showQuestionFragment((QuestionMessage) message);
            }
        }
    }

    private void showQuestionFragment(QuestionMessage message) {
        SelectAnswer selectAnswerFragment = SelectAnswer.getInstance(message);
        selectAnswerFragment.setSelectAnswerActions(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, selectAnswerFragment);
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.commit();
    }


    @Override
    public void onSetTeamName(String teamName) {
        mClient.setName(teamName);
        mManager.connectToHost(mHost, mClient);
    }

    @Override
    public void onAnswerSelected(String answer) {
        AnswerMessage message = new AnswerMessage();
        message.answer = answer;
        mManager.sendAnswer(mHost, message);
    }
}
