package com.codephillip.app.hymnbook.ui.home;

import static com.codephillip.app.hymnbook.utilities.Utils.category;
import static com.codephillip.app.hymnbook.utilities.Utils.cursor;
import static com.codephillip.app.hymnbook.utilities.Utils.isFromCategoryFragment;
import static com.codephillip.app.hymnbook.utilities.Utils.showFavoriteScreen;
import static com.codephillip.app.hymnbook.utilities.Utils.songType;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.codephillip.app.hymnbook.R;
import com.codephillip.app.hymnbook.adapters.HymnsAdapter;
import com.codephillip.app.hymnbook.adapters.RecentAdapter;
import com.codephillip.app.hymnbook.databinding.FragmentHomeBinding;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.Utils;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.recentRecycler.setLayoutManager(new CardLayoutManager(getContext(), 1,
                GridLayoutManager.HORIZONTAL, false, 8));
        binding.hymnsRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        binding.recentRecycler.setAdapter(new RecentAdapter(getActivity(), "productEntities"));
        cursor = queryHymnTable();
        binding.hymnsRecycler.setAdapter(new HymnsAdapter(getActivity(), cursor));

        binding.searchfield.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.searchfield.setHint("");
                } else if (binding.searchfield.getText().toString().isEmpty()) {
                    binding.searchfield.setHint(getString(R.string.enter_text));
                }
            }
        });

        return root;
    }

    private HymntableCursor queryHymnTable() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}