package com.annotation.drinking_zone.CheckOutBillingStage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.annotation.drinking_zone.AdapterLayouts.AddressesRecyclerViewAdapter;
import com.annotation.drinking_zone.CCAvenueActivities.WebViewActivity;
import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AddressesData;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.AvenuesParams;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.ServiceUtility;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.annotation.drinking_zone.MyApp.getContext;

public class PayNowActivity extends AppCompatActivity implements AsyncTaskListener, View.OnClickListener {

    TextView amount_to_be_paid;
    Button confirm_booking;
    String payNowType, confirmBookingType, confirmOrderType, payment_status, payment_type, address_id;
    BackgroundWorker payNow, confirmBooking, confirmOrder, confirmCancel;
    UserSharedPrefDetails userSharedPrefDetails;
    int userId;

    JSONObject jsonResult;
    boolean success;
    String amount, cancelType, email, intent_order_id;
    Dialog alertDialog;
    Button alert_btn;
    TextView alert_title;
    TextView alert_message;
    ImageView alert_image;
    String addressId, addressholder_name, address_line1, address_line2, address_landmark,address_mobile, address_pincode, address_state, address_district, billing_address;
    JSONArray addressesList;
    JSONObject temp_jsonObject, single_address;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {

        alertDialog = new Dialog(PayNowActivity.this);
        alertDialog.setContentView(R.layout.epic_popup_positive);
//        closepositive = alertDialog.findViewById(R.id.closepostive);
        alert_btn = alertDialog.findViewById(R.id.pos_btn_ok);
        alert_title = alertDialog.findViewById(R.id.pos_pop_title);
        alert_message = alertDialog.findViewById(R.id.pos_pop_msg);
//        alert_card = alertDialog.findViewById(R.id.alert_card);
        alert_image = alertDialog.findViewById(R.id.alert_img);
        alert_title.setText("INFORMATION SAYS:");
        alert_title.setTextColor(this.getResources().getColor(R.color.colorRedDark));
        alert_image.setImageResource(R.drawable.ic_mood_bad_black_24dp);
        alert_message.setText("Are you sure to cancel the order\n Whole progress would be destroyed if you press back?");
        alert_message.setTextColor(this.getResources().getColor(R.color.colorRedDark));
        alert_btn.setText("Yes");
        alert_btn.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(R.color.colorRedDark)));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelType = "cancel_confirm";
                confirmCancel = new BackgroundWorker(PayNowActivity.this);
                confirmCancel.setAsyncTaskListener(PayNowActivity.this);
                confirmCancel.execute(cancelType, String.valueOf(userId), intent_order_id);
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);
        amount_to_be_paid = findViewById(R.id.amount_to_be_paid);
        confirm_booking = findViewById(R.id.confirm_order);

        userSharedPrefDetails = new UserSharedPrefDetails(this);
        userId = userSharedPrefDetails.getUser_Id();
        email = userSharedPrefDetails.getUser_email();

        Intent intent = getIntent();
        if(intent.hasExtra("reference"))
        {
            confirm_booking.setText("Confirm Booking");
        }
        else
        {
            confirm_booking.setText("Confirm Order");
        }

        if(intent.hasExtra("address_id_selected") && intent.hasExtra("payment_type"))
        {
            payment_type = intent.getStringExtra("payment_type");
            address_id = intent.getStringExtra("address_id_selected");
            intent_order_id = intent.getStringExtra("order_id");

            Log.i("IntentID", "onCreate: "+intent_order_id);

            String type_getAddress = "get_address";
            BackgroundWorker getAddress= new BackgroundWorker(PayNowActivity.this);
            getAddress.setAsyncTaskListener(this);
            getAddress.execute(type_getAddress, Integer.toString(userId), address_id);

            if(payment_type.equals("Cash"))
            {
                payment_status = "Unpaid";
            }
            else
            {
                payment_status = "";
            }
        }
        else
        {
            Log.i("Intents", "Intents didnt fetched properly");
        }

        payNowType = "pay_now";
        payNow = new BackgroundWorker(PayNowActivity.this);
        payNow.setAsyncTaskListener(this);
        payNow.execute(payNowType, String.valueOf(userId), intent_order_id);
//        confirm_booking.setVisibility(View.GONE);

        confirm_booking.setOnClickListener(this);
    }

    @Override
    public void updateResult(String result) {
        if(result != null) {
            try {
                jsonResult = new JSONObject(result);
//            int success = jsonResult.getInt("success");
                success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {
                    if (jsonResult.has("Amount")) {
                        amount = jsonResult.getString("Amount");
                        new CustomDialogs(PayNowActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Failed to Load Amount", "RETRY AGAIN", "INFORMATION SAYS:", HomeActivity.class);
                    }
                } else {
                    if (jsonResult.has("Amount")) {
                        String amtToBePaid = jsonResult.getString("Amount");
                        amount_to_be_paid.setText(amtToBePaid);
                    }
                    else if (jsonResult.has("msg")) {
                        String message = jsonResult.getString("msg");
                        new CustomDialogs(PayNowActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "INFORMATION SAYS:", HomeActivity.class);
                    }
                    else if(jsonResult.has("data")) {
                        addressesList = jsonResult.getJSONArray("data");
                        if (addressesList.length() != 0) {
                            for (int i = 0; i < addressesList.length(); i++) {
                                single_address = addressesList.getJSONObject(i);
                                addressId = single_address.getString("Id");
                                addressholder_name = single_address.getString("Name");
                                address_line1 = single_address.getString("Line1");
                                address_line2 = single_address.getString("Line2");
                                address_landmark = single_address.getString("Landmark");
                                address_mobile = single_address.getString("Mobile");
                                address_pincode = single_address.getString("Pin");
                                address_state = single_address.getString("State");
                                address_district = single_address.getString("District");

                                billing_address = address_line1+", "+address_line2+", "+address_landmark;
                            }
                        }

                    }
                    else
                    {
                        new CustomDialogs(PayNowActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, "Something went Wrong", "OK", "INFORMATION SAYS:", HomeActivity.class);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "We didnt got any response...Ooopss Network Error!!", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if(v.equals(confirm_booking))
        {
            if(payment_status.equals("Unpaid"))
            {
                if(confirm_booking.getText().equals("Confirm Booking"))
                {
                    confirmBookingType = "confirm_booking";
                    confirmBooking = new BackgroundWorker(PayNowActivity.this);
                    confirmBooking.setAsyncTaskListener(PayNowActivity.this);
                    confirmBooking.execute(confirmBookingType, String.valueOf(userId), payment_status, intent_order_id);
                    Log.i("Conf_booking", "onClick: ConfirmBooking Clicked_ cash on delivery");
                }
                else if(confirm_booking.getText().equals("Confirm Order"))
                {
                    confirmOrderType = "confirm_order";
                    confirmOrder = new BackgroundWorker(PayNowActivity.this);
                    confirmOrder.setAsyncTaskListener(PayNowActivity.this);
                    confirmOrder.execute(confirmOrderType, String.valueOf(userId), payment_status, intent_order_id);
                    Log.i("Conf_order", "onClick: ConfirmOrder Clicked_ cash on delivery");
                }
            }
            else
            {
                alertDialog = new Dialog(PayNowActivity.this);
                alertDialog.setContentView(R.layout.epic_popup_positive);
                alert_btn = alertDialog.findViewById(R.id.pos_btn_ok);
                alert_title = alertDialog.findViewById(R.id.pos_pop_title);
                alert_message = alertDialog.findViewById(R.id.pos_pop_msg);
                alert_image = alertDialog.findViewById(R.id.alert_img);
                alert_image.setVisibility(View.GONE);
                alert_title.setText("INFORMATION SAYS:");
                alert_title.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
                alert_message.setText("Are you sure to pay now?");
                alert_message.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
                alert_btn.setText("Yes");
                alert_btn.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(R.color.colorPrimaryDark)));
                alertDialog.setCancelable(true);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                alert_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
//                        int randomNum = ServiceUtility.randInt(0, 9999999);
//                        order_id = Integer.parseInt(intent_order_id);
                        Log.i("orderId", "onClick: "+intent_order_id);
                        String redirectURL = "http://drinkingzone.in/cc_av/ccavResponseHandler.php";
                        String cancelURL = "http://drinkingzone.in/cc_av/ccavResponseHandler.php";
                        String RSAkeyURL = "http://drinkingzone.in/cc_av/GetRSA.php";
                        Intent intent = new Intent(PayNowActivity.this, WebViewActivity.class);
                        intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull("AVKB91HC07BB06BKBB").toString());
                        intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull("242670").toString());
                        intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(intent_order_id).toString().trim());
                        intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull("INR").toString().trim());
                        intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount_to_be_paid.getText()).toString().trim());
//                        intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull("1").toString().trim());
                        intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectURL).toString());
                        intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelURL).toString());
                        intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(RSAkeyURL).toString());
                        intent.putExtra(AvenuesParams.BILLING_NAME, ServiceUtility.chkNull(addressholder_name).toString());
                        intent.putExtra(AvenuesParams.BILLING_ADDRESS, ServiceUtility.chkNull(billing_address).toString());
                        intent.putExtra(AvenuesParams.BILLING_CITY, ServiceUtility.chkNull(address_district).toString());
                        intent.putExtra(AvenuesParams.BILLING_STATE, ServiceUtility.chkNull(address_state).toString());
                        intent.putExtra(AvenuesParams.BILLING_COUNTRY, ServiceUtility.chkNull("India").toString());
                        intent.putExtra(AvenuesParams.BILLING_ZIP, ServiceUtility.chkNull(address_pincode).toString());
                        intent.putExtra(AvenuesParams.BILLING_TEL, ServiceUtility.chkNull(address_mobile).toString());
                        intent.putExtra(AvenuesParams.BILLING_EMAIL, ServiceUtility.chkNull(email).toString());
                        intent.putExtra(AvenuesParams.DELIVERY_NAME, ServiceUtility.chkNull(addressholder_name).toString());
                        intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, ServiceUtility.chkNull(billing_address).toString());
                        intent.putExtra(AvenuesParams.DELIVERY_CITY, ServiceUtility.chkNull(address_district).toString());
                        intent.putExtra(AvenuesParams.DELIVERY_STATE, ServiceUtility.chkNull(address_state).toString());
                        intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, ServiceUtility.chkNull("India").toString());
                        intent.putExtra(AvenuesParams.DELIVERY_ZIP, ServiceUtility.chkNull(address_pincode).toString());
                        intent.putExtra(AvenuesParams.DELIVERY_TEL, ServiceUtility.chkNull(address_mobile).toString());
                        if(confirm_booking.getText().equals("Confirm Booking"))
                        {
                            intent.putExtra(AvenuesParams.BOOKING_TYPE, "Booking");
                            Log.i("bookingTypeBook", "onCreate: This is Set");
                        }
                        else if(confirm_booking.getText().equals("Confirm Order"))
                        {
                            intent.putExtra(AvenuesParams.BOOKING_TYPE, "Order");
                            Log.i("bookingTypeOrd", "onCreate: This is set");
                        }
                        startActivity(intent);
                    }
                });
            }

        }
    }
}
