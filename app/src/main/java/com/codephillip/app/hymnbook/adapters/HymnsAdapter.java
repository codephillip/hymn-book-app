package com.codephillip.app.hymnbook.adapters;

import static com.codephillip.app.hymnbook.utilities.Utils.category;
import static com.codephillip.app.hymnbook.utilities.Utils.findLargestNumber;
import static com.codephillip.app.hymnbook.utilities.Utils.isFromCategoryFragment;
import static com.codephillip.app.hymnbook.utilities.Utils.showFavoriteScreen;
import static com.codephillip.app.hymnbook.utilities.Utils.songType;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.codephillip.app.hymnbook.R;
import com.codephillip.app.hymnbook.SongActivity;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableContentValues;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.Utils;

import java.util.Locale;


public class HymnsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = HymnsAdapter.class.getSimpleName();
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SEPARATOR = 1;

    private Activity activity;
    private HymntableCursor dataCursor;
    private String currentSearchText = "";
    private boolean showHeaders;
    
    private int firstHeaderPosition = -1;
    private int secondHeaderPosition = -1;
    private String firstHeaderTitle = "";
    private String secondHeaderTitle = "";


    public HymnsAdapter(FragmentActivity activity, HymntableCursor cursor, boolean showHeaders) {
        this.activity = activity;
        this.dataCursor = cursor;
        this.showHeaders = showHeaders;
        calculateHeaders();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView number;
        public TextView title;
        public ImageView like;
        public TextView numbVerses;
        public LinearLayout dotsContainer;
        public boolean isLiked;


        public ViewHolder(View v) {
            super(v);
            number = v.findViewById(R.id.number);
            title = v.findViewById(R.id.title);
            like = v.findViewById(R.id.like);
            numbVerses = v.findViewById(R.id.numb_verses);
            cardView = v.findViewById(R.id.card_view);
            dotsContainer = v.findViewById(R.id.dots_container);
        }
    }

    public static class SeparatorViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public SeparatorViewHolder(View v) {
            super(v);
            title = (TextView) v;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (showHeaders && (position == firstHeaderPosition || position == secondHeaderPosition)) {
            return TYPE_SEPARATOR;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEPARATOR) {
            return new SeparatorViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.favourite_header_item, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_card_item, parent, false));
    }

    public void filter(String text) {
        this.currentSearchText = text;
        swapCursor(queryHymnTable(text));
    }

    public HymntableCursor swapCursor(HymntableCursor cursor) {
        Log.d(TAG, "swapCursor: ");
        if (dataCursor == cursor) {
            return null;
        }
        HymntableCursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        calculateHeaders();
        if (cursor != null) {
            Utils.cursor = cursor;
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    private void calculateHeaders() {
        firstHeaderPosition = -1;
        secondHeaderPosition = -1;
        firstHeaderTitle = "";
        secondHeaderTitle = "";
        
        if (showHeaders && dataCursor != null && dataCursor.getCount() > 0 && dataCursor.moveToFirst()) {
            firstHeaderPosition = 0;
            firstHeaderTitle = getCategoryGroup(dataCursor.getCategory());
            
            String firstCategory = firstHeaderTitle;
            int count = 0;
            while (dataCursor.moveToNext()) {
                count++;
                String currentCategory = getCategoryGroup(dataCursor.getCategory());
                if (!currentCategory.equals(firstCategory)) {
                    secondHeaderPosition = 1 + count;
                    secondHeaderTitle = currentCategory;
                    break;
                }
            }
        }
        Log.d(TAG, "calculateHeaders: H1=" + firstHeaderPosition + ", H2=" + secondHeaderPosition);
    }

    private String getCategoryGroup(String category) {
        if (category == null) return "";
        if (category.endsWith("HS")) return "Home Songs";
        if (category.endsWith("ORIGINAL")) return "Original Songs";
        return category;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder rawHolder, int position) {
        if (getItemViewType(position) == TYPE_SEPARATOR) {
            SeparatorViewHolder holder = (SeparatorViewHolder) rawHolder;
            if (position == firstHeaderPosition) {
                holder.title.setText(firstHeaderTitle);
            } else {
                holder.title.setText(secondHeaderTitle);
            }
            return;
        }

        ViewHolder holder = (ViewHolder) rawHolder;
        int cursorPosition = position;
        if (secondHeaderPosition != -1 && position > secondHeaderPosition) {
            cursorPosition = position - 2;
        } else if (firstHeaderPosition != -1 && position > firstHeaderPosition) {
            cursorPosition = position - 1;
        }

        dataCursor.moveToPosition(cursorPosition);
        try {
            holder.title.setText(dataCursor.getTitle());
            holder.isLiked = dataCursor.getLike();
            changeLikeImageButton(holder.like, holder.isLiked);
            int verses = findLargestNumber(dataCursor.getContent());
            String navigationText;
            if (verses > 0)
                navigationText = String.format(Locale.US, "Hymn %d • %d verses", dataCursor.getNumber(), verses);
            else
                navigationText = String.format(Locale.US, "Hymn %d", dataCursor.getNumber());
            holder.numbVerses.setText(navigationText);
            holder.number.setText(String.valueOf(dataCursor.getNumber()));

            updateDots(holder.dotsContainer, dataCursor.getOpenCount());

            int finalCursorPosition = cursorPosition;
            holder.cardView.setOnClickListener(view -> {
                Utils.getInstance();
                Utils.position = finalCursorPosition;
                Utils.cursor = dataCursor;
                activity.startActivity(new Intent(activity, SongActivity.class));
            });

            holder.like.setOnClickListener(view -> {
                holder.isLiked = !holder.isLiked;
                changeLikeImageButton(holder.like, holder.isLiked);
                changeLikePreference(holder.isLiked, holder.title.getText().toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDots(LinearLayout container, Integer count) {
        container.removeAllViews();
        // Don't display dots if count is 0, null, or reaches maximum (15)
        if (count == null || count <= 0 || count >= 15) return;

        int dotsToShow = ((count - 1) % 5) + 1;
        int colorResId = getDotColor(count);

        int dotSize = (int) (6 * activity.getResources().getDisplayMetrics().density);
        int margin = (int) (2 * activity.getResources().getDisplayMetrics().density);

        for (int i = 0; i < dotsToShow; i++) {
            View dot = new View(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotSize, dotSize);
            params.setMargins(margin, 0, margin, 0);
            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.ic_circle);
            dot.setBackgroundTintList(activity.getResources().getColorStateList(colorResId));
            container.addView(dot);
        }
    }

    private int getDotColor(int count) {
        if (count <= 1) return R.color.blue_l1_1;
        if (count == 2) return R.color.blue_l1_2;
        if (count == 3) return R.color.blue_l1_3;
        if (count == 4) return R.color.blue_l1_4;
        if (count == 5) return R.color.blue_l1_5;
        if (count == 6) return R.color.blue_l2_1;
        if (count == 7) return R.color.blue_l2_2;
        if (count == 8) return R.color.blue_l2_3;
        if (count == 9) return R.color.blue_l2_4;
        if (count == 10) return R.color.blue_l2_5;
        if (count == 11) return R.color.blue_l3_1;
        if (count == 12) return R.color.blue_l3_2;
        if (count == 13) return R.color.blue_l3_3;
        if (count == 14) return R.color.blue_l3_4;
        return R.color.blue_l3_5;
    }

    private void changeLikeImageButton(@NonNull ImageView like, Boolean value) {
        int image = Boolean.TRUE.equals(value) ? R.drawable.ic_star_black_36dp : R.drawable.ic_star_outline_black_36dp;
        like.setImageDrawable(activity.getResources().getDrawable(image));
    }

    private void changeLikePreference(boolean liked, String title) {
        HymntableContentValues values = new HymntableContentValues();
        values.putLike(liked);
        values.update(activity.getContentResolver(), new HymntableSelection().titleLike(title));
        swapCursor(queryHymnTable(currentSearchText));
    }

    private HymntableCursor queryHymnTable(String text) {
        HymntableSelection selection = new HymntableSelection();
        if (text != null && !text.isEmpty()) {
            selection.openParen();
            try {
                selection.number(Integer.valueOf(text));
            } catch (NumberFormatException e) {
                Log.d(TAG, "Can't convert text to int");
                selection.titleContains(text).or().contentContains(text);
            }
            selection.closeParen();
            selection.and();
        }


        Log.d(TAG, "queryHymnTable: " + isFromCategoryFragment);

        if (showFavoriteScreen) {
            selection.like(true).and();
        } else if (isFromCategoryFragment) {
            selection.categoryContains(category).and();
        }

        if (songType.equals(Utils.HOME_SONGS)) {
            return selection.categoryEndsWith("HS").orderByNumber().query(activity.getContentResolver());
        } else if (songType.equals(Utils.ORIGINAL_SONGS)) {
            return selection.categoryEndsWith("ORIGINAL").orderByNumber().query(activity.getContentResolver());
        } else if (showFavoriteScreen) {
            return selection.orderByCategory(true).orderByNumber().query(activity.getContentResolver());
        } else {
            return selection.orderByNumber().query(activity.getContentResolver());
        }
    }

    @Override
    public int getItemCount() {
        if (dataCursor == null) return 0;
        int count = dataCursor.getCount();
        if (showHeaders) {
            if (firstHeaderPosition != -1) count++;
            if (secondHeaderPosition != -1) count++;
        }
        return count;
    }
}
