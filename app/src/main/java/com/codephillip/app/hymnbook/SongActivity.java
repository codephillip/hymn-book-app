package com.codephillip.app.hymnbook;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;

import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.utilities.Utils;

public class SongActivity extends BaseActivity {

    private static final String TAG = SongActivity.class.getSimpleName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public HymntableCursor songCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        // Capture the cursor at the moment of creation to prevent IllegalStateException
        // if Utils.cursor is changed globally later.
        songCursor = Utils.cursor;

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(Utils.position);
        Utils.getInstance();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: " + position);
            return SongFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return songCursor == null ? 0 : songCursor.getCount();
        }
    }
}
