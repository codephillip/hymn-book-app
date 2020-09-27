package com.codephillip.app.hymnbook;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.codephillip.app.hymnbook.adapters.SongGridAdapter;
import com.codephillip.app.hymnbook.adapters.SongListAdapter;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.Utils;

import static com.codephillip.app.hymnbook.utilities.Utils.CATEGORY;
import static com.codephillip.app.hymnbook.utilities.Utils.category;
import static com.codephillip.app.hymnbook.utilities.Utils.cursor;
import static com.codephillip.app.hymnbook.utilities.Utils.isFromCategoryFragment;
import static com.codephillip.app.hymnbook.utilities.Utils.showFavoriteScreen;
import static com.codephillip.app.hymnbook.utilities.Utils.songType;

/**
 * Created by codephillip on 31/03/17.
 */

public class AllSongsFragment extends Fragment {

    private static final String TAG = AllSongsFragment.class.getSimpleName();
    private SongListAdapter listAdapter;
    private SongGridAdapter gridAdapter;
    private RecyclerView recyclerView;
    private LinearLayout errorLinearLayout;

    public AllSongsFragment() {
    }

    public static AllSongsFragment newInstance(boolean isFavorite, String songType) {
        Log.d(TAG, "newInstance: " + isFavorite);
        AllSongsFragment fragment = new AllSongsFragment();
        Bundle args = new Bundle();
        args.putBoolean(Utils.IS_FAVORITE, isFavorite);
        args.putString(Utils.SONG_TYPE, songType);
        fragment.setArguments(args);
        return fragment;
    }

    public static AllSongsFragment newInstance(boolean isFavorite) {
        Log.d(TAG, "newInstance: fav" + isFavorite);
        AllSongsFragment fragment = new AllSongsFragment();
        Bundle args = new Bundle();
        args.putBoolean(Utils.IS_FAVORITE, isFavorite);
        args.putString(Utils.SONG_TYPE, songType);
        fragment.setArguments(args);
        return fragment;
    }

    public static AllSongsFragment newInstance(String category, String songType) {
        Log.d(TAG, "newInstance: " + category);
        AllSongsFragment fragment = new AllSongsFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        args.putString(Utils.SONG_TYPE, songType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_songs, container, false);

        // We have a menu item to show in action bar.
        setHasOptionsMenu(true);

        Utils.getInstance();

        try {
            //Incase navigation is from CategoryFragment, NullPointerException is thrown
            showFavoriteScreen = getArguments().getBoolean(Utils.IS_FAVORITE, false);
            songType = getArguments().getString(Utils.SONG_TYPE, Utils.ORIGINAL_SONGS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //navigation from CategoryFragment
        try {
            Utils.category = getArguments().getString(CATEGORY);
            isFromCategoryFragment = !Utils.category.isEmpty();
            Log.d(TAG, "onCreateView: isFromCategoryFragment " + isFromCategoryFragment);
        } catch (Exception e) {
            e.printStackTrace();
            isFromCategoryFragment = false;
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        errorLinearLayout = (LinearLayout) rootView.findViewById(R.id.error_layout);
        cursor = queryHymnTable();
        showErrorMessage();

        if (hasChangedView()) {
            attachListAdapter(cursor);
        } else {
            attachGridAdapter();
        }
        return rootView;
    }

    private void showErrorMessage() {
        Log.d(TAG, "showErrorMessage: started");
        if (!cursor.moveToFirst()) {
            recyclerView.setVisibility(View.GONE);
            errorLinearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            errorLinearLayout.setVisibility(View.GONE);
        }
    }

    private void attachListAdapter(HymntableCursor cursor) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new SongListAdapter(getContext(), cursor);
        recyclerView.setAdapter(listAdapter);
    }

    private void attachGridAdapter() {
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        gridAdapter = new SongGridAdapter(getContext());
        recyclerView.setAdapter(gridAdapter);
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

    //true -> list, false -> grid
    private boolean hasChangedView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.getBoolean(Utils.CHANGE_VIEW, true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (hasChangedView()) {
            inflater.inflate(R.menu.allsong_toolbar, menu);
            final MenuItem searchItem = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d(TAG, "onQueryTextSubmit: ");
                    listAdapter.filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d(TAG, "onQueryTextChange: ");
                    listAdapter.filter(newText);
                    return true;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    listAdapter.swapCursor(queryHymnTable());
                    return false;
                }
            });
        }
    }
}