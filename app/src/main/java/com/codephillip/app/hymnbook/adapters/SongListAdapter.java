package com.codephillip.app.hymnbook.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codephillip.app.hymnbook.R;


/**
 * Created by codephillip on 20/03/17.
 */

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    Cursor dataCursor;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        private TextView numberView;
        private TextView titleView;
        private ViewHolder(View v) {
            super(v);
            numberView = (TextView) v.findViewById(R.id.number_view);
            titleView = (TextView) v.findViewById(R.id.title_view);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: " + numberView.getText());
                }
            });
        }
    }

    public SongListAdapter(Context mContext, Cursor cursor) {
        dataCursor = cursor;
        context = mContext;
    }

    public SongListAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songlist_row, parent, false);
        return new ViewHolder(cardview);
    }

    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("Metric#", "onBindViewHolder: "+position);
//        try{
//            dataCursor.moveToPosition(position);
//            MetrictableCursor metric = new MetrictableCursor(dataCursor);
//            holder.waterVolumeView.setText(String.valueOf(metric.getWaterVolume()));
//            holder.dateView.setText(metric.getDate());
//            holder.timeView.setText(metric.getTime());
//
//            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
//            int color1 = generator.getRandomColor();
//            TextDrawable drawable = TextDrawable.builder()
//                    .beginConfig()
//                    .width(140)  // width in px
//                    .height(140) // height in px
//                    .endConfig()
//                    .buildRound(metric.getIsirrigating().toString().substring(0,1), color1);
//            holder.imageView.setImageDrawable(drawable);
//        } catch (Exception e){
//            e.printStackTrace();
//        }

        holder.titleView.setText("Be like You" + position);
        holder.numberView.setText("34" + position);
    }

    @Override
    public int getItemCount() {
        // any number other than zero will cause a bug
        return (dataCursor == null) ? 10 : dataCursor.getCount();
    }
}