package com.avery.pubquiz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avery.pubquiz.R;

public class WinningFragment extends Fragment {

    private static final String EXTRA_TEAM_NAME = "extra_team_name";
    private static final String EXTRA_TEAM_SCORE = "extra_team_score";

    private String mTeamName;
    private int mTeamScore;

    private TextView mWinnerTextView;

    public static WinningFragment getInstance(String teamName, int teamScore) {
        WinningFragment fragment = new WinningFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TEAM_NAME, teamName);
        bundle.putInt(EXTRA_TEAM_SCORE, teamScore);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_winning, container, false );
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        mTeamName = getArguments().getString(EXTRA_TEAM_NAME);
        mTeamScore = getArguments().getInt(EXTRA_TEAM_SCORE);

        mWinnerTextView.setText(mTeamName + " : " + mTeamScore);
    }

    private void initViews(View view) {
        mWinnerTextView = (TextView) view.findViewById(R.id.tv_winner);
    }
}
