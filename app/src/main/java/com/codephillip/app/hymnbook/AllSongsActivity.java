package com.codephillip.app.hymnbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.codephillip.app.hymnbook.utilities.Utils;

import static com.codephillip.app.hymnbook.utilities.Utils.screenNames;

public class AllSongsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            String category = getIntent().getStringExtra(Utils.CATEGORY);
            Fragment fragment = AllSongsFragment.newInstance(category);
            getSupportActionBar().setTitle(screenNames[0]);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
