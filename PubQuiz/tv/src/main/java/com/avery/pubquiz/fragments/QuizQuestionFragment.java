package com.avery.pubquiz.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private int mMaxNumberOfQuestions = 1;
    private int mTimePerQuestion = 15;
    private int mCurrentTime;


    private LinearLayout mQuestionContainer;
    private TextView mQuestionTextView;
    private TextView mTimerTextView;
    private TextView mAnswerTextViewA;
    private TextView mAnswerTextViewB;
    private TextView mAnswerTextViewC;
    private TextView mAnswerTextViewD;

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
        loadJsonQuestions();

        initViews(view);
        loadQuestion();
    }

    private void loadImageQuestions() {

    }

    private void loadBeerQuestions() {

    }

    private void showTieBreaker() {

    }

    private void initViews(View view) {
        mQuestionContainer = (LinearLayout) view.findViewById(R.id.container_question);
        mQuestionTextView = (TextView) view.findViewById(R.id.tv_question);
        mTimerTextView = (TextView) view.findViewById(R.id.tv_time);
        mAnswerTextViewA = (TextView) view.findViewById(R.id.answer_a);
        mAnswerTextViewB = (TextView) view.findViewById(R.id.answer_b);
        mAnswerTextViewC = (TextView) view.findViewById(R.id.answer_c);
        mAnswerTextViewD = (TextView) view.findViewById(R.id.answer_d);

        mCorrectTeamsContainer = (RelativeLayout) view.findViewById(R.id.container_correct_teams);
        mTeam1 = (TextView) view.findViewById(R.id.tv_team_1);
        mTeam2 = (TextView) view.findViewById(R.id.tv_team_2);
        mCorrectTeamsContainer.setVisibility(View.GONE);

        mEveryonesWrongContainer = (RelativeLayout) view.findViewById(R.id.tv_everyone_wrong);
        mEveryonesWrongContainer.setVisibility(View.GONE);

    }

    private void loadQuestion() {
        mCurrentQuestionNumber++;
        mCurrentTime = mTimePerQuestion;
        mCorrectTeams.clear();
        mInCorrectTeams.clear();
        mQuestionContainer.setVisibility(View.VISIBLE);
        mCorrectTeamsContainer.setVisibility(View.GONE);
        mEveryonesWrongContainer.setVisibility(View.GONE);
        mCurrentQuestion = mQuestions.get( ((new Random()).nextInt(10)) );

        mCurrentQuestion.setQuestionType("multiple-choice");
        mQuestionTextView.setText(mCurrentQuestion.getQuestion());
        mAnswerTextViewA.setText(mCurrentQuestion.getA());
        mAnswerTextViewB.setText(mCurrentQuestion.getB());
        mAnswerTextViewC.setText(mCurrentQuestion.getC());
        mAnswerTextViewD.setText(mCurrentQuestion.getD());

        mTimerTextView.setText(String.valueOf(mCurrentTime));
        ((MainActivity) getActivity()).sendQuestion( mCurrentQuestion );

        new QuestionTimerTask().execute();
    }

    private void loadJsonQuestions() {
        String json = Utils.loadJSONFromResource( getActivity(), R.raw.questions );
        Gson gson = new Gson();
        Type collection = new TypeToken<ArrayList<Question>>(){}.getType();
        mQuestions = gson.fromJson( json, collection );
    }

    public void showWinner() {
        ((MainActivity) getActivity()).showWinner();
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
            displayCorrectTeams();
        }
    }

    private void displayCorrectTeams() {
        mQuestionContainer.setVisibility(View.GONE);

        if( mCorrectTeams.isEmpty() ) {
            Log.e("Quiz", "Everybodys wrong");
            mEveryonesWrongContainer.setVisibility(View.VISIBLE);
        } else {
            mTeam2.setVisibility(View.GONE);
            if( mCorrectTeams.size() >= 1 ) {
                mTeam1.setText(mCorrectTeams.get(0).getName());
            }
            if( mCorrectTeams.size() >= 2 ) {
                mTeam2.setText(mCorrectTeams.get(1).getName());
                mTeam2.setVisibility(View.VISIBLE);
            }
            mCorrectTeamsContainer.setVisibility(View.VISIBLE);
        }

        new showQuestionResultTask().execute();
    }

    public class QuestionTimerTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mTimerTextView.setText(String.valueOf(values[0]));
        }

        @Override
        protected Void doInBackground(Void... params) {
            while ( !(mInCorrectTeams.size() + mCorrectTeams.size() == ((MainActivity) getActivity()).getNumberOfTeams() )
                    && mCurrentTime > 0 ) {
                try {
                    Thread.sleep(1000);
                } catch( InterruptedException e ) {

                }

                mCurrentTime--;
                publishProgress(mCurrentTime);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if( !(mInCorrectTeams.size() + mCorrectTeams.size() == ((MainActivity) getActivity()).getNumberOfTeams() ) ) {
                displayCorrectTeams();
            }
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
                Log.e("Quiz", "mCurrentQuestionNumber: " + mCurrentQuestionNumber );
                showWinner();
            } else {
                loadQuestion();
            }
        }
    }



}
