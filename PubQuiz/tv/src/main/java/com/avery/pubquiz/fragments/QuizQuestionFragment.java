package com.avery.pubquiz.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avery.networking.model.Question;
import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.messages.AnswerMessage;
import com.avery.pubquiz.R;
import com.avery.pubquiz.Utils;
import com.avery.pubquiz.activities.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizQuestionFragment extends Fragment {

    private int mCurrentQuestionNumber = 0;
    private int mMaxNumberOfQuestions = 10;

    private RelativeLayout mQuestionContainer;
    private TextView mQuestionTextView;

    private RelativeLayout mCorrectTeamsContainer;
    private TextView mTeam1;
    private TextView mTeam2;

    private RelativeLayout mEveryonesWrongContainer;

    private List<Question> mQuestions = new ArrayList<>();
    private List<Client> mCorrectTeams = new ArrayList<>();
    private List<Client> mInCorrectTeams = new ArrayList<>();

    private Question mCurrentQuestion;

    public static QuizQuestionFragment getInstance() {
        QuizQuestionFragment fragment = new QuizQuestionFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_quiz_question, container, false );
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();

        initViews(view);
        loadQuestion();
    }

    private void initViews(View view) {
        mQuestionContainer = (RelativeLayout) view.findViewById(R.id.container_question);
        mQuestionTextView = (TextView) view.findViewById(R.id.tv_question);

        mCorrectTeamsContainer = (RelativeLayout) view.findViewById(R.id.container_correct_teams);
        mTeam1 = (TextView) view.findViewById(R.id.tv_team_1);
        mTeam2 = (TextView) view.findViewById(R.id.tv_team_2);
        mCorrectTeamsContainer.setVisibility(View.GONE);

        mEveryonesWrongContainer = (RelativeLayout) view.findViewById(R.id.tv_everyone_wrong);
        mEveryonesWrongContainer.setVisibility(View.GONE);

    }

    private void loadQuestion() {
        mCurrentQuestionNumber++;
        mCorrectTeams.clear();
        mInCorrectTeams.clear();
        mQuestionContainer.setVisibility(View.VISIBLE);
        mCorrectTeamsContainer.setVisibility(View.GONE);
        mEveryonesWrongContainer.setVisibility(View.GONE);
        mCurrentQuestion = mQuestions.get( ((new Random()).nextInt(10)) );
        mQuestionTextView.setText(mCurrentQuestion.getQuestion());
        ((MainActivity)getActivity()).sendQuestion( mCurrentQuestion );
    }

    private void loadData() {
        String json = Utils.loadJSONFromResource( getActivity(), R.raw.questions );
        Gson gson = new Gson();
        Type collection = new TypeToken<ArrayList<Question>>(){}.getType();
        mQuestions = gson.fromJson( json, collection );
    }

    public void answerReceived(Client client, AnswerMessage message) {
        if( mCurrentQuestion.getAnswer().equalsIgnoreCase(message.answer) ) {
            ( (MainActivity) getActivity() ).addToClientScore(client, 10);
            mCorrectTeams.add(client);
        } else {
            mInCorrectTeams.add(client);
        }

        //Everyone has answered
        if( mInCorrectTeams.size() + mCorrectTeams.size() == ((MainActivity) getActivity()).getNumberOfTeams() ) {
            //display who got it right, then load question
            //loadQuestion();
            displayCorrectTeams();
        }
    }

    private void displayCorrectTeams() {
        mQuestionContainer.setVisibility(View.GONE);

        if( mCorrectTeams.isEmpty() ) {
            Log.e("Quiz", "Everybodys wrong");
            mEveryonesWrongContainer.setVisibility(View.VISIBLE);
        } else {
            if( mCorrectTeams.size() >= 1 ) {
                mTeam1.setText(mCorrectTeams.get(0).getName());
            }
            if( mCorrectTeams.size() >= 2 ) {
                mTeam2.setText(mCorrectTeams.get(1).getName());
            }
            mCorrectTeamsContainer.setVisibility(View.VISIBLE);
        }

        new showQuestionResultTask().execute();
    }

    public class QuestionTimerTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    public class showQuestionResultTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e ) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if( mCurrentQuestionNumber >= mMaxNumberOfQuestions ) {
                ((MainActivity) getActivity()).showWinner();
            } else {
                loadQuestion();
            }
        }
    }



}
