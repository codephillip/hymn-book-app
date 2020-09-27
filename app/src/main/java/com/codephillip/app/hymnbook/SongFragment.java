package com.codephillip.app.hymnbook;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codephillip.app.hymnbook.provider.hymntable.HymntableContentValues;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.Utils;

import java.util.Locale;

import static com.codephillip.app.hymnbook.utilities.Utils.cursor;
import static com.codephillip.app.hymnbook.utilities.Utils.showFavoriteScreen;
import static com.codephillip.app.hymnbook.utilities.Utils.typeface;

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

        setHasOptionsMenu(true);

        titleView = (TextView) rootView.findViewById(R.id.title);
        contentView = (TextView) rootView.findViewById(R.id.content);
        navigationView = (TextView) rootView.findViewById(R.id.navigation);
        likeButton = (ImageButton) rootView.findViewById(R.id.like);

        Utils.getInstance();

        Log.d(TAG, "onCreateView: started");
        position = getArguments().getInt(SONG_NUMBER);
        Log.d(TAG, "onCreateView: ###" + position);

        //you can only get a value once very time you move a cursor position
        cursor.moveToPosition(position);
        attachDataToViews();

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

    private void attachDataToViews() {
        try {
            titleView.setTypeface(typeface);
            contentView.setTypeface(typeface);
            navigationView.setTypeface(typeface);
            contentView.setTextSize(getFontSize());
            titleView.setText(String.format(Locale.US, "%d. %s", cursor.getNumber(), cursor.getTitle()));
            contentView.setText(cursor.getContent());
            navigationView.setText(String.format(Locale.US, "%d/%d", position + 1, cursor.getCount()));
            changeLikeImageButton(cursor.getLike());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeLikeImageButton(Boolean like) {
        int image = like ? R.drawable.ic_star_black_16dp : R.drawable.ic_star_border_black_16dp;
        likeButton.setImageDrawable(getResources().getDrawable(image));
        likeButton.setColorFilter(getResources().getColor((R.color.colorAccent)));
    }

    private void changeLikePreference(boolean liked, String title) {
        Log.d(TAG, "changeLikePreference: " + liked);
        HymntableContentValues values = new HymntableContentValues();
        values.putLike(liked);
        values.update(getContext().getContentResolver(), new HymntableSelection().titleLike(title));
        cursor = queryHymnTable(showFavoriteScreen);
    }

    private HymntableCursor queryHymnTable(boolean showFavoriteScreen) {
        return showFavoriteScreen ? new HymntableSelection().like(true).query(getContext().getContentResolver()) : new HymntableSelection().query(getContext().getContentResolver());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.song_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.zoom_in) {
            if (getFontSize() >= 25.0f)
                return true;
            saveFontSize(getFontSize() + 1);
            contentView.setTextSize(getFontSize());
            return true;
        } else  if (id == R.id.zoom_out) {
            if (getFontSize() <= 10.0f)
                return true;
            saveFontSize(getFontSize() - 1);
            contentView.setTextSize(getFontSize());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveFontSize(float fontSize) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(Utils.FONT_SIZE, fontSize);
        editor.apply();
    }

    private float getFontSize() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        //default size 42.0f
        return prefs.getFloat(Utils.FONT_SIZE, 17.0f);
    }
}