package com.annotation.drinking_zone.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.annotation.drinking_zone.R;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class BackgroundWorker extends AsyncTask<String, Void, String> {

    private Context context;
    private AsyncTaskListener asyncTaskListener;
    Dialog alertDialog;
    TextView alert_title;

    public BackgroundWorker(Context ctx)
    {
        this.context = ctx;
    }

    public void setAsyncTaskListener(AsyncTaskListener listener){
        this.asyncTaskListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String post_data;
        String api_key = "9vmeprm5n2ap1@w52019";
        String main_domain_url = "http://drinkingzone.in/Mobile/";
        String login_api = "user_login_check";
        String registration_api = "pre_register_user";
        String otp_api = "verify_otp";
        String get_all_products_api = "get_all_products";
        String forgot_password_api = "forgot_password";
        String reset_password_api = "reset_password";
        String change_password_api = "change_password";
        String update_cart_api = "update_cart";
        String view_cart_api = "view_cart";
        String delete_Cart_api = "delete_cart";
        String get_states_api = "get_states";
        String get_district_api = "get_districts";
        String update_address_api = "update_address";
        String get_address_api = "get_address";
        String delete_address_api = "delete_address";
        String place_order_api = "place_order";
        String get_bookable_products_api = "get_bookable_products";
        String get_price_chart_api = "get_price_chart";
        String get_book_now_api = "book_now";
        String get_pay_now_api = "pay_now";
        String get_confirm_booking_api = "confirm_booking";
        String get_confirm_order_api = "confirm_order";
        String get_view_bookings_api = "view_bookings";
        String get_get_my_wallet_api = "get_my_wallet";
        String get_view_orders_api = "view_orders";
        String get_cancel_confirm_api = "cancel_confirm";
        String get_cancel_order_api = "cancel_order";
        String get_view_single_order_api = "view_single_order";
        String get_check_update_api = "check_update";
        String get_update_profile_api = "update_profile";
        String api_url = main_domain_url+type;
        try {
            URL url = new URL(api_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            if (type.equals(login_api))
            {
                String email = params[1];
                String password = params[2];
                String deviceId = params[3];

                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("Email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"
                        +URLEncoder.encode("DeviceId", "UTF-8")+"="+URLEncoder.encode(deviceId, "UTF-8")+"&"
                        +URLEncoder.encode("Password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(registration_api))
            {
                String firstname = params[1];
                String lastname = params[2];
                String email = params[3];
                String password = params[4];
                String contact = params[5];
                String referralCode = params[6];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("FirstName", "UTF-8")+"="+URLEncoder.encode(firstname, "UTF-8")+"&"
                        +URLEncoder.encode("LastName", "UTF-8")+"="+URLEncoder.encode(lastname, "UTF-8")+"&"
                        +URLEncoder.encode("Password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"
                        +URLEncoder.encode("Mobile", "UTF-8")+"="+URLEncoder.encode(contact, "UTF-8")+"&"
                        +URLEncoder.encode("Email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"
                        +URLEncoder.encode("ReferredBy", "UTF-8")+"="+URLEncoder.encode(referralCode, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(otp_api))
            {
                String OTP = params[1];
                String mobile = params[2];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("Mobile", "UTF-8")+"="+URLEncoder.encode(mobile, "UTF-8")+"&"
                        +URLEncoder.encode("OTP", "UTF-8")+"="+URLEncoder.encode(OTP, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if (type.equals(get_all_products_api))
            {
                String category = params[1];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("CategoryId", "UTF-8")+"="+URLEncoder.encode(category, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(forgot_password_api))
            {
                String contact = params[1];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("Mobile", "UTF-8")+"="+URLEncoder.encode(contact, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(reset_password_api))
            {
                String contact = params[1];
                String OTP = params[2];
                String newPassword = params[3];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("Mobile", "UTF-8")+"="+URLEncoder.encode(contact, "UTF-8")+"&"
                        +URLEncoder.encode("OTP", "UTF-8")+"="+URLEncoder.encode(OTP, "UTF-8")+"&"
                        +URLEncoder.encode("Password", "UTF-8")+"="+URLEncoder.encode(newPassword, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(change_password_api))
            {
                String userId = params[1];
                String currentPassword = params[2];
                String newPassword = params[3];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8")+"&"
                        +URLEncoder.encode("CurrentPassword", "UTF-8")+"="+URLEncoder.encode(currentPassword, "UTF-8")+"&"
                        +URLEncoder.encode("NewPassword", "UTF-8")+"="+URLEncoder.encode(newPassword, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(get_check_update_api))
            {
                post_data = URLEncoder.encode("api", "UTF-8")+"="+ URLEncoder.encode(api_key, "UTF-8");

                bufferedWriter.write(post_data);
            }
            else if(type.equals(update_cart_api))
            {
                String cartId = params[1];
                String userId = params[2];
                String productId = params[3];
                String quantity = params[4];
                String isFresh = params[5];
                String deviceId = params[6];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("Id", "UTF-8")+"="+URLEncoder.encode(cartId, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8")+"&"
                        +URLEncoder.encode("ProductId", "UTF-8")+"="+URLEncoder.encode(productId, "UTF-8")+"&"
                        +URLEncoder.encode("Quantity", "UTF-8")+"="+URLEncoder.encode(quantity, "UTF-8")+"&"
                        +URLEncoder.encode("DeviceId", "UTF-8")+"="+URLEncoder.encode(deviceId, "UTF-8")+"&"
                        +URLEncoder.encode("IsFresh", "UTF-8")+"="+URLEncoder.encode(isFresh, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(view_cart_api))
            {
                String userId = params[1];
                String deviceId = params[2];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("DeviceId", "UTF-8")+"="+URLEncoder.encode(deviceId, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(delete_address_api))
            {
                String userId = params[1];
                String addressId = params[2];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("Id", "UTF-8")+"="+URLEncoder.encode(addressId, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if (type.equals(get_states_api))
            {
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if (type.equals(get_district_api))
            {
                String state_id = params[1];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("StateId", "UTF-8")+"="+URLEncoder.encode(state_id, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(update_address_api))
            {
                String addressId = params[1];
                String userId = params[2];
                String mobile = params[3];
                String name = params[4];
                String line1 = params[5];
                String line2 = params[6];
                String landmark = params[7];
                String pincode = params[8];
                String deliveryType = params[9];
                String stateId = params[10];
                String districtId = params[11];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("Id", "UTF-8")+"="+URLEncoder.encode(addressId, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId    , "UTF-8")+"&"
                        +URLEncoder.encode("Mobile", "UTF-8")+"="+URLEncoder.encode(mobile, "UTF-8")+"&"
                        +URLEncoder.encode("Name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"
                        +URLEncoder.encode("Line1", "UTF-8")+"="+URLEncoder.encode(line1, "UTF-8")+"&"
                        +URLEncoder.encode("Line2", "UTF-8")+"="+URLEncoder.encode(line2, "UTF-8")+"&"
                        +URLEncoder.encode("Landmark", "UTF-8")+"="+URLEncoder.encode(landmark, "UTF-8")+"&"
                        +URLEncoder.encode("Pin", "UTF-8")+"="+URLEncoder.encode(pincode, "UTF-8")+"&"
                        +URLEncoder.encode("AddressType", "UTF-8")+"="+URLEncoder.encode(deliveryType, "UTF-8")+"&"
                        +URLEncoder.encode("StateId", "UTF-8")+"="+URLEncoder.encode(stateId, "UTF-8")+"&"
                        +URLEncoder.encode("DistrictId", "UTF-8")+"="+URLEncoder.encode(districtId, "UTF-8");

                bufferedWriter.write(post_data);
            }
            else if (type.equals(get_address_api))
            {
                String address_id = params[2];
                String user_id = params[1];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("Id", "UTF-8")+"="+URLEncoder.encode(address_id, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(user_id, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(delete_Cart_api))
            {
                String cid = params[1];
                String userId = params[2];
                String deviceId = params[3];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("Id", "UTF-8")+"="+URLEncoder.encode(cid, "UTF-8")+"&"
                        +URLEncoder.encode("DeviceId", "UTF-8")+"="+URLEncoder.encode(deviceId, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(get_cancel_order_api))
            {
                String userId = params[1];
                String orderId = params[2];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("Id", "UTF-8")+"="+URLEncoder.encode(orderId, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(get_view_single_order_api))
            {
                String userId = params[1];
                String orderId = params[2];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("Id", "UTF-8")+"="+URLEncoder.encode(orderId, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(place_order_api))
            {
                String userId = params[1];
                String isQuick = params[2];
                String dateTimeSelected = params[3];
                String addressId = params[4];
                String useWalletCash = params[5];
                String deviceId = params[6];

                Log.i("bg data received", "doInBackground: "+userId+" "+isQuick+" "+dateTimeSelected+" "+addressId+" "+useWalletCash);
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8")+"&"
                        +URLEncoder.encode("IsQuick", "UTF-8")+"="+URLEncoder.encode(isQuick, "UTF-8")+"&"
                        +URLEncoder.encode("DeliveryTime", "UTF-8")+"="+URLEncoder.encode(dateTimeSelected, "UTF-8")+"&"
                        +URLEncoder.encode("AddressId", "UTF-8")+"="+URLEncoder.encode(addressId, "UTF-8")+"&"
                        +URLEncoder.encode("DeviceId", "UTF-8")+"="+URLEncoder.encode(deviceId, "UTF-8")+"&"
                        +URLEncoder.encode("UseWalletCash", "UTF-8")+"="+URLEncoder.encode(useWalletCash, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if (type.equals(get_bookable_products_api))
            {
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if (type.equals(get_price_chart_api))
            {
                String product_id = params[1];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("ProductId", "UTF-8")+"="+URLEncoder.encode(product_id, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(get_book_now_api))
            {
                String userId = params[1];
//                String paymentMode = params[2];
                String quantity = params[2];
                String productId = params[3];
                String addressId = params[4];
                String dateTimeSelected = params[5];
                String isFresh = params[6];
                String useWalletCash = params[7];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8")+"&"
                        +URLEncoder.encode("Quantity", "UTF-8")+"="+URLEncoder.encode(quantity, "UTF-8")+"&"
                        +URLEncoder.encode("ProductId", "UTF-8")+"="+URLEncoder.encode(productId, "UTF-8")+"&"
                        +URLEncoder.encode("AddressId", "UTF-8")+"="+URLEncoder.encode(addressId, "UTF-8")+"&"
                        +URLEncoder.encode("StartDate", "UTF-8")+"="+URLEncoder.encode(dateTimeSelected, "UTF-8")+"&"
                        +URLEncoder.encode("IsFresh", "UTF-8")+"="+URLEncoder.encode(isFresh, "UTF-8")+"&"
                        +URLEncoder.encode("UseWalletCash", "UTF-8")+"="+URLEncoder.encode(useWalletCash, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if (type.equals(get_pay_now_api))
            {
                String userId = params[1];
                String orderId = params[2];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("MyId", "UTF-8")+"="+URLEncoder.encode(orderId, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(get_confirm_booking_api))
            {
                String userId = params[1];
                String paymentMode = params[2];
                String orderId = params[3];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8")+"&"
                        +URLEncoder.encode("MyId", "UTF-8")+"="+URLEncoder.encode(orderId, "UTF-8")+"&"
                        +URLEncoder.encode("PaymentStatus", "UTF-8")+"="+URLEncoder.encode(paymentMode, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(get_confirm_order_api))
            {
                String userId = params[1];
                String paymentMode = params[2];
                String orderId = params[3];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8")+"&"
                        +URLEncoder.encode("MyId", "UTF-8")+"="+URLEncoder.encode(orderId, "UTF-8")+"&"
                        +URLEncoder.encode("PaymentStatus", "UTF-8")+"="+URLEncoder.encode(paymentMode, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(get_view_bookings_api))
            {
                String userId = params[1];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(get_view_orders_api))
            {
                String userId = params[1];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(get_get_my_wallet_api))
            {
                String userId = params[1];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(get_cancel_confirm_api))
            {
                String userId = params[1];
                String orderId = params[2];
                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("MyId", "UTF-8")+"="+URLEncoder.encode(orderId, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8");
                bufferedWriter.write(post_data);
            }
            else if(type.equals(get_update_profile_api))
            {
                String userId = params[1];
                String firstname = params[2];
                String lastname = params[3];

                post_data = URLEncoder.encode("api", "UTF-8")+"="+URLEncoder.encode(api_key, "UTF-8")+"&"
                        +URLEncoder.encode("FirstName", "UTF-8")+"="+URLEncoder.encode(firstname, "UTF-8")+"&"
                        +URLEncoder.encode("LastName", "UTF-8")+"="+URLEncoder.encode(lastname, "UTF-8")+"&"
                        +URLEncoder.encode("UserId", "UTF-8")+"="+URLEncoder.encode(userId, "UTF-8");
                bufferedWriter.write(post_data);
            }

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
            String result = "";
            String line ="";
            while((line = bufferedReader.readLine()) != null)
            {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new Dialog((Activity)context);
        alertDialog.setContentView(R.layout.progressbar_layout);
        alert_title = alertDialog.findViewById(R.id.title);
        alert_title.setText("Please wait till it loads");
//        progressBar.setIndeterminate(true);
        alert_title.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

//        progressBar.setBackgroundColor(context.getResources().getColor(R.color.colorgreenLight));
    }

    @Override
    protected void onPostExecute(String result) {

        alertDialog.dismiss();
        Log.i("####result", "onPostExecute: "+result);
        asyncTaskListener.updateResult(result);
    }
}
