package com.avery.pubquiz.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.messages.WinnerMessage;
import com.avery.pubquiz.R;

/**
 * Created by geoff on 6/10/16.
 */
public class WinOrLose extends Fragment {

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

        Bundle args = getArguments();
        Client client = (Client) args.getSerializable(KEY_CLIENT);
        WinnerMessage message = (WinnerMessage) args.getSerializable(KEY_MESSAGE);

        Log.e(TAG, "CLIENT NAME : " + client.getName());
        Log.e(TAG, "WINNER : " + message.getWinner());
        if (client.getName().equalsIgnoreCase(message.getWinner())) {
            mWinLoseText.setText("Congratulations, You Win!");
        } else {
            mWinLoseText.setText("You Lose");
        }

        Vibrator v = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        long pattern[]={0,800};
        v.vibrate(pattern, -1);
    }
}
