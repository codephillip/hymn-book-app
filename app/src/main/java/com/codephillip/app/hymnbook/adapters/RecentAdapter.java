package com.codephillip.app.hymnbook.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codephillip.app.hymnbook.R;


public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {

    private FragmentManager supportFragmentManager;
//    private ProductBottomSheetFragment dialog;
//    private List<ProductEntity> productEntities;
    Activity activity;

    public RecentAdapter(FragmentActivity activity, String sample) {
//    public ProductDiscountAdapter(FragmentActivity activity, List<ProductEntity> productEntities) {
        this.activity = activity;
//        this.productEntities = productEntities;
        this.supportFragmentManager = activity.getSupportFragmentManager();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView price;
        public TextView discount;
        public ImageView thumbnail;

        public ViewHolder(View v) {
            super(v);
//            name = v.findViewById(R.id.product_name);
//            thumbnail = v.findViewById(R.id.thumbnail);
//            price = v.findViewById(R.id.price);
//            discount = v.findViewById(R.id.discount_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
//            ProductEntity product = productEntities.get(position);
//            holder.name.setText(activity.getString(R.string.metric_fmt, product.getName(), product.getMetric()));
//            holder.price.setText(activity.getString(R.string.price_fmt, product.getDiscountedPrice()));
//            holder.discount.setText(activity.getString(R.string.discount_fmt, product.getDiscount()));
//
//            Picasso.get()
//                    .load(product.getImage())
//                    .placeholder(R.mipmap.ic_launcher)
//                    .error(R.mipmap.ic_launcher)
//                    .into(holder.thumbnail);
//
//            holder.itemView.setOnClickListener(v -> {
//            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
//        return productEntities.size();
        return 10;
    }
}