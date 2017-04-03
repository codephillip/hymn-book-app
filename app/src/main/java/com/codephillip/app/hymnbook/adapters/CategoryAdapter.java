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
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color1 = generator.getColor(colourQueue.getCount());
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(140)  // width in px
                    .height(140) // height in px
                    .endConfig()
                    .buildRound(String.valueOf(position + 1), color1);
            holder.numberView.setImageDrawable(drawable);
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
        return (dataCursor == null) ? 0 : dataCursor.size();
    }
}