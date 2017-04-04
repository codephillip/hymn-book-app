package com.codephillip.app.hymnbook.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.codephillip.app.hymnbook.R;
import com.codephillip.app.hymnbook.SongActivity;
import com.codephillip.app.hymnbook.utilities.ColourQueue;
import com.codephillip.app.hymnbook.utilities.Utils;

import static com.codephillip.app.hymnbook.utilities.Utils.position;

/**
 * Created by codephillip on 31/03/17.
 */

public class SongGridAdapter extends RecyclerView.Adapter<SongGridAdapter.ViewHolder> {

    private final ColourQueue colourQueue;
    private String[] mData = new String[0];
    private LayoutInflater mInflater;
    private static Context context;

    // data is passed into the constructor
    public SongGridAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
        colourQueue = new ColourQueue();
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
        String animal = mData[position];
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getColor(colourQueue.getCount());
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(140)  // width in px
                .height(140) // height in px
                .endConfig()
                .buildRound(animal, color1);
        holder.numberView.setImageDrawable(drawable);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
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
}