package com.avery.pubquiz.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avery.pubquiz.R;


/**
 * Created by geoff on 6/10/16.
 */
public class LoadingFragment extends Fragment {

    private static final String TAG = LoadingFragment.class.getSimpleName();

    private ViewGroup mLoadingContainer;
    private ImageView mAveryTab;
    private ProgressBar mProgressBar;
    private TextView mStatusText;

    private ViewGroup mTeamNameContainer;
    private TextView mTeamNameLabelText;
    private EditText mTeamNameEditText;
    private Button mTeamNameSubmitButton;


    private LoadingFragmentActions mLoadingFragmentActions;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_loading, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLoadingContainer = (ViewGroup) view.findViewById(R.id.loading_container);
        mAveryTab = (ImageView) view.findViewById(R.id.avery_tab);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mStatusText = (TextView) view.findViewById(R.id.status_text);

        mTeamNameContainer = (ViewGroup) view.findViewById(R.id.team_name_container);
        mTeamNameLabelText = (TextView) view.findViewById(R.id.team_name_label_text);
        mTeamNameEditText = (EditText) view.findViewById(R.id.team_name_edit_text);
        mTeamNameSubmitButton = (Button) view.findViewById(R.id.team_name_submit_button);

        mTeamNameSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teamName = mTeamNameEditText.getText().toString();
                if(teamName.length() <= 0) {
                    return;
                }

                if(mLoadingFragmentActions != null) {
                    mTeamNameSubmitButton.setEnabled(false);
                    mTeamNameEditText.setEnabled(false);
                    mLoadingFragmentActions.onSetTeamName(teamName);
                }
            }
        });
    }


    public void setConnected() {

        final Animation fadeOut = new AnimationUtils().loadAnimation(getContext(), R.anim.abc_fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation) { mLoadingContainer.setVisibility(View.GONE); }
        });
        mLoadingContainer.startAnimation(fadeOut);

        mTeamNameContainer.setVisibility(View.VISIBLE);
        mTeamNameContainer.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_in));
    }


    public void setWaitingForQuestion() {
        mStatusText.setText("Waiting for game to start....");
        mLoadingContainer.setVisibility(View.VISIBLE);
        mTeamNameContainer.setVisibility(View.GONE);
    }

    public void setTeamNameNotAvailable() {
        mTeamNameLabelText.setText("Name already in use. Choose another.");
        mTeamNameSubmitButton.setEnabled(true);
        mTeamNameEditText.setEnabled(true);
    }


    public void setLoadingFragmentActions(LoadingFragmentActions actions) {
        mLoadingFragmentActions = actions;
    }


    public interface LoadingFragmentActions {
        void onSetTeamName(String teamName);
    }
}
