package com.annotation.drinking_zone.AdapterLayouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AddressesData;
import com.annotation.drinking_zone.Utility.RecyclerViewClickHandler;

import java.lang.ref.WeakReference;
import java.util.List;

public class AddressesRecyclerViewAdapter extends RecyclerView.Adapter<AddressesRecyclerViewAdapter.PostHolder>{

    private Context ctx;
    private List<AddressesData> addressesDataList;
    View productItemView;

    private final RecyclerViewClickHandler clickHandler;


    public AddressesRecyclerViewAdapter(Context ctx, List<AddressesData> addressesDataList, RecyclerViewClickHandler clickHandler) {
        this.ctx = ctx;
        this.addressesDataList = addressesDataList;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_layout, parent, false);
        return  new PostHolder(productItemView, clickHandler);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostHolder holder, final int position) {
        holder.address_card.setTag(addressesDataList.get(position).getId());
        holder.addressholder_name.setText(addressesDataList.get(position).getName());
        holder.contact_number.setText(addressesDataList.get(position).getMobile());
        holder.address_line1.setText(addressesDataList.get(position).getLine1());
        holder.address_line2.setText(addressesDataList.get(position).getLine2());
        holder.landmark_name.setText(addressesDataList.get(position).getLandmark());
        holder.district_name.setText(addressesDataList.get(position).getDistrict());
        holder.pincode.setText(addressesDataList.get(position).getPincode());
        holder.state_name.setText(addressesDataList.get(position).getState());
        holder.address_type.setText(addressesDataList.get(position).getAddressType());
    }

    @Override
    public int getItemCount() {
        return addressesDataList.size();
    }


    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView address_card;
        TextView addressholder_name, contact_number, address_line1, address_line2, landmark_name, district_name, pincode, state_name, address_type;
        private WeakReference<RecyclerViewClickHandler> listenerRef;

        PostHolder(@NonNull View itemView, RecyclerViewClickHandler listener) {
            super(itemView);
            address_card = itemView.findViewById(R.id.address_card);
            addressholder_name = itemView.findViewById(R.id.addressholder_name);
            contact_number = itemView.findViewById(R.id.contact_number);
            address_line1 = itemView.findViewById(R.id.address_line1);
            address_line2 = itemView.findViewById(R.id.address_line2);
            landmark_name = itemView.findViewById(R.id.landmark_name);
            district_name = itemView.findViewById(R.id.district_name);
            pincode = itemView.findViewById(R.id.pincode);
            state_name = itemView.findViewById(R.id.state_name);
            address_type = itemView.findViewById(R.id.address_type);
            listenerRef = new WeakReference<>(listener);

            address_card.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v.equals(address_card))
            {
                String addressId = String.valueOf(address_card.getTag());
                listenerRef.get().onDeleteClicked(Integer.parseInt(addressId));
            }
        }
    }
}
