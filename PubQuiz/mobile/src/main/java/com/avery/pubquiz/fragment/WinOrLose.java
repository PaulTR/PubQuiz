package com.avery.pubquiz.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.messages.WinnerMessage;
import com.avery.pubquiz.R;

/**
 * Created by geoff on 6/10/16.
 */
public class WinOrLose extends Fragment implements View.OnClickListener{

    private static final String KEY_CLIENT = "key_client";
    private static final String KEY_MESSAGE = "key_message";
    private static final String TAG = WinOrLose.class.getSimpleName();


    public static WinOrLose getInstance(Client client, WinnerMessage message) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_CLIENT, client);
        args.putSerializable(KEY_MESSAGE, message);

        WinOrLose winOrLose = new WinOrLose();
        winOrLose.setArguments(args);

        return winOrLose;
    }

    private TextView mWinLoseText;

    private RelativeLayout mPlayAgainContainer;
    private Button mYesButton;
    private Button mNoButton;

    private WinOrLoseActions mWinOrLoseActions;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_win_or_lose, container, false);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWinLoseText = (TextView) view.findViewById(R.id.win_lose_text);

        mPlayAgainContainer = (RelativeLayout) view.findViewById(R.id.play_again_container);
        mYesButton = (Button) view.findViewById(R.id.yes_button);
        mNoButton = (Button) view.findViewById(R.id.no_button);

        mYesButton.setOnClickListener(this);
        mNoButton.setOnClickListener(this);

        Bundle args = getArguments();
        Client client = (Client) args.getSerializable(KEY_CLIENT);
        WinnerMessage message = (WinnerMessage) args.getSerializable(KEY_MESSAGE);

        if (client.getName().equalsIgnoreCase(message.getWinner())) {
            mWinLoseText.setText("Congratulations, You Win!");
            MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.congratulations_you_won);
            mediaPlayer.start();
        } else {
            mWinLoseText.setText("You Lose");
        }

        Vibrator v = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        long pattern[]={0,800};
        v.vibrate(pattern, -1);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mPlayAgainContainer.setVisibility(View.VISIBLE);
                mPlayAgainContainer.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_up));

            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {

        if(mWinOrLoseActions == null)
            return;

        int id = v.getId();
        if(id == R.id.yes_button) {
            mWinOrLoseActions.onPlayAgain(true);
        }else if(id == R.id.no_button) {
            mWinOrLoseActions.onPlayAgain(false);
        }
    }

    public void setWinOrLoseActions(WinOrLoseActions listener) {
        mWinOrLoseActions = listener;
    }


    public interface WinOrLoseActions {
        void onPlayAgain(boolean playAgain);
    }
}
