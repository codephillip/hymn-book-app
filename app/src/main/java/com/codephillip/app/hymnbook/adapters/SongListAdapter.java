package com.codephillip.app.hymnbook.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codephillip.app.hymnbook.R;
import com.codephillip.app.hymnbook.SongActivity;
import com.codephillip.app.hymnbook.Utils;
import com.codephillip.app.hymnbook.models.Hymn;
import com.codephillip.app.hymnbook.models.HymnDatabase;

import java.util.ArrayList;


public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private static final String TAG = SongListAdapter.class.getSimpleName();
    ArrayList<Hymn> dataCursor;
    private static Context context;

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
                    Utils.getInstance();
                    //subtract 1 because the ArrayList starts from zero
                    Utils.position = Integer.parseInt(String.valueOf(numberView.getText()));
                    Utils.isSongActivityActive = false;
                    context.startActivity(new Intent(context, SongActivity.class));
                }
            });
        }
    }

    public SongListAdapter(Context mContext, ArrayList<Hymn> cursor) {
        dataCursor = cursor;
        context = mContext;
        HymnDatabase.getInstance();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songlist_row, parent, false);
        return new ViewHolder(cardview);
    }

    public ArrayList<Hymn> swapCursor(ArrayList<Hymn> cursor) {
        Log.d(TAG, "swapCursor: ");
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

    public void filter(String text) {
        Log.d(TAG, "filter: " + text);
        dataCursor.clear();
        if(text.isEmpty()){
            dataCursor = HymnDatabase.hymns.getHymnArrayList();
        } else{
            text = text.toLowerCase();
            for (Hymn hymn : HymnDatabase.hymns.getHymnArrayList()) {
                if(hymn.getTitle().toLowerCase().contains(text) || hymn.getContent().toLowerCase().contains(text) || hymn.getNumber() == Integer.parseInt(text)) {
                    dataCursor.add(hymn);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {
            holder.titleView.setText(dataCursor.get(position).getTitle());
            holder.numberView.setText(String.valueOf(dataCursor.get(position).getNumber()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        // any number other than zero will cause a bug
        return (dataCursor == null) ? 0 : dataCursor.size();
    }
}