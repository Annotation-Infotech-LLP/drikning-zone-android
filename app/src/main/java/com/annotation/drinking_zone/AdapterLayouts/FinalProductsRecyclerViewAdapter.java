package com.annotation.drinking_zone.AdapterLayouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.R;


import java.util.List;

public class FinalProductsRecyclerViewAdapter extends RecyclerView.Adapter<FinalProductsRecyclerViewAdapter.PostHolder>{

    private Context ctx;
    private List<ProductsData> productsDataList;
    View productItemView;
    int count;


    public FinalProductsRecyclerViewAdapter(Context ctx, List<ProductsData> productsDataList) {
        this.ctx = ctx;
        this.productsDataList = productsDataList;
    }

    @NonNull
    @Override
    public FinalProductsRecyclerViewAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.summarized_final_products_layout, parent, false);
        return  new FinalProductsRecyclerViewAdapter.PostHolder(productItemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final FinalProductsRecyclerViewAdapter.PostHolder holder, final int position) {
        holder.product_name.setText(productsDataList.get(position).getProduct_name());
        if(productsDataList.get(position).getProduct_deposit().equals("0"))
        {
            holder.deposit_layout.setVisibility(View.GONE);
            holder.refill_layout.setVisibility(View.GONE);
        }
        else
        {
            holder.product_deposit.setText(productsDataList.get(position).getProduct_deposit());
            holder.product_isfreshType.setText(productsDataList.get(position).getProduct_isFresh());
        }
        holder.product_subtotal.setText(productsDataList.get(position).getSubTotal());
        holder.quantity.setText(productsDataList.get(position).getQuantity());
        Glide.with(ctx).load(productsDataList.get(position).getProduct_image_link()).into(holder.product_img);
    }

    @Override
    public int getItemCount() {
        return productsDataList.size();
    }


    public class PostHolder extends RecyclerView.ViewHolder{
        CardView product_card;
        ImageView product_img;
        TextView product_name, product_deposit, product_isfreshType, product_subtotal, quantity;
        LinearLayout refill_layout, deposit_layout;

        PostHolder(@NonNull View itemView) {
            super(itemView);
            product_card = itemView.findViewById(R.id.product_card);
            product_img = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_deposit = itemView.findViewById(R.id.product_deposit);
            product_isfreshType = itemView.findViewById(R.id.isfresh_type);
            product_subtotal = itemView.findViewById(R.id.sub_total);
            refill_layout = itemView.findViewById(R.id.refill_layout);
            deposit_layout = itemView.findViewById(R.id.deposit_layout);
            quantity = itemView.findViewById(R.id.quantity);
        }

    }
}
