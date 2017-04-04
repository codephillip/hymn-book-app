package com.codephillip.app.hymnbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codephillip.app.hymnbook.models.HymnDatabase;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableContentValues;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.Utils;

import static com.codephillip.app.hymnbook.utilities.Utils.showFavoriteScreen;

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
    private HymntableCursor cursor;

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

        Log.d(TAG, "STARTED FRAGMENT");

        titleView = (TextView) rootView.findViewById(R.id.title);
        contentView = (TextView) rootView.findViewById(R.id.content);
        navigationView = (TextView) rootView.findViewById(R.id.navigation);
        likeButton = (ImageButton) rootView.findViewById(R.id.like);

        HymnDatabase.getInstance();
        Utils.getInstance();

        Log.d(TAG, "onCreateView: started");
        position = getArguments().getInt(SONG_NUMBER);
        Log.d(TAG, "onCreateView: ###" + position);

        cursor =  queryHymnTable(showFavoriteScreen);
        //you can only get a value once very time you move a cursor position
        cursor.moveToPosition(position);
        final boolean like = cursor.getLike();
        attachDataToViews(like);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(position);
                boolean liked = cursor.getLike();
                changeLikeImageButton(!liked);
                changeLikePreference(!liked, cursor.getTitle());
            }
        });
        return rootView;
    }

    private void changeLikeImageButton(Boolean like) {
        if (like)
            likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_16dp));
        else
            likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_16dp));
    }


    private void attachDataToViews(boolean isLiked) {
        try {
            titleView.setText(cursor.getNumber() + ". " + cursor.getTitle());
            contentView.setText(cursor.getContent());
            navigationView.setText((position + 1) + "/" + cursor.getCount());
            changeLikeImageButton(isLiked);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeLikePreference(boolean liked, String title) {
        Log.d(TAG, "changeLikePreference: " + liked);
        HymntableContentValues values = new HymntableContentValues();
        values.putLike(liked);
        values.update(getContext().getContentResolver(), new HymntableSelection().titleLike(title));
        cursor = queryHymnTable(showFavoriteScreen);
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