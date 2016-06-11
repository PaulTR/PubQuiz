package com.avery.pubquiz.activities;

import android.app.Activity;
import android.os.Bundle;

import com.avery.pubquiz.R;
import com.avery.pubquiz.fragments.FormTeamsFragment;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();

    }

    private void initFragment() {
        getFragmentManager().beginTransaction().replace(R.id.container, FormTeamsFragment.getInstance()).commit();
    }
}
