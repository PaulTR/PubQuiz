package com.avery.pubquiz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avery.networking.nearby.Client;
import com.avery.networking.nearby.NearbyHostCallback;
import com.avery.networking.nearby.NearbyManager;
import com.avery.networking.nearby.messages.BaseMessage;
import com.avery.pubquiz.R;
import com.avery.pubquiz.activities.MainActivity;

public class FormTeamsFragment extends Fragment {

    public static FormTeamsFragment getInstance() {
        FormTeamsFragment fragment = new FormTeamsFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_form_teams, container, false );
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).startAdvertising();
        /*
        NearbyManager manager = NearbyManager.getInstance();

        manager.setNearbyHostCallback(this);

        manager.initialize(getActivity());
        */
    }

}
