package com.avery.pubquiz.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avery.networking.model.Question;
import com.avery.networking.model.beer.Beer;
import com.avery.networking.model.beer.BeerList;
import com.avery.networking.model.beer.BeerResult;
import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.messages.AnswerMessage;
import com.avery.networking.services.AveryNetworkAdapter;
import com.avery.pubquiz.R;
import com.avery.pubquiz.Utils;
import com.avery.pubquiz.activities.MainActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizQuestionFragment extends Fragment {

    private int mCurrentQuestionNumber = 0;
    private int mMaxNumberOfQuestions = 10;
    private int mTimePerQuestion = 15;
    private int mCurrentTime;


    private RelativeLayout mMultipleChoiceContainer;
    private LinearLayout mQuestionContainer;
    private TextView mQuestionTextView;
    private TextView mTimerTextView;
    private TextView mAnswerTextViewA;
    private TextView mAnswerTextViewB;
    private TextView mAnswerTextViewC;
    private TextView mAnswerTextViewD;

    private ImageView mBeerImage;
    private View mImageBlocker1;
    private View mImageBlocker2;
    private View mImageBlocker3;
    private View mImageBlocker4;
    private View mImageBlocker5;
    private View mImageBlocker6;
    private View mImageBlocker7;
    private View mImageBlocker8;
    private View mImageBlocker9;

    private BeerList mBeerList;

    private RelativeLayout mImageQuestionContainer;

    private RelativeLayout mCorrectTeamsContainer;
    private TextView mTeam1;
    private TextView mTeam2;

    private RelativeLayout mEveryonesWrongContainer;

    private List<Question> mQuestions = new ArrayList<>();
    private List<Client> mCorrectTeams = new ArrayList<>();
    private List<Client> mInCorrectTeams = new ArrayList<>();

    private Question mCurrentQuestion;

    private List<Integer> imageBlocks = new ArrayList<>();

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void loadImageQuestions() {

        mImageBlocker1.setVisibility(View.VISIBLE);
        mImageBlocker2.setVisibility(View.VISIBLE);
        mImageBlocker3.setVisibility(View.VISIBLE);
        mImageBlocker4.setVisibility(View.VISIBLE);
        mImageBlocker5.setVisibility(View.VISIBLE);
        mImageBlocker6.setVisibility(View.VISIBLE);
        mImageBlocker7.setVisibility(View.VISIBLE);
        mImageBlocker8.setVisibility(View.VISIBLE);
        mImageBlocker9.setVisibility(View.VISIBLE);

        mTimerTextView.setVisibility(View.GONE);
        imageBlocks.add(1);
        imageBlocks.add(2);
        imageBlocks.add(3);
        imageBlocks.add(4);
        imageBlocks.add(5);
        imageBlocks.add(6);
        imageBlocks.add(7);
        imageBlocks.add(8);
        imageBlocks.add(9);
        mMultipleChoiceContainer.setVisibility(View.GONE);

        final List<String> beerIds = new ArrayList<>();
        beerIds.add("ipa");
        beerIds.add("hog-heaven");
        beerIds.add("joe-s-pils");
        beerIds.add("liliko-i-kepolo");
        beerIds.add("mephistopheles");
        beerIds.add("old-jubiliation-ale");
        beerIds.add("out-of-bounds-stout");
        beerIds.add("perzik-saison");
        beerIds.add("pump-ky-n");
        beerIds.add("raja");
        beerIds.add("raspberry-sour");
        beerIds.add("rumpkin");
        beerIds.add("salvation");
        beerIds.add("samael-s");
        beerIds.add("the-beast");
        beerIds.add("the-czar");
        beerIds.add("the-kaiser");
        beerIds.add("the-maharaja");
        beerIds.add("the-reverend");
        beerIds.add("tweak");
        beerIds.add("twenty-three-anniversary");
        beerIds.add("uncle-jacob-s-stout");
        beerIds.add("vanilla-bean-stout");
        beerIds.add("white-rascal");
        beerIds.add("ellie-s-brown-ale");

        AveryNetworkAdapter.getInstance().getService().getBeers().enqueue(new Callback<BeerList>() {
            @Override
            public void onResponse(Call<BeerList> call, Response<BeerList> response) {
                mBeerList = response.body();

                Random random = new Random();
                AveryNetworkAdapter.getInstance().getService().getBeer(beerIds.get(random.nextInt(beerIds.size()))).enqueue(new Callback<BeerResult>() {
                    @Override
                    public void onResponse(Call<BeerResult> call, Response<BeerResult> response) {
                        Beer beer = response.body().getBeer();
                        if( !isAdded() ) {
                            return;
                        }

                        Glide.with(getActivity()).load(beer.getLabelImage().getDesktop2x()).into(mBeerImage);
                        Question question = new Question();
                        question.setQuestionType("multiple-choice");
                        question.setQuestion("Name that beer!");
                        Random randomAnswer = new Random();
                        switch( randomAnswer.nextInt(4) ) {
                            case 0: {
                                question.setA(beer.getName());
                                question.setAnswer("A");
                                question.setB(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                question.setC(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                question.setD(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                break;
                            }
                            case 1: {
                                question.setB(beer.getName());
                                question.setAnswer("B");
                                question.setA(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                question.setC(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                question.setD(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                break;
                            }
                            case 2: {
                                question.setC(beer.getName());
                                question.setAnswer("C");
                                question.setB(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                question.setA(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                question.setD(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                break;
                            }
                            case 3: {
                                question.setD(beer.getName());
                                question.setAnswer("D");
                                question.setB(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                question.setC(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                question.setA(mBeerList.beers.get(randomAnswer.nextInt(beerIds.size())).getName());
                                break;
                            }
                        }
                        mCurrentQuestion = question;
                        ((MainActivity) getActivity()).sendQuestion(question);

                        new removeBeerBlockTask().execute();
                    }

                    @Override
                    public void onFailure(Call<BeerResult> call, Throwable t) {
                        Log.e("blocks", "beer load failure");
                    }
                });
            }

            @Override
            public void onFailure(Call<BeerList> call, Throwable t) {
                Log.e("blocks", "error loading beer list");
            }
        });
    }

    private void loadBeerQuestions() {
        AveryNetworkAdapter.getInstance().getService().getBeers().enqueue(new Callback<BeerList>() {
            @Override
            public void onResponse(Call<BeerList> call, Response<BeerList> response) {
                mBeerList = response.body();

                mTimerTextView.setVisibility(View.VISIBLE);
                mCurrentQuestion = mQuestions.get(((new Random()).nextInt(mQuestions.size())));
                mCurrentQuestion.setQuestionType("multiple-choice");
                mCurrentTime = mTimePerQuestion;
                mTimerTextView.setText(String.valueOf(mCurrentTime));
                mMultipleChoiceContainer.setVisibility(View.VISIBLE);
                mImageQuestionContainer.setVisibility(View.GONE);

                List<Beer> beers = response.body().beers;
                Random random = new Random();
                Beer answerBeer = response.body().beers.get(random.nextInt(response.body().beers.size()));
                mCurrentQuestion.setQuestion("What is the ABV of " + answerBeer.getName() + "?");

                Random randomAnswer = new Random();
                switch( randomAnswer.nextInt(4) ) {
                    case 0: {
                        mCurrentQuestion.setA(answerBeer.getAbv());
                        mCurrentQuestion.setAnswer("A");
                        mCurrentQuestion.setB(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        mCurrentQuestion.setC(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        mCurrentQuestion.setD(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        break;
                    }
                    case 1: {
                        mCurrentQuestion.setB(answerBeer.getAbv());
                        mCurrentQuestion.setAnswer("B");
                        mCurrentQuestion.setA(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        mCurrentQuestion.setC(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        mCurrentQuestion.setD(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        break;
                    }
                    case 2: {
                        mCurrentQuestion.setC(answerBeer.getAbv());
                        mCurrentQuestion.setAnswer("C");
                        mCurrentQuestion.setB(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        mCurrentQuestion.setA(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        mCurrentQuestion.setD(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        break;
                    }
                    case 3: {
                        mCurrentQuestion.setD(answerBeer.getAbv());
                        mCurrentQuestion.setAnswer("D");
                        mCurrentQuestion.setB(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        mCurrentQuestion.setC(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        mCurrentQuestion.setA(mBeerList.beers.get(randomAnswer.nextInt(beers.size())).getAbv());
                        break;
                    }
                }

                mQuestionTextView.setText(mCurrentQuestion.getQuestion());
                mAnswerTextViewA.setText(mCurrentQuestion.getA());
                mAnswerTextViewB.setText(mCurrentQuestion.getB());
                mAnswerTextViewC.setText(mCurrentQuestion.getC());
                mAnswerTextViewD.setText(mCurrentQuestion.getD());

                ((MainActivity) getActivity()).sendQuestion(mCurrentQuestion);
                new QuestionTimerTask().execute();

            }

            @Override
            public void onFailure(Call<BeerList> call, Throwable t) {

            }
        });
    }

    private void initViews(View view) {
        mMultipleChoiceContainer = (RelativeLayout) view.findViewById(R.id.multiple_choice_container);
        mImageQuestionContainer = (RelativeLayout) view.findViewById(R.id.image_question_container);

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

        mImageBlocker1 = view.findViewById(R.id.image_blocker_1);
        mImageBlocker2 = view.findViewById(R.id.image_blocker_2);
        mImageBlocker3 = view.findViewById(R.id.image_blocker_3);
        mImageBlocker4 = view.findViewById(R.id.image_blocker_4);
        mImageBlocker5 = view.findViewById(R.id.image_blocker_5);
        mImageBlocker6 = view.findViewById(R.id.image_blocker_6);
        mImageBlocker7 = view.findViewById(R.id.image_blocker_7);
        mImageBlocker8 = view.findViewById(R.id.image_blocker_8);
        mImageBlocker9 = view.findViewById(R.id.image_blocker_9);
        mBeerImage = (ImageView) view.findViewById(R.id.beer_image);

    }

    private void loadMultipleChoiceQuestion() {
        mTimerTextView.setVisibility(View.VISIBLE);
        mCurrentQuestion = mQuestions.get(((new Random()).nextInt(mQuestions.size())));
        mCurrentQuestion.setQuestionType("multiple-choice");
        mCurrentTime = mTimePerQuestion;
        mTimerTextView.setText(String.valueOf(mCurrentTime));
        mMultipleChoiceContainer.setVisibility(View.VISIBLE);
        mImageQuestionContainer.setVisibility(View.GONE);

        mQuestionTextView.setText(mCurrentQuestion.getQuestion());
        mAnswerTextViewA.setText(mCurrentQuestion.getA());
        mAnswerTextViewB.setText(mCurrentQuestion.getB());
        mAnswerTextViewC.setText(mCurrentQuestion.getC());
        mAnswerTextViewD.setText(mCurrentQuestion.getD());


        ((MainActivity) getActivity()).sendQuestion(mCurrentQuestion);

        new QuestionTimerTask().execute();
    }

    private void loadQuestion() {
        mCurrentQuestionNumber++;
        mCorrectTeams.clear();
        mInCorrectTeams.clear();
        mQuestionContainer.setVisibility(View.VISIBLE);
        mCorrectTeamsContainer.setVisibility(View.GONE);
        mEveryonesWrongContainer.setVisibility(View.GONE);

        Random random = new Random();
        int selection = random.nextInt(5);


        if( selection == 0 ) {
            loadImageQuestions();
        }
        else {
            if( random.nextBoolean() ) {
                loadMultipleChoiceQuestion();
            } else {
                loadBeerQuestions();
            }
        }
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

    public class removeBeerBlockTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.e("blocks", "image blocks is empty : " + imageBlocks.isEmpty());
            if( imageBlocks.isEmpty() ) {
                return;
            }

            Random random = new Random();
            int item = random.nextInt(imageBlocks.size());
            int block = imageBlocks.get(item);
            imageBlocks.remove(item);
            Log.e("blocks", "onProgressUpdate : Item : " + item);
            switch( block ) {
                case 1: {
                    mImageBlocker1.setVisibility(View.INVISIBLE);
                    break;
                }
                case 2: {
                    mImageBlocker2.setVisibility(View.INVISIBLE);
                    break;
                }
                case 3: {
                    mImageBlocker3.setVisibility(View.INVISIBLE);
                    break;
                }
                case 4: {
                    mImageBlocker4.setVisibility(View.INVISIBLE);
                    break;
                }
                case 5: {
                    mImageBlocker5.setVisibility(View.INVISIBLE);
                    break;
                }
                case 6: {
                    mImageBlocker6.setVisibility(View.INVISIBLE);
                    break;
                }
                case 7: {
                    mImageBlocker7.setVisibility(View.INVISIBLE);
                    break;
                }
                case 8: {
                    mImageBlocker8.setVisibility(View.INVISIBLE);
                    break;
                }
                case 9: {
                    mImageBlocker9.setVisibility(View.INVISIBLE);
                    break;
                }

            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("blocks", "block size : " + imageBlocks.size());
            while( imageBlocks.size() > 0 && mCorrectTeams.isEmpty() ) {
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e) {

                }
                Log.e("blocks", "publish progress");
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            displayCorrectTeams();
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
