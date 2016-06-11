package com.avery.pubquiz.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avery.networking.nearby.messages.QuestionMessage;
import com.avery.pubquiz.R;

/**
 * Created by geoff on 6/10/16.
 */
public class SelectAnswer extends Fragment {

    private static final String KEY_QUESTION = "key_question";


    public static SelectAnswer getInstance(QuestionMessage message) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_QUESTION, message);

        SelectAnswer selectAnswer = new SelectAnswer();
        selectAnswer.setArguments(args);

        return selectAnswer;
    }


    private TextView mQuestionText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_select_answer, container, false);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mQuestionText = (TextView) view.findViewById(R.id.question_text);

        Bundle args = getArguments();
        QuestionMessage message = (QuestionMessage) args.getSerializable(KEY_QUESTION);
        mQuestionText.setText(message.question);
    }
}
