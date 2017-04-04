package com.codephillip.app.hymnbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codephillip.app.hymnbook.models.Hymn;
import com.codephillip.app.hymnbook.models.HymnDatabase;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableContentValues;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;

import java.util.ArrayList;

/**
 * Created by codephillip on 31/03/17.
 */

public class SongFragment extends Fragment {

    private static final String TAG = SongFragment.class.getSimpleName();
    private static final String SONG_NUMBER = "song_number";
    private TextView titleView;
    private TextView contentView;
    private TextView navigationView;
    private ImageButton likeButton;
    private int position;
    private Hymn hymn;

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
        likeButton = (ImageButton) rootView.findViewById(R.id.like);

        HymnDatabase.getInstance();
        hymn = getHymnFromList();

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + hymn.isLiked());
                changeLikePreference(!hymn.isLiked(), hymn.getTitle());
                changeLikeImageButton();
            }
        });
        attachDataToViews(hymn);
        return rootView;
    }

    private void changeLikeImageButton() {
        if (hymn.isLiked())
            likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_16dp));
        else
            likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_16dp));
    }

    private Hymn getHymnFromList() {
        position = getArguments().getInt(SONG_NUMBER);
        return HymnDatabase.hymns.getHymnArrayList().get(position);
    }

    private void attachDataToViews(Hymn hymn) {
        try {
            titleView.setText(hymn.getNumber() + ". " + hymn.getTitle());
            contentView.setText(hymn.getContent());
            navigationView.setText((position + 1) + "/" + HymnDatabase.hymns.getHymnArrayList().size());
            changeLikeImageButton();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeLikePreference(boolean liked, String title) {
        Log.d(TAG, "changeLikePreference: " + liked);
        HymntableContentValues values = new HymntableContentValues();
        values.putLike(liked);
        values.update(getContext().getContentResolver(), new HymntableSelection().titleLike(title));
        updateSingletonHymns(false);
        hymn = getHymnFromList();
    }

    private void updateSingletonHymns(boolean showFavoriteScreen) {
        ArrayList<Hymn> hymnArrayList = new ArrayList<>();
        HymntableCursor cursor = queryHymnTable(showFavoriteScreen);
        if (cursor.moveToFirst()) {
            do {
                hymnArrayList.add(new Hymn(cursor.getNumber(), cursor.getTitle(), cursor.getContent(), cursor.getCategory(), cursor.getId(), cursor.getLike()));
            } while (cursor.moveToNext());
        }
        HymnDatabase.hymns.setHymnArrayList(hymnArrayList);
        Log.d(TAG, "onCreateView: ###" + HymnDatabase.hymns.getHymnArrayList().size());
    }

    private HymntableCursor queryHymnTable(boolean showFavoriteScreen) {
        HymntableCursor cursor;
        if (showFavoriteScreen) {
            cursor = new HymntableSelection().like(true).query(getContext().getContentResolver());
        } else {
            cursor = new HymntableSelection().query(getContext().getContentResolver());
        }
        return cursor;
    }
}