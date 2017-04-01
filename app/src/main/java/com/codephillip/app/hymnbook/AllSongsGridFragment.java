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
import com.codephillip.app.hymnbook.utilities.Utils;

/**
 * Created by codephillip on 31/03/17.
 */

public class AllSongsGridFragment extends Fragment implements SongGridAdapter.ItemClickListener {

    private static final String TAG = AllSongsGridFragment.class.getSimpleName();
    SongGridAdapter adapter;

    public AllSongsGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_all_songs_grid, container, false);
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
        Utils.position = Integer.parseInt(adapter.getItem(position));
        Utils.isSongActivityActive = false;
        startActivity(new Intent(getContext(), SongActivity.class));
    }
}