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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.codephillip.app.hymnbook.R;
import com.codephillip.app.hymnbook.SettingsActivity;
import com.codephillip.app.hymnbook.SongActivity;
import com.codephillip.app.hymnbook.adapters.HymnsAdapter;
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
    private HymnsAdapter hymnsAdapter;
    private boolean isHeaderVisible = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.hymnsRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        cursor = queryHymnTable();
        hymnsAdapter = new HymnsAdapter(getActivity(), cursor);
        binding.hymnsRecycler.setAdapter(hymnsAdapter);

        binding.searchfield.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    focusOnInputField();
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

        binding.settings.setOnClickListener(view -> startActivity(new Intent(getActivity(), SettingsActivity.class)));

        binding.hymnsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager != null ? layoutManager.findFirstVisibleItemPosition() : 0;

                if (dy > 0 && isHeaderVisible) {
                    isHeaderVisible = false;
                    TransitionSet transitionSet = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new ChangeBounds())
                            .setDuration(300);
                    TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot(), transitionSet);
                    binding.collapsibleHeader.setVisibility(View.GONE);
                } else if (dy < 0 && firstVisibleItemPosition <= 4 && !isHeaderVisible) {
                    isHeaderVisible = true;
                    TransitionSet transitionSet = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new ChangeBounds())
                            .setDuration(300);
                    TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot(), transitionSet);
                    binding.collapsibleHeader.setVisibility(View.VISIBLE);
                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hymnsAdapter != null) {
            hymnsAdapter.swapCursor(queryHymnTable());
        }
    }

    private void setHymnOfTheDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateString = sdf.format(Calendar.getInstance().getTime());

        long seed = Long.parseLong(dateString);
        Random random = new Random(seed);

        int randomNumber = random.nextInt(cursor.getCount()) + 1;
        cursor.moveToPosition(randomNumber);
        int verses = findLargestNumber(cursor.getContent());
        String navigationText;
        if (verses > 0)
            navigationText = String.format(Locale.US, "Hymn %d • %d verses", cursor.getNumber(), verses);
        else
            navigationText = String.format(Locale.US, "Hymn %d", cursor.getNumber());
        binding.hymnTitle.setText(navigationText);

        binding.openNow.setOnClickListener(view -> {
            Utils.getInstance();
            Utils.position = randomNumber;
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
        binding.hymnDay.setVisibility(View.VISIBLE);
        binding.settings.setVisibility(View.VISIBLE);
        binding.hymnTitle.setVisibility(View.VISIBLE);
        binding.openNow.setVisibility(View.VISIBLE);
    }

    private void focusOnInputField() {
        binding.searchfield.setHint("");
        binding.searchLayout.setEndIconDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cancel, null));
        binding.searchfield.requestFocus();

        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(binding.searchfield, InputMethodManager.SHOW_IMPLICIT);
        }
        binding.hymnDay.setVisibility(View.GONE);
        binding.settings.setVisibility(View.GONE);
        binding.hymnTitle.setVisibility(View.GONE);
        binding.openNow.setVisibility(View.GONE);
    }

    private HymntableCursor queryHymnTable() {
        songType = Utils.HOME_SONGS;
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
