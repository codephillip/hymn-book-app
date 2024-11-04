package com.codephillip.app.hymnbook.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.codephillip.app.hymnbook.R;


public class HymnsAdapter extends RecyclerView.Adapter<HymnsAdapter.ViewHolder> {

    //    private List<OrderEntity> orderEntities;
    private Activity activity;


    //    public CuratedListAdapter(FragmentActivity activity, List<OrderEntity> orderEntities) {
    public HymnsAdapter(FragmentActivity activity, String sample) {
        this.activity = activity;
//        this.orderEntities = orderEntities;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout constraintBubble;
        public TextView amount;
        public TextView orderNumber;
        public ImageView thumbnail;
        public TextView orders;

        public ViewHolder(View v) {
            super(v);
//            orderNumber = v.findViewById(R.id.order_number);
//            thumbnail = v.findViewById(R.id.thumbnail);
//            amount = v.findViewById(R.id.amount);
//            orders = v.findViewById(R.id.order_items_count);
//            constraintBubble = v.findViewById(R.id.constraint_bubble);
        }
    }

    @NonNull
    @Override
    public HymnsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new HymnsAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HymnsAdapter.ViewHolder holder, int position) {
        try {
//            OrderEntity order = orderEntities.get(position);
//
//            holder.orderNumber.setText(order.getName());
//            holder.amount.setText(order.getDescription());
//            holder.orders.setVisibility(View.GONE);
//            holder.constraintBubble.setVisibility(View.GONE);
//
//            Picasso.get()
//                    .load(order.getImage())
//                    .placeholder(R.mipmap.ic_launcher)
//                    .error(R.mipmap.ic_launcher)
//                    .into(holder.thumbnail);
//
//
//            holder.itemView.setOnClickListener(view -> {
//                OrderItemRepository repo = new OrderItemRepository(view.getContext());
//                ProductRepository productRepository = new ProductRepository(view.getContext());
//                saveTemporaryOrderItems(order, repo, productRepository);
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
//        return orderEntities.size();
        return 10;
    }
}