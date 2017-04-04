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

import com.codephillip.app.hymnbook.adapters.SongGridAdapter;
import com.codephillip.app.hymnbook.adapters.SongListAdapter;
import com.codephillip.app.hymnbook.models.Hymn;
import com.codephillip.app.hymnbook.models.HymnDatabase;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.Utils;

import java.util.ArrayList;

/**
 * Created by codephillip on 31/03/17.
 */

public class AllSongsFragment extends Fragment {

    private static final String TAG = AllSongsFragment.class.getSimpleName();
    private SongListAdapter listAdapter;
    private SongGridAdapter gridAdapter;
    private RecyclerView recyclerView;

    public AllSongsFragment() {
    }

    public static AllSongsFragment newInstance(boolean isFavorite) {
        Log.d(TAG, "newInstance: " + isFavorite);
        AllSongsFragment fragment = new AllSongsFragment();
        Bundle args = new Bundle();
        args.putBoolean(Utils.IS_FAVORITE, isFavorite);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_songs, container, false);

        boolean showFavoriteScreen = false;
        try {
            showFavoriteScreen = getArguments().getBoolean(Utils.IS_FAVORITE, false);
        } catch (Exception e) {
            e.printStackTrace();
            showFavoriteScreen = false;
        }
        updateSingletonHymns(showFavoriteScreen);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        if (hasChangedView()) {
            attachListAdapter();
        } else {
            attachGridAdapter();
        }

        return rootView;
    }

    private void attachListAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new SongListAdapter(getContext(), HymnDatabase.hymns.getHymnArrayList());
        recyclerView.setAdapter(listAdapter);
    }

    private void attachGridAdapter() {
        int numberOfColumns = 5;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        gridAdapter = new SongGridAdapter(getContext(), generateGridViewData());
        recyclerView.setAdapter(gridAdapter);
    }

    private String[] generateGridViewData() {
        String[] data = new String[HymnDatabase.hymns.getHymnArrayList().size()];
        int counter = 0;
        for (Hymn hymn : HymnDatabase.hymns.getHymnArrayList()) {
            data[counter++] = String.valueOf(hymn.getNumber());
        }
        return data;
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

    private boolean hasChangedView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.getBoolean(Utils.CHANGE_VIEW, false);
    }

    //todo work on search
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: ");
//        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
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
    }
}