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

import com.codephillip.app.hymnbook.AllSongsActivity;
import com.codephillip.app.hymnbook.R;
import com.codephillip.app.hymnbook.models.HymnDatabase;
import com.codephillip.app.hymnbook.utilities.ColourQueue;
import com.codephillip.app.hymnbook.utilities.Utils;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private static final String TAG = CategoryAdapter.class.getSimpleName();
    private ArrayList<String> dataCursor;
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

    public CategoryAdapter(Context mContext, ArrayList<String> cursor) {
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            holder.titleView.setText(dataCursor.get(position));
            holder.numberView.setImageDrawable(Utils.generateTextDrawable(position, colourQueue));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                Utils.category = dataCursor.get(position);
                context.startActivity(new Intent(context, AllSongsActivity.class).putExtra(Utils.CATEGORY, dataCursor.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        // any number other than zero will cause a bug
        return (dataCursor == null) ? 0 : dataCursor.size();
    }
}