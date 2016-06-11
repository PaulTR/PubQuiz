package com.avery.pubquiz.activity;

import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements NearbyDiscoveryCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private NearbyManager mManager;
    private Client mClient;
    private Host mHost;

    private TextView mQuestionText;
    private Button answerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        answerButton = (Button) findViewById(R.id.answer_button);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnswerMessage message = new AnswerMessage();
                message.answer = "A";
                mManager.sendAnswer(mHost, message);
            }
        });
        answerButton.setVisibility(View.GONE);

        mQuestionText = (TextView) findViewById(R.id.question_text);
        mQuestionText.setVisibility(View.GONE);

        mManager = NearbyManager.getInstance();
        mManager.setNearbyDiscoveryCallback(this);
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
                client.setName("my stupid name");
                mManager.connectToHost(host, client);
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
                Log.e(TAG, "Register response is successful: " + ((RegisterResponseMessage) message).isSuccessful );
            }else if(message instanceof QuestionMessage) {
                answerButton.setVisibility(View.VISIBLE);
                mQuestionText.setVisibility(View.VISIBLE);
                mQuestionText.setText(((QuestionMessage) message).question);
            }
        }
    }
}
