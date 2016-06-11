package com.avery.pubquiz.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avery.pubquiz.R;


/**
 * Created by geoff on 6/10/16.
 */
public class LoadingFragment extends Fragment{

    private static final String TAG = LoadingFragment.class.getSimpleName();





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_loading, container, false);

        Button nextButton = (Button) v.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new HostSearch(), "contentFrameTag").commit();
            }
        });

        return v;
    }



/*
    private void insertSectionFragment() {

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                android.support.v4.app.Fragment fragment = mActivity.getCurrentFragment();

                if( fragment == null ) {
                    mActivity.selectItem(0);
                }
                else {
                    if( fragment instanceof StoreListFragment )
                        mActivity.selectItem(0);
                    else if ( fragment instanceof MapFragment )
                        mActivity.selectItem(1);
                    else
                        mActivity.selectItem(0);
                }

                mActivity.getSupportActionBar().show();
            }
        });
    }
*/

}
