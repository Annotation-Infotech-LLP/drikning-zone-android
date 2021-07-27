package com.annotation.drinking_zone.AdapterLayouts;

import android.content.Context;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.InputFilterMinMax;
import com.annotation.drinking_zone.Utility.RecyclerViewClickHandler;

import java.lang.ref.WeakReference;
import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.PostHolder>{

    private Context ctx;
    private List<ProductsData> productsDataList;
    View productItemView;
    int count;

    private final RecyclerViewClickHandler clickHandler;

    public CartRecyclerViewAdapter(Context ctx, List<ProductsData> productsDataList, RecyclerViewClickHandler clickHandler) {
        this.ctx = ctx;
        this.productsDataList = productsDataList;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public CartRecyclerViewAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_carted_products, parent, false);
        return  new CartRecyclerViewAdapter.PostHolder(productItemView, clickHandler);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartRecyclerViewAdapter.PostHolder holder, final int position) {
        holder.product_name.setText(productsDataList.get(position).getProduct_name());
        holder.product_price.setText(productsDataList.get(position).getProduct_price());
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


    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView product_card;
        ImageView product_img;
        TextView product_name, product_price, product_deposit, product_isfreshType, product_subtotal;
        ImageView delete_cart;
        EditText quantity;
        ImageView quantity_inc, quantity_dec;
        LinearLayout refill_layout, deposit_layout;
        private WeakReference<RecyclerViewClickHandler> listenerRef;

        PostHolder(@NonNull View itemView, RecyclerViewClickHandler listener) {
            super(itemView);
            product_card = itemView.findViewById(R.id.product_card);
            product_img = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_deposit = itemView.findViewById(R.id.product_deposit);
            product_isfreshType = itemView.findViewById(R.id.isfresh_type);
            delete_cart = itemView.findViewById(R.id.delete_cart_btn);
            quantity = itemView.findViewById(R.id.sproduct_quantity);
            quantity_inc = itemView.findViewById(R.id.product_quantity_up);
            quantity_dec = itemView.findViewById(R.id.product_quantity_down);
            product_subtotal = itemView.findViewById(R.id.sub_total);
            refill_layout = itemView.findViewById(R.id.refill_layout);
            deposit_layout = itemView.findViewById(R.id.deposit_layout);
            listenerRef = new WeakReference<>(listener);

            delete_cart.setOnClickListener(this);
            quantity_inc.setOnClickListener(this);
            quantity_dec.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(delete_cart.equals(v))
            {
                listenerRef.get().onDeleteClicked(getAdapterPosition());
            }
            else if(quantity_inc.equals(v))
            {
                String min_quantity = "1";
//                String position = productsDataList.get(getAdapterPosition());
//                String max_quantity = productsDataList.get(getAdapterPosition()).getUpto_quantity();

                String max_quantity = "100";
                quantity.setFilters(new InputFilter[]{ new InputFilterMinMax(min_quantity, max_quantity)});
                if(quantity.getText().toString().equals(""))
                {
                    quantity.setText(String.valueOf(min_quantity));
                    listenerRef.get().onQuantityUpdated(getAdapterPosition(), String.valueOf(count));
                }
                count = Integer.parseInt(String.valueOf(quantity.getText()));
                if(count < Integer.parseInt(String.valueOf(max_quantity)))
                {
                    count++;
                    Log.i("inc count", "onClick: "+count);
                    quantity.setText(String.valueOf(count));
                    listenerRef.get().onQuantityUpdated(getAdapterPosition(), String.valueOf(count));
                }
                else if(count == Integer.parseInt(String.valueOf(max_quantity)))
                {
                    quantity.setText(String.valueOf(count));
                    Toast.makeText(ctx, "The Maximum Quantity can be only Upto "+max_quantity, Toast.LENGTH_LONG).show();
                    listenerRef.get().onQuantityUpdated(getAdapterPosition(), String.valueOf(count));
                }
                else
                {
                    quantity.setText(String.valueOf(count));
                    Toast.makeText(ctx, "The Maximum Quantity can be only upto "+max_quantity, Toast.LENGTH_LONG).show();
                    listenerRef.get().onQuantityUpdated(getAdapterPosition(), String.valueOf(count));
                }

            }
            else if (quantity_dec.equals(v))
            {
                String min_quantity = "1";
//                String position = productsDataList.get(getAdapterPosition()).getId();
                String max_quantity = productsDataList.get(getAdapterPosition()).getUpto_quantity();
                if(quantity.getText().toString().equals(""))
                {
                    quantity.setText(String.valueOf(min_quantity));
                    listenerRef.get().onQuantityUpdated(getAdapterPosition(), String.valueOf(count));
                }
                count = Integer.parseInt(String.valueOf(quantity.getText()));
                if(count>Integer.parseInt(String.valueOf(min_quantity)))
                {
                    count--;
                    quantity.setText(String.valueOf(count));
                    listenerRef.get().onQuantityUpdated(getAdapterPosition(), String.valueOf(count));
                }
                else if(count == Integer.parseInt(String.valueOf(min_quantity)))
                {
                    quantity.setText(String.valueOf(count));
                    listenerRef.get().onQuantityUpdated(getAdapterPosition(), String.valueOf(count));
                    Toast.makeText(ctx, "The Minimum Quantity can be "+min_quantity, Toast.LENGTH_LONG).show();
                }
                else
                {

                    quantity.setText(String.valueOf(count));
                    listenerRef.get().onQuantityUpdated(getAdapterPosition(), String.valueOf(count));
                    Toast.makeText(ctx, "The minimum Quantity can be "+min_quantity, Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
