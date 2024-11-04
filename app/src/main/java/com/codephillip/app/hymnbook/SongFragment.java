package com.codephillip.app.hymnbook;

import static com.codephillip.app.hymnbook.utilities.Utils.category;
import static com.codephillip.app.hymnbook.utilities.Utils.cursor;
import static com.codephillip.app.hymnbook.utilities.Utils.isFromCategoryFragment;
import static com.codephillip.app.hymnbook.utilities.Utils.showFavoriteScreen;
import static com.codephillip.app.hymnbook.utilities.Utils.songType;
import static com.codephillip.app.hymnbook.utilities.Utils.typeface;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codephillip.app.hymnbook.provider.hymntable.HymntableContentValues;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.Utils;

import java.util.Locale;

/**
 * Created by codephillip on 31/03/17.
 */

public class SongFragment extends Fragment {

    private static final String TAG = SongFragment.class.getSimpleName();
    private static final String SONG_NUMBER = "song_number";
    private TextView titleView;
    private TextView contentView;
    private TextView songTypeView;
    private TextView textSizeView;
    private TextView navigationView;
    private ImageButton likeButton;
    private ImageView songTypeIcon;
    private ImageView textSizeIcon;
    private ImageView backButton;
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

        titleView = rootView.findViewById(R.id.title);
        contentView = rootView.findViewById(R.id.content);
        navigationView = rootView.findViewById(R.id.navigation);
        likeButton = rootView.findViewById(R.id.like);
        songTypeView = rootView.findViewById(R.id.song_type);
        textSizeView = rootView.findViewById(R.id.text_size);
        songTypeIcon = rootView.findViewById(R.id.song_type_icon);
        textSizeIcon = rootView.findViewById(R.id.text_size_icon);
        backButton = rootView.findViewById(R.id.backbutton);

        Utils.getInstance();
        position = getArguments().getInt(SONG_NUMBER);
        cursor.moveToPosition(position);
        attachDataToViews(cursor);

        likeButton.setOnClickListener(view -> {
            cursor.moveToPosition(position);
            changeLikeImageButton(!cursor.getLike());
            changeLikePreference(!cursor.getLike(), cursor.getTitle());
        });

        songTypeView.setOnClickListener(v -> showTypeDialog());
        songTypeIcon.setOnClickListener(v -> showTypeDialog());
        textSizeView.setOnClickListener(v -> showSizeDialog());
        textSizeIcon.setOnClickListener(v -> showSizeDialog());

        backButton.setOnClickListener(v -> getActivity().onBackPressed());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        textSizeView.setText(String.format(Locale.US, "%.0fpx", getFontSize()));
        contentView.setTextSize(getFontSize());
    }

    private void showTypeDialog() {
        final String[] options = {"Original", "Home"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Type");
        builder.setItems(options, (dialog, which) -> {
            String choice = options[which];
            songTypeView.setText(choice);
            Utils.songType = choice.equals("Original") ? Utils.ORIGINAL_SONGS : Utils.HOME_SONGS;
            int lastPosition = cursor.getPosition();
            HymntableCursor tempCursor = queryHymnTable();
            tempCursor.moveToPosition(lastPosition > 0 ? lastPosition - 1 : lastPosition);
            attachDataToViews(tempCursor);
        });
        builder.create().show();
    }

    private void showSizeDialog() {
        final String[] options = {"16px", "17px", "18px", "19px", "20px"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Font size");
        builder.setItems(options, (dialog, which) -> {
            final float fontsize = Float.parseFloat(options[which].replace("px", ""));
            saveFontSize(fontsize);
            contentView.setTextSize(fontsize);
            textSizeView.setText(String.format(Locale.US, "%.0fpx", fontsize));
        });
        builder.create().show();
    }

    private void attachDataToViews(HymntableCursor cursor) {
        try {
            titleView.setTypeface(typeface);
            textSizeView.setTypeface(typeface);
            navigationView.setTypeface(typeface);
            songTypeView.setTypeface(typeface);

            Typeface contentTypeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/" + "DMSans.ttf");
            contentView.setTypeface(contentTypeface);
            contentView.setTextSize(getFontSize());
            titleView.setText(cursor.getTitle());
            contentView.setText(cursor.getContent());
            int verses = Utils.findLargestNumber(cursor.getContent());
            navigationView.setText(String.format(Locale.US, "Hymn %d . %d verses", cursor.getNumber(), verses));
            changeLikeImageButton(cursor.getLike());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeLikeImageButton(Boolean like) {
        int image = like ? R.drawable.ic_star_black_36dp : R.drawable.ic_star_outline_black_36dp;
        likeButton.setImageDrawable(getResources().getDrawable(image));
    }

    private void changeLikePreference(boolean liked, String title) {
        Log.d(TAG, "changeLikePreference: " + liked);
        HymntableContentValues values = new HymntableContentValues();
        values.putLike(liked);
        values.update(getContext().getContentResolver(), new HymntableSelection().titleLike(title));
        cursor = queryHymnTable();
    }

    private HymntableCursor queryHymnTable() {
        Log.d(TAG, "queryHymnTable: show " + showFavoriteScreen);
        if (songType.equals(Utils.HOME_SONGS)) {
            if (showFavoriteScreen) {
                return new HymntableSelection().like(true).and().categoryEndsWith("HS").orderByNumber().query(getContext().getContentResolver());
            } else if (isFromCategoryFragment) {
                return new HymntableSelection().categoryContains(category).and().categoryEndsWith("HS").orderByNumber().query(getContext().getContentResolver());
            } else {
                return new HymntableSelection().categoryEndsWith("HS").orderByNumber().query(getContext().getContentResolver());
            }
        } else if (songType.equals(Utils.ORIGINAL_SONGS)) {
            if (showFavoriteScreen) {
                return new HymntableSelection().like(true).and().categoryEndsWith("ORIGINAL").orderByNumber().query(getContext().getContentResolver());
            } else if (isFromCategoryFragment) {
                return new HymntableSelection().categoryContains(category).and().categoryEndsWith("ORIGINAL").orderByNumber().query(getContext().getContentResolver());
            } else {
                return new HymntableSelection().categoryEndsWith("ORIGINAL").orderByNumber().query(getContext().getContentResolver());
            }
        } else {
            if (showFavoriteScreen) {
                return new HymntableSelection().like(true).query(getContext().getContentResolver());
            } else if (isFromCategoryFragment) {
                return new HymntableSelection().categoryContains(category).query(getContext().getContentResolver());
            } else {
                return new HymntableSelection().query(getContext().getContentResolver());
            }
        }
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