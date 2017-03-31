package com.codephillip.app.hymnbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codephillip.app.hymnbook.models.HymnDatabase;

/**
 * Created by codephillip on 31/03/17.
 */

public class SongFragment extends Fragment {

    private static final String TAG = SongFragment.class.getSimpleName();
    private TextView titleView;
    private TextView contentView;

    public SongFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_song, container, false);
        titleView = (TextView) rootView.findViewById(R.id.title);
        contentView = (TextView) rootView.findViewById(R.id.content);

        HymnDatabase.getInstance();
        titleView.setText(HymnDatabase.hymns.getHymnArrayList().get(0).getTitle());
        contentView.setText(HymnDatabase.hymns.getHymnArrayList().get(0).getContent());
        return rootView;
    }

}