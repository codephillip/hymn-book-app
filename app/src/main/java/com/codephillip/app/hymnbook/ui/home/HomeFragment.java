package com.codephillip.app.hymnbook.ui.home;

import android.os.Bundle;
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
        binding.hymnsRecycler.setAdapter(new HymnsAdapter(getActivity(), "orderEntities"));

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}