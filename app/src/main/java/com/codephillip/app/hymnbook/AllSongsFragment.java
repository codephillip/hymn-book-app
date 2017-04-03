package com.codephillip.app.hymnbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
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

import com.codephillip.app.hymnbook.adapters.SongListAdapter;
import com.codephillip.app.hymnbook.models.Hymn;
import com.codephillip.app.hymnbook.models.HymnDatabase;
import com.codephillip.app.hymnbook.provider.favoritetable.FavoritetableColumns;
import com.codephillip.app.hymnbook.provider.favoritetable.FavoritetableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableColumns;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.utilities.Utils;

import java.util.ArrayList;

/**
 * Created by codephillip on 31/03/17.
 */

public class AllSongsFragment extends Fragment {

    private static final String TAG = AllSongsFragment.class.getSimpleName();
    SongListAdapter adapter;
    RecyclerView recyclerView;


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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Hymn> hymnArrayList = new ArrayList<>();
        if (getArguments().getBoolean(Utils.IS_FAVORITE, false)) {
            CursorLoader cursorLoader = new CursorLoader(getContext(), FavoritetableColumns.CONTENT_URI, null, null, null, null);
            FavoritetableCursor cursor = new FavoritetableCursor(cursorLoader.loadInBackground());
            if (cursor.moveToFirst()) {
                do {
                    hymnArrayList.add(new Hymn(cursor.getNumber(), cursor.getTitle(), cursor.getContent(), cursor.getCategory()));
                } while (cursor.moveToNext());
            }
        } else {
            CursorLoader cursorLoader = new CursorLoader(getContext(), HymntableColumns.CONTENT_URI, null, null, null, null);
            HymntableCursor cursor = new HymntableCursor(cursorLoader.loadInBackground());
            if (cursor.moveToFirst()) {
                do {
                    hymnArrayList.add(new Hymn(cursor.getNumber(), cursor.getTitle(), cursor.getContent(), cursor.getCategory()));
                } while (cursor.moveToNext());
            }

        }
        HymnDatabase.hymns.setHymnArrayList(hymnArrayList);
        Log.d(TAG, "onCreateView: ###" + HymnDatabase.hymns.getHymnArrayList().size());
        adapter = new SongListAdapter(getContext(), HymnDatabase.hymns.getHymnArrayList());
        recyclerView.setAdapter(adapter);
        return rootView;
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
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: ");
                adapter.filter(newText);
                return true;
            }
        });
    }
}