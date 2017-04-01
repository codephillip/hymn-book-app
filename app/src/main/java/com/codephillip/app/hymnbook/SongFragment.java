package com.codephillip.app.hymnbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codephillip.app.hymnbook.models.Hymn;
import com.codephillip.app.hymnbook.models.HymnDatabase;

/**
 * Created by codephillip on 31/03/17.
 */

public class SongFragment extends Fragment {

    private static final String TAG = SongFragment.class.getSimpleName();
    private static final String SONG_NUMBER = "song_number";
    private TextView titleView;
    private TextView contentView;
    private TextView navigationView;

    public SongFragment() {
    }

    public static SongFragment newInstance(int position) {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        args.putInt(SONG_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_song, container, false);
        titleView = (TextView) rootView.findViewById(R.id.title);
        contentView = (TextView) rootView.findViewById(R.id.content);
        navigationView = (TextView) rootView.findViewById(R.id.navigation);

        HymnDatabase.getInstance();
        int position = getArguments().getInt(SONG_NUMBER);
        Log.d(TAG, "onCreateView: position#" + position);
        Hymn hymn = HymnDatabase.hymns.getHymnArrayList().get(position);
        try {
            titleView.setText(hymn.getNumber() + ". " +hymn.getTitle());
            contentView.setText(hymn.getContent());
            navigationView.setText(hymn.getNumber() + " / " + HymnDatabase.hymns.getHymnArrayList().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

}