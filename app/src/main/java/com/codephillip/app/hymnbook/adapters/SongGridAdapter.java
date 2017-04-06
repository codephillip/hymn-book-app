package com.codephillip.app.hymnbook.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codephillip.app.hymnbook.R;
import com.codephillip.app.hymnbook.SongActivity;
import com.codephillip.app.hymnbook.utilities.ColourQueue;
import com.codephillip.app.hymnbook.utilities.Utils;

import static com.codephillip.app.hymnbook.utilities.Utils.cursor;
import static com.codephillip.app.hymnbook.utilities.Utils.position;

/**
 * Created by codephillip on 31/03/17.
 */

public class SongGridAdapter extends RecyclerView.Adapter<SongGridAdapter.ViewHolder> {

    private final ColourQueue colourQueue;
    private LayoutInflater mInflater;
    private static Context context;

    // data is passed into the constructor
    public SongGridAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        colourQueue = new ColourQueue();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView numberView;

        public ViewHolder(View itemView) {
            super(itemView);
            numberView = (ImageView) itemView.findViewById(R.id.numberImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TAG", "You clicked number " + getAdapterPosition() + ", which is at cell position " + position);
                    Utils.position = getAdapterPosition();
                    //moves the ViewPager to the right position
                    if (Utils.clickedFavorite)
                        Utils.position++;
                    Utils.isSongActivityActive = false;
                    context.startActivity(new Intent(context, SongActivity.class));
                }
            });
        }
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.numberView.setImageDrawable(Utils.generateTextDrawable(cursor.getNumber(), colourQueue));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}