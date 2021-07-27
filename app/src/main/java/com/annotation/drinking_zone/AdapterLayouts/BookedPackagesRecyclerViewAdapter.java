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

import com.bumptech.glide.Glide;
import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.RecyclerViewClickHandler;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

public class BookedPackagesRecyclerViewAdapter extends RecyclerView.Adapter<BookedPackagesRecyclerViewAdapter.PostHolder>{

    private Context ctx;
    private List<ProductsData> productsDataList;
    View productItemView;
    int count;
    Date current_date;
    int current_day, package_endDate, reminder_Date, current_month, package_start_month, package_end_month;

    private final RecyclerViewClickHandler clickHandler;

    public BookedPackagesRecyclerViewAdapter(Context ctx, List<ProductsData> productsDataList, RecyclerViewClickHandler clickHandler) {
        this.ctx = ctx;
        this.productsDataList = productsDataList;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_booked_orders_single_layout, parent, false);
        return  new PostHolder(productItemView, clickHandler);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.product_name.setText(productsDataList.get(position).getProduct_name());
        holder.booking_id.setText(productsDataList.get(position).getBookingId());
        holder.one_product_price.setText(productsDataList.get(position).getProduct_price());
        holder.product_deposit.setText(productsDataList.get(position).getProduct_deposit());
        holder.total_price.setText(productsDataList.get(position).getTotalPrice());
        holder.subTotal.setText(productsDataList.get(position).getSubTotal());
        holder.quantity.setText(productsDataList.get(position).getQuantity());
        holder.booked_on.setText(productsDataList.get(position).getBooked_on());
        holder.start_date.setText(productsDataList.get(position).getStart_date());
        holder.end_date.setText(productsDataList.get(position).getEnd_date());
        Glide.with(ctx).load(productsDataList.get(position).getProduct_image_link()).into(holder.product_img);

        String reminder = productsDataList.get(position).getReminder();
        if(reminder.equals("Yes"))
        {
            holder.renewal_btn.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.renewal_btn.setVisibility(View.GONE);
        }

//        try {
//
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat currentDay = new SimpleDateFormat("dd");
//            SimpleDateFormat currentMonth = new SimpleDateFormat("MM");
//
//            Date date = df.parse(holder.end_date.getText().toString());
//            Date start_date = df.parse(holder.start_date.getText().toString());
//            current_date = Calendar.getInstance().getTime();
//
//            String formatedDay = currentDay.format(current_date);
//            String formatedMonth = currentMonth.format(current_date);
//            String end_date_package = currentDay.format(date);
//            String end_month = currentMonth.format(date);
//            String start_p_date = currentMonth.format(start_date);
//
//            current_day = Integer.parseInt(formatedDay);
//            package_endDate = Integer.parseInt(end_date_package);
//            reminder_Date = package_endDate - 7;
//            current_month = Integer.parseInt(formatedMonth)-1;
//            package_start_month = Integer.parseInt(start_p_date);
//            package_end_month = Integer.parseInt(end_month);
//
//            if(current_day >= reminder_Date && current_day <= package_endDate && current_month > package_start_month)
//            {
//                holder.renewal_btn.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                holder.renewal_btn.setVisibility(View.GONE);
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return productsDataList.size();
    }


    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView product_card;
        ImageView product_img;
        TextView product_name, one_product_price,subTotal, total_price, product_deposit, booking_id, booked_on, start_date, end_date, quantity;
        Button renewal_btn;
        private WeakReference<RecyclerViewClickHandler> listenerRef;

        PostHolder(@NonNull View itemView, RecyclerViewClickHandler listener) {
            super(itemView);
            product_card = itemView.findViewById(R.id.booked_product_card);
            product_img = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            booking_id = itemView.findViewById(R.id.booking_id);
            product_deposit = itemView.findViewById(R.id.product_deposit);
            one_product_price = itemView.findViewById(R.id.product_price);
            total_price = itemView.findViewById(R.id.total_amt_paid);
            quantity = itemView.findViewById(R.id.product_quantity);
            booked_on = itemView.findViewById(R.id.booked_on);
            start_date = itemView.findViewById(R.id.start_date);
            end_date = itemView.findViewById(R.id.end_date);
            subTotal = itemView.findViewById(R.id.sub_total);
            renewal_btn = itemView.findViewById(R.id.renewal_btn);
            listenerRef = new WeakReference<>(listener);
//            if(renewal_btn.isShown())
//            {
               renewal_btn.setOnClickListener(this);
//                Toast.makeText(ctx, "Renewal button is showing", Toast.LENGTH_SHORT).show();
//            }
        }

        @Override
        public void onClick(View v) {
            if(renewal_btn.equals(v))
            {
                listenerRef.get().onDeleteClicked(getAdapterPosition());
            }
        }
    }
}
