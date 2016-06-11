package com.avery.pubquiz.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avery.networking.nearby.messages.ScoreUpdateMessage;
import com.avery.pubquiz.R;

/**
 * Created by mark on 6/11/16.
 */
public class ScoreUpdate extends Fragment {

    private static final String KEY_MESSAGE = "key_message";

    public static ScoreUpdate getInstance(ScoreUpdateMessage message) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_MESSAGE, message);

        ScoreUpdate scoreUpdate = new ScoreUpdate();
        scoreUpdate.setArguments(args);
        return scoreUpdate;
    }


    private TextView mScoreUpdateText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_score_update, container, false);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mScoreUpdateText = (TextView) view.findViewById(R.id.win_lose_text);

        Bundle args = getArguments();
        ScoreUpdateMessage message = (ScoreUpdateMessage) args.getSerializable(KEY_MESSAGE);

        mScoreUpdateText.setText(message.teamName + " : " + message.score + " pts");
    }
}
