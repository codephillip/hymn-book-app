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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.codephillip.app.hymnbook.R;
import com.codephillip.app.hymnbook.SongActivity;
import com.codephillip.app.hymnbook.models.Hymn;
import com.codephillip.app.hymnbook.models.HymnDatabase;
import com.codephillip.app.hymnbook.utilities.ColourQueue;
import com.codephillip.app.hymnbook.utilities.Utils;

import java.util.ArrayList;


public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private static final String TAG = SongListAdapter.class.getSimpleName();
    private ArrayList<Hymn> dataCursor;
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

    public SongListAdapter(Context mContext, ArrayList<Hymn> cursor) {
        dataCursor = cursor;
        context = mContext;
        HymnDatabase.getInstance();
        colourQueue = new ColourQueue();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songlist_row, parent, false);
        return new ViewHolder(cardview);
    }

    public ArrayList<Hymn> swapCursor(ArrayList<Hymn> cursor) {
        Log.d(TAG, "swapCursor: ");
        Log.d(TAG, String.valueOf(cursor));
        if (dataCursor == cursor) {
            return null;
        }
        ArrayList<Hymn> oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public void filter(String text, ArrayList<Hymn> hymnArrayList) {
        ArrayList<Hymn> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            Log.d(TAG, "filter: empty string");
        } else {
            text = text.toLowerCase();
            for (Hymn hymn : hymnArrayList) {
                if (hymn.getTitle().toLowerCase().contains(text) || hymn.getContent().toLowerCase().contains(text)) {
                    filteredList.add(hymn);
                }
                swapCursor(filteredList);
            }
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            holder.titleView.setText(dataCursor.get(position).getTitle());
            holder.numberView.setImageDrawable(Utils.generateTextDrawable(position, colourQueue));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                Utils.getInstance();
                Utils.position = dataCursor.get(position).getNumber() - 1;
                Utils.isSongActivityActive = false;
                context.startActivity(new Intent(context, SongActivity.class));
            }
        });

        Log.d(TAG, String.valueOf(HymnDatabase.hymns.getHymnArrayList()));
    }

    @Override
    public int getItemCount() {
        // any number other than zero will cause a bug
        return (dataCursor == null) ? 0 : dataCursor.size();
    }
}