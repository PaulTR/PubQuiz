package com.avery.pubquiz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avery.networking.flowmeter.TapFlowManager;
import com.avery.networking.model.Question;
import com.avery.networking.model.tap.TapBeer;
import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.messages.AnswerMessage;
import com.avery.pubquiz.R;
import com.avery.pubquiz.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TieBreakerFragment extends Fragment implements TapFlowManager.TapFlowEventListener {

    private float mAmountRemaining = 0.0f;

    private HashMap<Client, Integer> receivedAnswers = new HashMap<>();

    private TextView mTieBreakerTextView;

    private static final String EXTRA_CLIENT_NAME_LIST = "extra_client_name_list";
    private static final String EXTRA_HIGH_SCORE = "extra_high_score";

    public static TieBreakerFragment getInstance(ArrayList<String> clients, int highestScore) {
        TieBreakerFragment fragment = new TieBreakerFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EXTRA_CLIENT_NAME_LIST, clients);
        bundle.putInt(EXTRA_HIGH_SCORE, highestScore);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_tie_breaker, container, false );
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTieBreakerTextView = (TextView) view.findViewById(R.id.tv_tie);

        TapFlowManager.getInstance().setTapFlowEventListener(this);
        TapFlowManager.getInstance().connect();
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onBeersReceived(final List<TapBeer> tapBeerList) {
        TapFlowManager.getInstance().setTapFlowEventListener(null);

        StringBuilder builder = new StringBuilder();
        for( String teamName : getArguments().getStringArrayList(EXTRA_CLIENT_NAME_LIST)) {
            builder.append(teamName).append("\n");
        }
        //builder.append(getArguments().getInt(EXTRA_HIGH_SCORE));
        final String teamNames = builder.toString();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                mTieBreakerTextView.setText("Tied Game! Winning teams, guess how much beer is in the keg of " + tapBeerList.get(0).getBeerName() + "!\n" + teamNames);
                mAmountRemaining = tapBeerList.get(random.nextInt(tapBeerList.size())).getAmountRemaining();

                Question question = new Question();
                question.setQuestionType("pour");
                ((MainActivity) getActivity()).sendQuestion(question);
                Log.e("Quiz", "amount: " + mAmountRemaining );
            }
        });
    }

    public void answerReceived(Client client, AnswerMessage answer ) {
        receivedAnswers.put(client, Integer.valueOf(answer.answer));
        if( ((MainActivity)getActivity()).getNumberOfTeams() == receivedAnswers.size() ) {
            determineWinner();
        }
    }

    private void determineWinner() {
        float guessDistance = 0.0f;
        Client closestGuesser = null;

        for (Map.Entry<Client, Integer> entry : receivedAnswers.entrySet()) {
            if( Math.abs( (mAmountRemaining - entry.getValue() ) ) > guessDistance ) {
                guessDistance = Math.abs(mAmountRemaining - entry.getValue());
                closestGuesser = entry.getKey();
            }
        }

        mTieBreakerTextView.setText("Winner is " + closestGuesser.getName() + "!");
    }
}
