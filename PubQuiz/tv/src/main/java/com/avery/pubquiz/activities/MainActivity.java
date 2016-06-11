package com.avery.pubquiz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.avery.networking.model.Question;
import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.NearbyHostCallback;
import com.avery.networking.nearby.NearbyManager;
import com.avery.networking.nearby.messages.BaseMessage;
import com.avery.networking.nearby.messages.QuestionMessage;
import com.avery.networking.nearby.messages.RegisterMessage;
import com.avery.networking.nearby.messages.RegisterResponseMessage;
import com.avery.pubquiz.R;
import com.avery.pubquiz.fragments.FormTeamsFragment;
import com.avery.pubquiz.fragments.QuizQuestionFragment;

import java.util.ArrayList;

public class MainActivity extends Activity implements NearbyHostCallback {

    ArrayList<String> clientNames = new ArrayList<>();
    ArrayList<Client> mClients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
    }

    public void startAdvertising() {
        NearbyManager manager = NearbyManager.getInstance();
        manager.setNearbyHostCallback(this);
        manager.initialize(this);
    }

    public void sendQuestion(Question question) {
        NearbyManager.getInstance().sendQuestion(mClients, new QuestionMessage(question.getQuestion()));
    }

    private void initFragment() {
        getFragmentManager().beginTransaction().replace(R.id.container, FormTeamsFragment.getInstance()).commit();
    }

    @Override
    public void onConnectedSuccess() {
        Log.e("Nearby", "onConnectedSuccess");
        NearbyManager.getInstance().advertise();
    }

    @Override
    public void onConnectedFailed() {
        Log.e("Nearby", "onConnectedFailed");
    }

    @Override
    public void onAdvertisingSuccess() {
        Log.e("Nearby", "onAdvertisingSuccess");
    }

    @Override
    public void onAdvertisingFailed(int statusCode) {
        Log.e("Nearby", "onAdvertisingFailed: " + statusCode);
    }

    @Override
    public void onConnectionAccepted(Client client, BaseMessage message) {
        Log.e("Nearby", "onConnectionAccepted: " + message.messageType );

        if( message instanceof RegisterMessage ) {
            Log.e("Nearby", "is register message");
            RegisterMessage registerMessage = (RegisterMessage) message;
            if( !clientNames.contains(registerMessage.teamName) ) {
                Log.e("Nearby", "is regsiter message: " + registerMessage.teamName );
                clientNames.add(registerMessage.teamName);
                client.setName(registerMessage.teamName);
                mClients.add(client);
                NearbyManager.getInstance().sendTeamRegisteredResponse(client, new RegisterResponseMessage(true));

                if( clientNames.size() >= 1 ) {
                    loadQuiz();
                }
            } else {
                Log.e("Nearby", "send failed");
                NearbyManager.getInstance().sendTeamRegisteredResponse(client, new RegisterResponseMessage(false));
            }
        }
    }

    private void loadQuiz() {
        getFragmentManager().beginTransaction().replace(R.id.container, QuizQuestionFragment.getInstance()).commit();
    }

    @Override
    public void onConnectionFailed(int statusCode) {
        Log.e("Nearby", "onConnectionFailed");
    }

    @Override
    public void handleMessage(String endpointId, BaseMessage message) {
        Log.e("nearby", "message: " + message );
    }
}
