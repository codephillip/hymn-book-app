package com.codephillip.app.hymnbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codephillip.app.hymnbook.adapters.SongGridAdapter;
import com.codephillip.app.hymnbook.models.Hymn;
import com.codephillip.app.hymnbook.models.HymnDatabase;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.Utils;

import java.util.ArrayList;

/**
 * Created by codephillip on 31/03/17.
 */

public class AllSongsGridFragment extends Fragment implements SongGridAdapter.ItemClickListener {

    private static final String TAG = AllSongsGridFragment.class.getSimpleName();
    SongGridAdapter adapter;

    public AllSongsGridFragment() {
    }

    public static AllSongsGridFragment newInstance(boolean isFavorite) {
        Log.d(TAG, "newInstance: " + isFavorite);
        AllSongsGridFragment fragment = new AllSongsGridFragment();
        Bundle args = new Bundle();
        args.putBoolean(Utils.IS_FAVORITE, isFavorite);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_all_songs_grid, container, false);

        boolean showFavoriteScreen = getArguments().getBoolean(Utils.IS_FAVORITE, false);
        updateSingletonHymns(showFavoriteScreen);

        // data to populate the RecyclerView with
        String[] data = new String[HymnDatabase.hymns.getHymnArrayList().size()];
        int counter = 0;
        for (Hymn hymn : HymnDatabase.hymns.getHymnArrayList()) {
            data[counter++] = String.valueOf(hymn.getNumber());
        }
        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rvNumbers);
        int numberOfColumns = 5;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        adapter = new SongGridAdapter(getContext(), data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        Utils.getInstance();
        return rootView;
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
        Utils.position = Integer.parseInt(adapter.getItem(position - 1));
        Utils.isSongActivityActive = false;
        startActivity(new Intent(getContext(), SongActivity.class));
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