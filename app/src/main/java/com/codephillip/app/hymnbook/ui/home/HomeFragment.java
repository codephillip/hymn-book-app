package com.codephillip.app.hymnbook.ui.home;

import static com.codephillip.app.hymnbook.utilities.Utils.category;
import static com.codephillip.app.hymnbook.utilities.Utils.cursor;
import static com.codephillip.app.hymnbook.utilities.Utils.findLargestNumber;
import static com.codephillip.app.hymnbook.utilities.Utils.isFromCategoryFragment;
import static com.codephillip.app.hymnbook.utilities.Utils.showFavoriteScreen;
import static com.codephillip.app.hymnbook.utilities.Utils.songType;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.codephillip.app.hymnbook.R;
import com.codephillip.app.hymnbook.SongActivity;
import com.codephillip.app.hymnbook.adapters.HymnsAdapter;
import com.codephillip.app.hymnbook.adapters.RecentAdapter;
import com.codephillip.app.hymnbook.databinding.FragmentHomeBinding;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.recentRecycler.setLayoutManager(new CardLayoutManager(getContext(), 1,
                GridLayoutManager.HORIZONTAL, false, 8));
        binding.hymnsRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // todo implement recent table
        HymntableCursor recentCursor = queryHymnTable();
        binding.recentRecycler.setAdapter(new RecentAdapter(getActivity(), recentCursor));
        cursor = queryHymnTable();
        HymnsAdapter hymnsAdapter = new HymnsAdapter(getActivity(), cursor);
        binding.hymnsRecycler.setAdapter(hymnsAdapter);

        binding.searchfield.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.searchfield.setHint("");
                    binding.searchLayout.setEndIconDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cancel, null));
                }
            }
        });

        binding.searchLayout.setEndIconOnClickListener(view -> {
            if (binding.searchfield.isFocused()) {
                hymnsAdapter.swapCursor(queryHymnTable());
                unfocusInputField();
            } else {
                focusOnInputField();
            }
        });


        binding.searchfield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called to notify you that the text is about to be changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called to notify you that the text has been changed
                // You can react to the text change here
                String inputText = s.toString();
                int wordCount = s.length();
                if (wordCount > 0) {
                    hymnsAdapter.filter(inputText);
                } else {
                    hymnsAdapter.swapCursor(queryHymnTable());
                }
                // Do something with inputText
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called when the text has been changed
            }
        });
        setHymnOfTheDay();

        return root;
    }

    private void setHymnOfTheDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateString = sdf.format(Calendar.getInstance().getTime());

        long seed = Long.parseLong(dateString);
        Random random = new Random(seed);

        int randomNumber = random.nextInt(cursor.getCount()) + 1;
        cursor.moveToPosition(randomNumber);
        int verses = findLargestNumber(cursor.getContent());
        binding.hymnTitle.setText(String.format(Locale.US, "Hymn %d . %d verses", cursor.getNumber(), verses));

        binding.openNow.setOnClickListener(view -> {
            Utils.getInstance();
            Utils.position = randomNumber;
            Utils.isSongActivityActive = false;
            startActivity(new Intent(getActivity(), SongActivity.class));
        });

    }

    private void unfocusInputField() {
        binding.searchLayout.setEndIconDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_search, null));
        binding.searchfield.setHint(getString(R.string.enter_text));
        binding.searchfield.clearFocus();
        binding.searchfield.setText("");
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.searchfield.getWindowToken(), 0);
        }
    }

    private void focusOnInputField() {
        binding.searchfield.setHint("");
        binding.searchLayout.setEndIconDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cancel, null));
        binding.searchfield.requestFocus();

        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(binding.searchfield, InputMethodManager.SHOW_IMPLICIT);
        }
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