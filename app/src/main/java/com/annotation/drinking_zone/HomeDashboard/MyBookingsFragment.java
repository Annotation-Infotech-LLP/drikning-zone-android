package com.annotation.drinking_zone.HomeDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annotation.drinking_zone.AdapterLayouts.BookedPackagesRecyclerViewAdapter;
import com.annotation.drinking_zone.CheckOutBillingStage.PlaceOrderActivity;
import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.RecyclerViewClickHandler;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsFragment extends Fragment implements AsyncTaskListener, RecyclerViewClickHandler {
    View view;
    RecyclerView bookedPackagesRecycler;
    private GridLayoutManager gridLayoutManager;
    private BookedPackagesRecyclerViewAdapter bookedPackagesRecyclerViewAdapter;
    private List<ProductsData> products_data;
    UserSharedPrefDetails userSharedPrefDetails;
    int userId;
    BackgroundWorker viewBookingProducts;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mybookings, container, false);
        bookedPackagesRecycler = view.findViewById(R.id.booked_packages_recyclerview);
        products_data = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        bookedPackagesRecycler.setLayoutManager(gridLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userSharedPrefDetails = new UserSharedPrefDetails(view.getContext());
        userId = userSharedPrefDetails.getUser_Id();

        String type = "view_bookings";
        viewBookingProducts = new BackgroundWorker(getActivity());
        viewBookingProducts.setAsyncTaskListener(this);
        viewBookingProducts.execute(type, Integer.toString(userId));

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("##tag", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Intent intent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void updateResult(String result) {
        Log.i("#Result All orders", "updateResult: " + result);
//                Parsing JSON DATA
        if(result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);
//            int success = jsonResult.getInt("success");
                boolean success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {
                    if (jsonResult.has("msg")) {
                        String message = jsonResult.getString("msg");
                        new CustomDialogs(view.getContext(), R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "Lets Get Started", "INFORMATION SAYS:", HomeActivity.class);
                    } else {
                        new CustomDialogs(view.getContext(), R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Nothing is yet Added to your monthly packages", "Lets Shop", "INFORMATION SAYS:", HomeActivity.class);
                    }
                } else if (success) {
                    if (jsonResult.has("data") && jsonResult.has("msg")) {
                        JSONArray productsDetails = jsonResult.getJSONArray("data");
                        if (productsDetails.length() != 0) {
                            products_data.clear();
                            for (int i = 0; i < productsDetails.length(); i++) {
                                JSONObject single_product = productsDetails.getJSONObject(i);
                                String id = single_product.getString("Id");
                                String product_name = single_product.getString("ProductName");
                                String product_price = single_product.getString("Price");
                                String product_deposit = single_product.getString("Deposit");
                                String productImg_link = single_product.getString("FilePath");
                                String product_quantity = single_product.getString("Quantity");
                                String product_subTotal = single_product.getString("SubTotal");
                                String total_amount = single_product.getString("TotalAmount");
                                String booking_number = single_product.getString("BookingNumber");
                                String start_date = single_product.getString("StartDate");
                                String end_date = single_product.getString("EndDate");
                                String booked_on = single_product.getString("BookedOn");
                                String address_id = single_product.getString("AddressId");
                                String reminder = single_product.getString("TimeForRenew");
//                            String reminder = single_product.getString("Yes");

                                ProductsData productsData = new ProductsData(id, product_name, booking_number, product_price, product_deposit, total_amount, product_subTotal, productImg_link, product_quantity, booked_on, start_date, end_date, address_id, reminder);
                                products_data.add(productsData);
                            }
                            bookedPackagesRecyclerViewAdapter = new BookedPackagesRecyclerViewAdapter(view.getContext(), products_data, this);
                            bookedPackagesRecycler.setAdapter(bookedPackagesRecyclerViewAdapter);
                            bookedPackagesRecyclerViewAdapter.notifyDataSetChanged();
                        } else {
                            new CustomDialogs(view.getContext(), R.color.colorgreen, R.drawable.ic_mood_black_24dp, "Nothing is yet Added to ordered Packages", "OK", "INFORMATION SAYS:", HomeActivity.class);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "We didnt got any response...Ooopss Network Error!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteClicked(int position) {

        String cid = products_data.get(position).getId();
        String product_name = products_data.get(position).getProduct_name();
        String product_deposit = products_data.get(position).getProduct_deposit();
        String productImg_link = products_data.get(position).getProduct_image_link();
        String product_quantity = products_data.get(position).getQuantity();
        String product_subTotal = products_data.get(position).getSubTotal();
        String total_amount = products_data.get(position).getTotalPrice();
        String end_date = products_data.get(position).getEnd_date();
        String address_id = products_data.get(position).getAddress_id();

        Intent intent = new Intent(getActivity(), PlaceOrderActivity.class);
        intent.putExtra("Id", cid);
        intent.putExtra("product_name", product_name);
        intent.putExtra("product_deposit", product_deposit);
        intent.putExtra("productImg_link", productImg_link);
        intent.putExtra("product_quantity", product_quantity);
        intent.putExtra("product_subTotal", product_subTotal);
        intent.putExtra("total_amount", total_amount);
        intent.putExtra("isFresh", "No");
        intent.putExtra("end_date", end_date);
        intent.putExtra("address_id", address_id);
        startActivity(intent);

    }

    @Override
    public void onQuantityUpdated(int position, String quantity) {

    }
}
