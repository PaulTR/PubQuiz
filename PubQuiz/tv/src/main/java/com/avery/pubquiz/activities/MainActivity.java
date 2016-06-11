package com.avery.pubquiz.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.avery.networking.model.Question;
import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.NearbyHostCallback;
import com.avery.networking.nearby.NearbyManager;
import com.avery.networking.nearby.messages.AnswerMessage;
import com.avery.networking.nearby.messages.BaseMessage;
import com.avery.networking.nearby.messages.QuestionMessage;
import com.avery.networking.nearby.messages.RegisterMessage;
import com.avery.networking.nearby.messages.RegisterResponseMessage;
import com.avery.pubquiz.R;
import com.avery.pubquiz.fragments.FormTeamsFragment;
import com.avery.pubquiz.fragments.QuizQuestionFragment;
import com.avery.pubquiz.fragments.WinningFragment;

import java.util.ArrayList;

public class MainActivity extends Activity implements NearbyHostCallback {

    ArrayList<String> clientNames = new ArrayList<>();
    ArrayList<Client> mClients = new ArrayList<>();
    ArrayList<Integer> mTeamScores = new ArrayList<>();

    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initFragment();
    }

    public void startAdvertising() {
        NearbyManager manager = NearbyManager.getInstance();
        manager.setNearbyHostCallback(this);
        manager.initialize(this);
    }

    public void sendQuestion(Question question) {
        NearbyManager.getInstance().sendQuestion(mClients, new QuestionMessage(question));
    }

    private void initFragment() {
        mCurrentFragment = FormTeamsFragment.getInstance();
        getFragmentManager().beginTransaction().replace(R.id.container, mCurrentFragment).commit();
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
                mTeamScores.add(0);
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
        mCurrentFragment = QuizQuestionFragment.getInstance();
        getFragmentManager().beginTransaction().replace(R.id.container, mCurrentFragment).commit();
    }

    @Override
    public void onConnectionFailed(int statusCode) {
        Log.e("Nearby", "onConnectionFailed");
    }

    @Override
    public void handleMessage(String endpointId, BaseMessage message) {
        Log.e("nearby", "message: " + message );

        if( message instanceof AnswerMessage ) {
            AnswerMessage answerMessage = (AnswerMessage) message;
            Log.e("Nearby", "answer! " + answerMessage.answer);

            if( mCurrentFragment instanceof QuizQuestionFragment ) {
                for( Client client : mClients ) {
                    if( client.getEndpointId().equalsIgnoreCase(endpointId) ) {
                        ((QuizQuestionFragment) mCurrentFragment).answerReceived(client, answerMessage);
                    }
                }
            }
        }
    }

    public void addToClientScore(Client client, int clientScore) {
        mTeamScores.set(mClients.indexOf(client), mTeamScores.get(mClients.indexOf(client)) + clientScore);

        Log.e("Quiz", "Team: " + client.getName() + " score: " + mTeamScores.get(mClients.indexOf(client)));
    }

    public Client checkForWinner() {
        //check for ties or a specific winner
        Client winningClient = null;
        Integer highestScore = 0;
        for( int i = 0; i < mTeamScores.size(); i++ ) {
            if( highestScore < mTeamScores.get(i) ) {
                winningClient = mClients.get(i);
                highestScore = mTeamScores.get(i);
            }
        }
        //return null if tie

        return winningClient;
    }

    public void showWinner() {
        Client winningClient = checkForWinner();

        if( winningClient != null ) {
            mCurrentFragment = WinningFragment.getInstance(winningClient.getName(), mTeamScores.get(mClients.indexOf(winningClient)));
            getFragmentManager().beginTransaction().replace(R.id.container, mCurrentFragment).commit();
        }


    }

    public int getNumberOfTeams() {
        return mClients.size();
    }
}
