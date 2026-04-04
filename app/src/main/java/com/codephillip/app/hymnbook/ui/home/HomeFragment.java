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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
import com.codephillip.app.hymnbook.provider.categorytable.CategorytableCursor;
import com.codephillip.app.hymnbook.provider.categorytable.CategorytableSelection;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private HymnsAdapter hymnsAdapter;
    private boolean isHeaderVisible = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.hymnsRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        cursor = queryHymnTable();
        hymnsAdapter = new HymnsAdapter(getActivity(), cursor, false);
        binding.hymnsRecycler.setAdapter(hymnsAdapter);

        setupCategoryDropdown();

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

    private void setupCategoryDropdown() {
        CategorytableCursor categoryCursor = new CategorytableSelection().nameEndsWith("- HS").query(getContext().getContentResolver());
        List<String> categories = new ArrayList<>();
        categories.add(getString(R.string.title_home).toUpperCase());
        if (categoryCursor.moveToFirst()) {
            do {
                String name = categoryCursor.getName();
                if (name != null) {
                    name = name.replace("- HS", "").replace("- ORIGINAL", "").trim();
                }
                categories.add(name);
            } while (categoryCursor.moveToNext());
        }
        categoryCursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.category_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(adapter);

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                if (selectedCategory.equals(getString(R.string.title_home).toUpperCase())) {
                    isFromCategoryFragment = false;
                } else {
                    isFromCategoryFragment = true;
                    category = selectedCategory;
                }
                hymnsAdapter.swapCursor(queryHymnTable());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hymnsAdapter != null) {
            hymnsAdapter.swapCursor(queryHymnTable());
        }
    }

    private void setHymnOfTheDay() {
        // Implementation of Seasonal Hymn of the Day, strictly Home Songs (HS)
        HymntableCursor pickCursor = Utils.getSeasonalHymnCursor(getContext(), null, "HS");
        if (pickCursor == null || pickCursor.getCount() == 0) {
            Log.d(TAG, "setHymnOfTheDay: Seasonal cursor empty, falling back to all Home Songs");
            pickCursor = new HymntableSelection().categoryEndsWith("HS").query(getContext().getContentResolver());
        }

        if (pickCursor == null || pickCursor.getCount() == 0) return;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        String dateString = sdf.format(Calendar.getInstance().getTime());

        long seed = Long.parseLong(dateString);
        Random random = new Random(seed);

        int randomPosition = random.nextInt(pickCursor.getCount());
        pickCursor.moveToPosition(randomPosition);

        int verses = findLargestNumber(pickCursor.getContent());
        String navigationText;
        if (verses > 0)
            navigationText = String.format(Locale.US, "Hymn %d • %d verses", pickCursor.getNumber(), verses);
        else
            navigationText = String.format(Locale.US, "Hymn %d", pickCursor.getNumber());
        binding.hymnTitle.setText(navigationText);

        final long selectedId = pickCursor.getId();

        binding.openNow.setOnClickListener(view -> {
            // Ensure the cursor used in SongActivity is all Home Songs
            HymntableCursor allHomeSongs = new HymntableSelection().categoryEndsWith("HS").orderByNumber().query(getContext().getContentResolver());
            int mainPosition = 0;
            if (allHomeSongs.moveToFirst()) {
                do {
                    if (allHomeSongs.getId() == selectedId) {
                        mainPosition = allHomeSongs.getPosition();
                        break;
                    }
                } while (allHomeSongs.moveToNext());
            }
            Utils.getInstance();
            Utils.cursor = allHomeSongs;
            Utils.position = mainPosition;
            startActivity(new Intent(getActivity(), SongActivity.class));
        });

        // Close seasonal cursor if it's separate from the main one
        if (pickCursor != cursor) {
            pickCursor.close();
        }
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
        binding.categorySpinner.setVisibility(View.VISIBLE);
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
        binding.categorySpinner.setVisibility(View.GONE);
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
