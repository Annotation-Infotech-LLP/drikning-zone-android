package com.annotation.drinking_zone.AdapterLayouts;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.ProductRelated.SingleProductActivity;
import com.annotation.drinking_zone.R;


import java.util.List;

public class ProductsViewRecyclerAdapter extends RecyclerView.Adapter<ProductsViewRecyclerAdapter.PostHolder> {

    private Context ctx;
    private List<ProductsData> productsDataList;

    public ProductsViewRecyclerAdapter(Context ctx, List<ProductsData> productsDataList) {
        this.ctx = ctx;
        this.productsDataList = productsDataList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view_layout, parent, false);
        return  new PostHolder(productItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {
        holder.product_name.setText(productsDataList.get(position).getProduct_name());
        holder.product_price.setText(productsDataList.get(position).getProduct_price());
//        holder.product_deposit.setText(productsDataList.get(position).getProduct_deposit());
        Glide.with(ctx).load(productsDataList.get(position).getProduct_image_link()).into(holder.product_img);

        holder.product_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, SingleProductActivity.class);
                intent.putExtra("product_id", productsDataList.get(position).getId());
                intent.putExtra("product_name", productsDataList.get(position).getProduct_name());
                intent.putExtra("product_price", productsDataList.get(position).getProduct_price());
                intent.putExtra("product_deposit", productsDataList.get(position).getProduct_deposit());
                intent.putExtra("product_category", productsDataList.get(position).getProduct_category());
//                intent.putExtra("product_category_name", productsDataList.get(position).getProduct_category_name());
                intent.putExtra("product_image_link", productsDataList.get(position).getProduct_image_link());
                intent.putExtra("product_upto_quantity", productsDataList.get(position).getUpto_quantity());
                intent.putExtra("product_min_quantity", productsDataList.get(position).getMin_quantity());
                intent.putExtra("product_max_quantity", productsDataList.get(position).getMax_quantity());
                int reference_category = productsDataList.get(position).getReference_category();

                if(reference_category > 0)
                {
                    intent.putExtra("product_reference_category", reference_category);
//                    Log.i("CategoryTag", "onClick: "+reference_category);
                    ctx.startActivity(intent);
                }
                else
                {
                    ctx.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsDataList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder{
        CardView product_card;
        ImageView product_img;
        TextView product_name, product_price, product_deposit;
        PostHolder(@NonNull View itemView) {
            super(itemView);
            product_card = itemView.findViewById(R.id.product_card);
            product_img = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
//            product_deposit = itemView.findViewById(R.id.product_deposit_price);
        }
    }
}
