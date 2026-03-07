package com.codephillip.app.hymnbook.adapters;

import androidx.fragment.app.FragmentActivity;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;


public class RecentAdapter extends HymnsAdapter {

    public RecentAdapter(FragmentActivity activity, HymntableCursor cursor) {
        super(activity, cursor, true);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}