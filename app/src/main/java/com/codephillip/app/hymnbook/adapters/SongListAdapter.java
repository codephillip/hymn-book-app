package com.codephillip.app.hymnbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codephillip.app.hymnbook.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private static final String TAG = SongListAdapter.class.getSimpleName();
    JSONArray dataCursor;
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

    public SongListAdapter(Context mContext, JSONArray cursor) {
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

    public JSONArray swapCursor(JSONArray cursor) {
        Log.d(TAG, "swapCursor: ");
        if (dataCursor == cursor) {
            return null;
        }
        JSONArray oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = dataCursor.getJSONObject(position);
            holder.titleView.setText("Be like You" + jsonObject.getString("title"));
            holder.numberView.setText("34" + position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        // any number other than zero will cause a bug
        return (dataCursor == null) ? 0 : dataCursor.length();
    }
}