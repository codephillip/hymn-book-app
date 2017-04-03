package com.codephillip.app.hymnbook;

import android.net.Uri;
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
import com.codephillip.app.hymnbook.provider.favoritetable.FavoritetableContentValues;

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
        int position = getArguments().getInt(SONG_NUMBER);
        Log.d(TAG, "onCreateView: position#" + position);
        final Hymn hymn = HymnDatabase.hymns.getHymnArrayList().get(position);


        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeInFavouriteTable(hymn.getNumber(), hymn.getTitle(), hymn.getContent(), hymn.getCategory(), true);
            }
        });
        attachDataToViews(hymn);
        return rootView;
    }

    private void attachDataToViews(Hymn hymn) {
        try {
            titleView.setText(hymn.getNumber() + ". " +hymn.getTitle());
            contentView.setText(hymn.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeInFavouriteTable(int number, String title, String content, String category, boolean like) {
        FavoritetableContentValues hymn = new FavoritetableContentValues();
        hymn.putNumber(number);
        hymn.putTitle(title);
        hymn.putContent(content);
        hymn.putCategory(category);
        hymn.putLike(like);
        Uri uri = hymn.insert(getContext().getContentResolver());
        Log.d("INSERT: ", "inserting" + uri.toString());
    }
}