package com.codephillip.app.hymnbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codephillip.app.hymnbook.adapters.CategoryAdapter;
import com.codephillip.app.hymnbook.models.HymnDatabase;
import com.codephillip.app.hymnbook.provider.categorytable.CategorytableCursor;
import com.codephillip.app.hymnbook.provider.categorytable.CategorytableSelection;

import java.util.ArrayList;

/**
 * Created by codephillip on 31/03/17.
 */

public class CategoryFragment extends Fragment {

    private static final String TAG = CategoryFragment.class.getSimpleName();
    private CategoryAdapter adapter;
    private RecyclerView recyclerView;

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_songs, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        generateAdapterData();
        adapter = new CategoryAdapter(getContext(), queryCategoryTable());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private void generateAdapterData() {
        ArrayList<String> categorys = new ArrayList<>();
        categorys.add("EZA MAYINGIRA");
        categorys.add("EZO MUGAATI OGW’OBULAMU");
        categorys.add("EZO KWEBAZA");
        HymnDatabase.categorys = categorys;
    }

    private CategorytableCursor queryCategoryTable() {
        return new CategorytableSelection().query(getContext().getContentResolver());
    }
}