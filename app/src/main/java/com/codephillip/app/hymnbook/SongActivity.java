package com.codephillip.app.hymnbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.codephillip.app.hymnbook.utilities.Utils;

import static com.codephillip.app.hymnbook.utilities.Utils.cursor;

public class SongActivity extends BaseActivity {

    private static final String TAG = SongActivity.class.getSimpleName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Utils.getInstance();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: " + Utils.position + "#" + position);
            try {
                //move ViewPager to the item clicked on first_click
                if (!Utils.isSongActivityActive) {
                    mViewPager.setCurrentItem(Utils.position, true);
                    Utils.isSongActivityActive = true;
                    return SongFragment.newInstance(Utils.position);
                }
                //use default position after the first_click
                return SongFragment.newInstance(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new ArrayIndexOutOfBoundsException("Wrong array position");
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }
    }
}
