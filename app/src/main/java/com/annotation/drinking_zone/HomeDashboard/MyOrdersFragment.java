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
import com.annotation.drinking_zone.AdapterLayouts.OrdersRecyclerViewAdapter;
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

public class MyOrdersFragment extends Fragment implements AsyncTaskListener, RecyclerViewClickHandler {
    View view;
    RecyclerView ordersRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private OrdersRecyclerViewAdapter ordersRecyclerViewAdapter;
    private List<ProductsData> products_data;
    UserSharedPrefDetails userSharedPrefDetails;
    int userId;
    BackgroundWorker viewOrdersWorker;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myorders, container, false);
        ordersRecyclerView = view.findViewById(R.id.orders_recyclerview);
        products_data = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        ordersRecyclerView.setLayoutManager(gridLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userSharedPrefDetails = new UserSharedPrefDetails(view.getContext());
        userId = userSharedPrefDetails.getUser_Id();

        String type = "view_orders";
        viewOrdersWorker = new BackgroundWorker(getActivity());
        viewOrdersWorker.setAsyncTaskListener(this);
        viewOrdersWorker.execute(type, Integer.toString(userId));

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
                boolean success = jsonResult.getBoolean("status");
                if (!success) {
                    if (jsonResult.has("msg")) {
                        String message = jsonResult.getString("msg");
                        new CustomDialogs(view.getContext(), R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "Lets Get Started", "INFORMATION SAYS:", HomeActivity.class);
                    } else {
                        new CustomDialogs(view.getContext(), R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Nothing is yet Added to orders", "Lets Shop", "INFORMATION SAYS:", HomeActivity.class);
                    }
                } else if (success) {
                    if (jsonResult.has("data") && jsonResult.has("msg")) {
                        JSONArray productsDetails = jsonResult.getJSONArray("data");
                        if (productsDetails.length() != 0) {
                            products_data.clear();
                            for (int i = 0; i < productsDetails.length(); i++) {
                                JSONObject single_product = productsDetails.getJSONObject(i);
                                String id = single_product.getString("Id");
                                String order_number = single_product.getString("OrderNumber");
                                String total_amount = single_product.getString("TotalAmount");
                                String payment_status = single_product.getString("PaymentStatus");
                                String delivery_time = single_product.getString("DeliveryTime");
                                String delivery_name = single_product.getString("DeliveryName");
                                String is_quick = single_product.getString("IsQuick");
                                String status = single_product.getString("Status");

                                ProductsData productsData = new ProductsData(id, order_number, total_amount, payment_status, delivery_time, delivery_name, is_quick, status);
                                products_data.add(productsData);
                            }
                            ordersRecyclerViewAdapter = new OrdersRecyclerViewAdapter(view.getContext(), products_data, this);
                            ordersRecyclerView.setAdapter(ordersRecyclerViewAdapter);
                            ordersRecyclerViewAdapter.notifyDataSetChanged();
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

        Log.i("order_id", "onDeleteClicked: "+cid);
        Intent intent = new Intent(getActivity(), ViewSingleOrderActivity.class);
        intent.putExtra("Id", cid);
        startActivity(intent);

    }

    @Override
    public void onQuantityUpdated(int position, String quantity) {}
}
