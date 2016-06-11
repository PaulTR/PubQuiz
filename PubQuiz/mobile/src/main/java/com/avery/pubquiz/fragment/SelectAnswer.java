package com.avery.pubquiz.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avery.networking.nearby.NearbyManager;
import com.avery.networking.nearby.messages.QuestionMessage;
import com.avery.pubquiz.R;

/**
 * Created by geoff on 6/10/16.
 */
public class SelectAnswer extends Fragment implements View.OnClickListener {

    private static final String KEY_QUESTION = "key_question";


    public static SelectAnswer getInstance(QuestionMessage message) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_QUESTION, message);

        SelectAnswer selectAnswer = new SelectAnswer();
        selectAnswer.setArguments(args);

        return selectAnswer;
    }



    private TextView mQuestionText;

    private TextView mAnswerText1;
    private TextView mAnswerText2;
    private TextView mAnswerText3;
    private TextView mAnswerText4;

    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private SelectAnswerActions mSelectAnswerActions;


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

        mAnswerText1 = (TextView) view.findViewById(R.id.answer_text_1);
        mAnswerText2 = (TextView) view.findViewById(R.id.answer_text_2);
        mAnswerText3 = (TextView) view.findViewById(R.id.answer_text_3);
        mAnswerText4 = (TextView) view.findViewById(R.id.answer_text_4);

        mAnswerText1.setText(message.A);
        mAnswerText2.setText(message.B);
        mAnswerText3.setText(message.C);
        mAnswerText4.setText(message.D);

        mAnswerButton1 = (Button) view.findViewById(R.id.answer_button_1);
        mAnswerButton2 = (Button) view.findViewById(R.id.answer_button_2);
        mAnswerButton3 = (Button) view.findViewById(R.id.answer_button_3);
        mAnswerButton4 = (Button) view.findViewById(R.id.answer_button_4);

        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(mSelectAnswerActions == null) {
            return;
        }

        String answer = "";
        int id = v.getId();
        if(id == R.id.answer_button_1) {
            answer = "A";
        }else if(id == R.id.answer_button_2) {
            answer = "B";
        }else if(id == R.id.answer_button_3) {
            answer = "C";
        }else if(id == R.id.answer_button_4) {
            answer = "D";
        }

        mAnswerButton1.setEnabled(false);
        mAnswerButton2.setEnabled(false);
        mAnswerButton3.setEnabled(false);
        mAnswerButton4.setEnabled(false);

        mSelectAnswerActions.onAnswerSelected(answer);
    }


    public void setSelectAnswerActions(SelectAnswerActions listener) {
        mSelectAnswerActions = listener;
    }


    public interface SelectAnswerActions {
        void onAnswerSelected(String answer);
    }
}
