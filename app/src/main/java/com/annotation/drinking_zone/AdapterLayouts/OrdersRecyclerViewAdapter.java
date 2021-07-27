package com.annotation.drinking_zone.AdapterLayouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.RecyclerViewClickHandler;
import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.PostHolder>{

    private Context ctx;
    private List<ProductsData> productsDataList;
    View productItemView;

    private final RecyclerViewClickHandler clickHandler;

    public OrdersRecyclerViewAdapter(Context ctx, List<ProductsData> productsDataList, RecyclerViewClickHandler clickHandler) {
        this.ctx = ctx;
        this.productsDataList = productsDataList;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_orders_single_layout, parent, false);
        return  new PostHolder(productItemView, clickHandler);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.order_number.setText(productsDataList.get(position).getOrder_number());
        holder.total_amount.setText(productsDataList.get(position).getTotal_amount());
        holder.is_quick.setText(productsDataList.get(position).getIs_quick());
        holder.payment_status.setText(productsDataList.get(position).getPayment_status());
        holder.delivery_name.setText(productsDataList.get(position).getDelivery_name());
        holder.delivery_time.setText(productsDataList.get(position).getDelivery_time());
        holder.status.setText(productsDataList.get(position).getStatus());

        if(productsDataList.get(position).getPayment_status().equals("Paid"))
        {
            holder.payment_status.setTextColor(ctx.getResources().getColor(R.color.colorgreens));
        }
        else
        {
            holder.payment_status.setTextColor(ctx.getResources().getColor(R.color.grapeFruit));
        }

    }

    @Override
    public int getItemCount() {
        return productsDataList.size();
    }


    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView product_card;
        TextView order_number, total_amount,payment_status, delivery_name, delivery_time, status, is_quick;
        private WeakReference<RecyclerViewClickHandler> listenerRef;

        PostHolder(@NonNull View itemView, RecyclerViewClickHandler listener) {
            super(itemView);
            product_card = itemView.findViewById(R.id.order_card);
            order_number= itemView.findViewById(R.id.order_number);
            total_amount= itemView.findViewById(R.id.total_amount);
            payment_status= itemView.findViewById(R.id.payment_status);
            delivery_name= itemView.findViewById(R.id.delivery_name);
            delivery_time= itemView.findViewById(R.id.delivery_time);
            status= itemView.findViewById(R.id.status);
            is_quick= itemView.findViewById(R.id.is_quick);
            listenerRef = new WeakReference<>(listener);
            product_card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(product_card.equals(v))
            {
                listenerRef.get().onDeleteClicked(getAdapterPosition());
            }
        }
    }
}
