package com.codephillip.app.hymnbook.ui.favourites;

import static com.codephillip.app.hymnbook.utilities.Utils.CATEGORY;
import static com.codephillip.app.hymnbook.utilities.Utils.cursor;
import static com.codephillip.app.hymnbook.utilities.Utils.isFromCategoryFragment;
import static com.codephillip.app.hymnbook.utilities.Utils.showFavoriteScreen;
import static com.codephillip.app.hymnbook.utilities.Utils.songType;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codephillip.app.hymnbook.R;
import com.codephillip.app.hymnbook.adapters.HymnsAdapter;
import com.codephillip.app.hymnbook.adapters.SongGridAdapter;
import com.codephillip.app.hymnbook.adapters.SongListAdapter;
import com.codephillip.app.hymnbook.databinding.FragmentFavouritesBinding;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.Utils;

public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;

    private static final String TAG = FavouritesFragment.class.getSimpleName();
    private SongListAdapter listAdapter;
    private SongGridAdapter gridAdapter;
    private RecyclerView recyclerView;
    private LinearLayout errorLinearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        setHasOptionsMenu(true);

        Utils.getInstance();
        songType = "";

        try {
            //Incase navigation is from CategoryFragment, NullPointerException is thrown
            showFavoriteScreen = getArguments().getBoolean(Utils.IS_FAVORITE);
            songType = getArguments().getString(Utils.SONG_TYPE, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //navigation from CategoryFragment
        try {
            Utils.category = getArguments().getString(CATEGORY);
            isFromCategoryFragment = !Utils.category.isEmpty();
//            Log.d(TAG, "onCreateView: isFromCategoryFragment " + isFromCategoryFragment);
        } catch (Exception e) {
            e.printStackTrace();
            isFromCategoryFragment = false;
        }

        recyclerView = binding.recycler;
        errorLinearLayout = binding.errorLayout;
        cursor = queryHymnTable();
        showErrorMessage();

        //todo add list,grid,cards settings
//        if (hasChangedView()) {
//            attachListAdapter(cursor);
//        } else {
//            attachGridAdapter();
//        }
        binding.recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        HymnsAdapter hymnsAdapter = new HymnsAdapter(getActivity(), cursor);
        binding.recycler.setAdapter(hymnsAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
        return new HymntableSelection().like(true).query(getContext().getContentResolver());
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