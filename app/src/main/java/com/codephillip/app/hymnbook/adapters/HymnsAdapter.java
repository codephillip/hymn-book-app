package com.codephillip.app.hymnbook.adapters;

import static com.codephillip.app.hymnbook.utilities.Utils.category;
import static com.codephillip.app.hymnbook.utilities.Utils.cursor;
import static com.codephillip.app.hymnbook.utilities.Utils.findLargestNumber;
import static com.codephillip.app.hymnbook.utilities.Utils.isFromCategoryFragment;
import static com.codephillip.app.hymnbook.utilities.Utils.showFavoriteScreen;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


public class HymnsAdapter extends RecyclerView.Adapter<HymnsAdapter.ViewHolder> {

    private static final String TAG = HymnsAdapter.class.getSimpleName();

    private Activity activity;
    private HymntableCursor dataCursor;


    public HymnsAdapter(FragmentActivity activity, HymntableCursor cursor) {
        this.activity = activity;
        this.dataCursor = cursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView number;
        public TextView title;
        public ImageView like;
        public TextView numbVerses;


        public ViewHolder(View v) {
            super(v);
            number = v.findViewById(R.id.number);
            title = v.findViewById(R.id.title);
            like = v.findViewById(R.id.like);
            numbVerses = v.findViewById(R.id.numb_verses);
            cardView = v.findViewById(R.id.card_view);
        }
    }

    @NonNull
    @Override
    public HymnsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new HymnsAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_card_item, parent, false));
    }

    public void filter(String text) {
        if (text.isEmpty()) {
            Log.d(TAG, "filter: empty string");
        } else {
            text = text.toLowerCase();
            swapCursor(queryHymnTable(text));
        }
    }

    public HymntableCursor swapCursor(HymntableCursor cursor) {
        Log.d(TAG, "swapCursor: ");
        if (dataCursor == cursor) {
            return null;
        }
        HymntableCursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            Utils.cursor = cursor;
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public void onBindViewHolder(@NonNull HymnsAdapter.ViewHolder holder, int position) {
        dataCursor.moveToPosition(holder.getAdapterPosition());
        try {
            holder.title.setText(dataCursor.getTitle());
            changeLikeImageButton(holder.like, dataCursor.getLike());
            int verses = findLargestNumber(dataCursor.getContent());
            holder.numbVerses.setText(String.format(Locale.US, "Hymn %d . %d verses", dataCursor.getNumber(), verses));
            holder.number.setText(String.valueOf(dataCursor.getNumber()));

            holder.cardView.setOnClickListener(view -> {
                Utils.getInstance();
                Utils.position = holder.getAdapterPosition();
                Utils.isSongActivityActive = false;
                activity.startActivity(new Intent(activity, SongActivity.class));
            });

            holder.like.setOnClickListener(view -> {
                cursor.moveToPosition(position);
                changeLikeImageButton(holder.like, !cursor.getLike());
                changeLikePreference(!cursor.getLike(), cursor.getTitle());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeLikeImageButton(@NonNull ImageView like, Boolean value) {
        int image = Boolean.TRUE.equals(value) ? R.drawable.ic_star_black_36dp : R.drawable.ic_star_outline_black_36dp;
        like.setImageDrawable(activity.getResources().getDrawable(image));
    }

    private void changeLikePreference(boolean liked, String title) {
        HymntableContentValues values = new HymntableContentValues();
        values.putLike(liked);
        values.update(activity.getContentResolver(), new HymntableSelection().titleLike(title));
    }

    private HymntableCursor queryHymnTable(String text) {
        HymntableSelection selection = new HymntableSelection();
        selection.titleContains(text);

        Log.d(TAG, "queryHymnTable: " + isFromCategoryFragment);

        if (showFavoriteScreen) {
            selection.and();
            return selection.like(true).query(activity.getContentResolver());
        } else if (isFromCategoryFragment) {
            selection.and();
            Log.d(TAG, "queryHymnTable: category#");
            return selection.category(category).query(activity.getContentResolver());
        } else {
            Log.d(TAG, "queryHymnTable: default#");
            return selection.query(activity.getContentResolver());
        }
    }

    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }
}