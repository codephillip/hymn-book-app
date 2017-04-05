package com.codephillip.app.hymnbook.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codephillip.app.hymnbook.R;
import com.codephillip.app.hymnbook.SongActivity;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;
import com.codephillip.app.hymnbook.utilities.ColourQueue;
import com.codephillip.app.hymnbook.utilities.Utils;


public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private static final String TAG = SongListAdapter.class.getSimpleName();
    private HymntableCursor dataCursor;
    private static Context context;
    private ColourQueue colourQueue;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        private ImageView numberView;
        private TextView titleView;

        private ViewHolder(View v) {
            super(v);
            numberView = (ImageView) v.findViewById(R.id.numberImageView);
            titleView = (TextView) v.findViewById(R.id.title_view);
        }
    }

    public SongListAdapter(Context mContext, HymntableCursor cursor) {
        Utils.getInstance();
        cursor = cursor;
        dataCursor = cursor;
        context = mContext;
        colourQueue = new ColourQueue();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songlist_row, parent, false);
        return new ViewHolder(cardview);
    }

    public HymntableCursor swapCursor(HymntableCursor cursor) {
        Log.d(TAG, "swapCursor: ");
        if (dataCursor == cursor) {
            return null;
        }
        HymntableCursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            cursor = cursor;
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public void filter(String text) {
        if (text.isEmpty()) {
            Log.d(TAG, "filter: empty string");
        } else {
            text = text.toLowerCase();
            HymntableSelection selection = new HymntableSelection();
            selection.titleContains(text);
            selection.contentContains(text);
            HymntableCursor cursor = selection.query(context.getContentResolver());
            swapCursor(cursor);
        }
    }

//    private HymntableCursor queryHymnTable() {
//        if (showFavoriteScreen) {
//            return new HymntableSelection().like(true).query(context.getContentResolver());
//        } else if (isFromCategoryFragment) {
//            return new HymntableSelection().categoryContains(category).query(context.getContentResolver());
//        } else {
//            return new HymntableSelection().query(context.getContentResolver());
//        }
//    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        dataCursor.moveToPosition(position);
        try {
            holder.titleView.setText(dataCursor.getTitle());
            holder.numberView.setImageDrawable(Utils.generateTextDrawable(dataCursor.getNumber() - 1, colourQueue));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                Utils.getInstance();
                Utils.position = position;
                Utils.isSongActivityActive = false;
                context.startActivity(new Intent(context, SongActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        // any number other than zero will cause a bug
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }
}