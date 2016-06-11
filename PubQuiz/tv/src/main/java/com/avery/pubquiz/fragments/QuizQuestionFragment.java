package com.avery.pubquiz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avery.networking.model.Question;
import com.avery.pubquiz.R;
import com.avery.pubquiz.Utils;
import com.avery.pubquiz.activities.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuizQuestionFragment extends Fragment {

    List<Question> mQuestions = new ArrayList<>();
    private TextView mQuestionTextView;

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

        mQuestionTextView = (TextView) view.findViewById(R.id.tv_question);
        loadQuestion();
    }

    private void loadQuestion() {
        mQuestionTextView.setText(mQuestions.get(0).getQuestion());
        ((MainActivity)getActivity()).sendQuestion( mQuestions.get(0) );
    }

    private void loadData() {
        String json = Utils.loadJSONFromResource( getActivity(), R.raw.questions );
        Gson gson = new Gson();
        Type collection = new TypeToken<ArrayList<Question>>(){}.getType();
        mQuestions = gson.fromJson( json, collection );
    }
}
