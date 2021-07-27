package com.annotation.drinking_zone.HomeDashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.annotation.drinking_zone.AdapterLayouts.AddressesRecyclerViewAdapter;
import com.annotation.drinking_zone.AdapterLayouts.FinalProductsRecyclerViewAdapter;
import com.annotation.drinking_zone.AdapterLayouts.OrdersRecyclerViewAdapter;
import com.annotation.drinking_zone.AdapterLayouts.ViewOrderItemsAdapter;
import com.annotation.drinking_zone.CheckOutBillingStage.PlaceOrderActivity;
import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AddressesData;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewSingleOrderActivity extends AppCompatActivity implements AsyncTaskListener {

    RecyclerView all_final_products_recyclerview;
    Button cancel_order_btn;
    UserSharedPrefDetails sharedPrefDetails;
    private GridLayoutManager gridLayoutManager;
    private List<ProductsData> products_data;
    BackgroundWorker get_single_order_worker, cancel_order_worker;
    ViewOrderItemsAdapter viewOrderItemsAdapter;
    TextView total_price, number_of_products, amount_to_be_paid, order_number, payment_status_view, delivery_time, order_status;
    TextView deliveryName, deliveryContact, deliveryLine1, deliveryLine2, deliveryLandMark, deliveryDistrict, deliveryPincode, deliveryState, deliveryAddressType;
    int userId;
    JSONObject orderDetailsObject, single_product;
    Intent intent;
    String id, itemId, productId, product_name, product_price, product_deposit, productImg_link, product_quantity, product_isFresh, product_subTotal, order_id, viewSingleOrderType, order_number_text, total_amount_text, payment_status_text, delivery_time_text, order_status_text, canBeCancelled, product_category, cancelOrderType;
    String addressholder_name, address_line1, address_line2, address_landmark,address_mobile, address_pincode, address_type, address_state, address_district, orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_order);

        all_final_products_recyclerview = findViewById(R.id.all_ordering_products);
        total_price = findViewById(R.id.total_price);
        number_of_products = findViewById(R.id.number_of_products);
        amount_to_be_paid = findViewById(R.id.amount_to_be_paid);
        order_number = findViewById(R.id.order_number);
        payment_status_view = findViewById(R.id.payment_status);
        delivery_time= findViewById(R.id.delivery_time);
        order_status= findViewById(R.id.order_status);
        deliveryName = findViewById(R.id.addressholder_name);
        deliveryContact = findViewById(R.id.contact_number);
        deliveryLine1= findViewById(R.id.address_line1);
        deliveryLine2 = findViewById(R.id.address_line2);
        deliveryLandMark= findViewById(R.id.landmark_name);
        deliveryDistrict= findViewById(R.id.district_name);
        deliveryPincode= findViewById(R.id.pincode);
        deliveryState= findViewById(R.id.state_name);
        deliveryAddressType = findViewById(R.id.address_type);
        cancel_order_btn = findViewById(R.id.cancel_order_btn);



        sharedPrefDetails = new UserSharedPrefDetails(ViewSingleOrderActivity.this);
        userId = sharedPrefDetails.getUser_Id();

        products_data = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(ViewSingleOrderActivity.this, 1);
        all_final_products_recyclerview.setLayoutManager(gridLayoutManager);

        intent = getIntent();
        if(intent.hasExtra("Id"))
        {
            order_id = intent.getStringExtra("Id");
            sharedPrefDetails.setOrderId(order_id);
        }
        else
        {
            Log.i("intent", "onCreate: Nothing to fetch");
        }

        viewSingleOrderType = "view_single_order";
        get_single_order_worker = new BackgroundWorker(ViewSingleOrderActivity.this);
        get_single_order_worker.setAsyncTaskListener(ViewSingleOrderActivity.this);
        get_single_order_worker.execute(viewSingleOrderType, String.valueOf(userId), sharedPrefDetails.getOrderId());

        cancel_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelOrderType = "cancel_order";
                cancel_order_worker = new BackgroundWorker(ViewSingleOrderActivity.this);
                cancel_order_worker.setAsyncTaskListener(ViewSingleOrderActivity.this);
                cancel_order_worker.execute(cancelOrderType, String.valueOf(userId), sharedPrefDetails.getOrderId());

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
                        new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "OK", "INFORMATION SAYS:", HomeActivity.class);
                    } else {
                        new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Something Went Wrong, Please Try again..!!", "Lets Shop", "INFORMATION SAYS:", HomeActivity.class);
                    }
                } else {
                    if (jsonResult.has("data") && jsonResult.has("msg")) {
                        JSONObject wholeObject = jsonResult.getJSONObject("data");

                        orderDetailsObject = wholeObject.getJSONObject("order");
                        id = orderDetailsObject.getString("Id");
                        order_number_text = orderDetailsObject.getString("OrderNumber");
                        total_amount_text = orderDetailsObject.getString("TotalAmount");
                        payment_status_text= orderDetailsObject.getString("PaymentStatus");
                        delivery_time_text= orderDetailsObject.getString("DeliveryTime");
                        addressholder_name= orderDetailsObject.getString("DeliveryName");
                        address_line1= orderDetailsObject.getString("DeliveryLine1");
                        address_line2= orderDetailsObject.getString("DeliveryLine2");
                        address_landmark= orderDetailsObject.getString("DeliveryLandMark");
                        address_mobile= orderDetailsObject.getString("DeliveryMobile");
                        address_pincode= orderDetailsObject.getString("DeliveryPIN");
                        address_type= orderDetailsObject.getString("DeliveryAddressType");
                        address_district= orderDetailsObject.getString("DeliveryDistrict");
                        address_state= orderDetailsObject.getString("DeliveryState");
                        order_status_text= orderDetailsObject.getString("Status");
                        canBeCancelled= orderDetailsObject.getString("CanBeCancelled");


                        order_number.setText(order_number_text);
                        amount_to_be_paid.setText(total_amount_text);
                        payment_status_view.setText(payment_status_text);
                        delivery_time.setText(delivery_time_text);
                        deliveryName.setText(addressholder_name);
                        deliveryLine1.setText(address_line1);
                        deliveryLine2.setText(address_line2);
                        deliveryLandMark.setText(address_landmark);
                        deliveryContact.setText(address_mobile);
                        deliveryPincode.setText(address_pincode);
                        deliveryAddressType.setText(address_type);
                        deliveryDistrict.setText(address_district);
                        deliveryState.setText(address_state);
                        order_status.setText(order_status_text);
                        total_price.setText(total_amount_text);

                        if(canBeCancelled.equals("Yes"))
                        {
                            cancel_order_btn.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            cancel_order_btn.setVisibility(View.GONE);
                        }


                        JSONArray productsDetails = wholeObject.getJSONArray("items");
                        if (productsDetails.length() != 0) {
                            products_data.clear();
                            for (int i = 0; i < productsDetails.length(); i++) {
                                single_product= productsDetails.getJSONObject(i);
                                itemId = single_product.getString("Id");
                                productId = single_product.getString("ProductId");
                                orderId = single_product.getString("OrderId");
                                product_name = single_product.getString("Name");
                                product_price = single_product.getString("Price");
                                product_deposit = single_product.getString("Deposit");
                                productImg_link = single_product.getString("FilePath");
                                product_quantity = single_product.getString("Quantity");
                                product_isFresh = single_product.getString("IsFresh");
                                product_subTotal = single_product.getString("SubTotal");
                                product_category = single_product.getString("Category");

                                ProductsData productsData = new ProductsData(product_name, product_price, product_deposit, productImg_link, product_quantity, product_isFresh, product_subTotal);
                                products_data.add(productsData);
                            }

                            viewOrderItemsAdapter = new ViewOrderItemsAdapter(ViewSingleOrderActivity.this, products_data);
                            all_final_products_recyclerview.setAdapter(viewOrderItemsAdapter);
                            viewOrderItemsAdapter.notifyDataSetChanged();

                            number_of_products.setText(String.valueOf(viewOrderItemsAdapter.getItemCount()));
                        } else {
                            new CustomDialogs(ViewSingleOrderActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, "Something went Wrong please Try again..!!", "OK", "INFORMATION SAYS:", HomeActivity.class);
                        }
                    }
                    else if(jsonResult.has("msg") && !jsonResult.has("data") && !jsonResult.has("items"))
                    {
                        String message = jsonResult.getString("msg");
                        new CustomDialogs(ViewSingleOrderActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "INFORMATION SAYS:", HomeActivity.class);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(ViewSingleOrderActivity.this, "We didnt got any response...Ooopss Network Error!!", Toast.LENGTH_SHORT).show();
        }

    }
}
