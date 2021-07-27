package com.annotation.drinking_zone.CheckOutBillingStage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.annotation.drinking_zone.Login.SignUpActivity;
import com.annotation.drinking_zone.ProductRelated.SingleProductActivity;
import com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM;
import com.bumptech.glide.Glide;
import com.annotation.drinking_zone.AdapterLayouts.AddressesRecyclerViewAdapter;
import com.annotation.drinking_zone.AdapterLayouts.FinalProductsRecyclerViewAdapter;
import com.annotation.drinking_zone.AddressRelated.NewAddressWhileOrderingActivity;
import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AddressesData;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.RecyclerViewClickHandler;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.annotation.drinking_zone.MyApp.getContext;

public class PlaceOrderActivity extends AppCompatActivity implements AsyncTaskListener, RecyclerViewClickHandler, View.OnClickListener {

    RecyclerView address_recycler, address_list, all_final_products;
    LinearLayout delivery_selection_layout, address_recycler_displayLayout, delivery_types_layout, /*is_quick_layout,*/ use_wallet_cash_layout, deposit_layout;
    Button change_address_btn, proceed_to_pay_btn, show_datepicker, show_timepicker;
    UserSharedPrefDetails sharedPrefDetails;
    CardView booked_product, no_address_display, alert_no_address_display;
    private GridLayoutManager gridLayoutManager, gridLayoutManager2;
    private AddressesRecyclerViewAdapter addressesRecyclerViewAdapter, addressesRecyclerViewAdapter1;
    private List<AddressesData> addressesDataList, firstAddressList;
    private List<ProductsData> products_data;
    BackgroundWorker get_all_products, place_order, book_now;
    TextInputLayout delivery_date;
    TextView total_price, number_of_products, amount_to_be_paid, quick_disclaimer;
    TextView booking_productName, booking_product_deposit, booking_product_isFresh, booking_product_quantity, booking_subTotal;
    ImageView product_image;
    String bookingProductId, bookingProductName, bookingImgUrl, bookingProductDeposit, bookingIsfresh, bookingProductQuantity, bookingProductPrice, bookingProductSubTotal, bookingProductEndDate, bookingProductAddressId, payment_status;
    int bookingHomePrice, bookingOfficePrice, product_category_reference;
    private FinalProductsRecyclerViewAdapter finalProductsRecyclerViewAdapter;
    int userId, address_default_id;
    RadioGroup /*quick_delivery_selector,*/ delivery_type_selector, use_wallet_cash_selector, payment_mode_selector;
    RadioButton quick_delivery_type_yes, quick_delivery_type_no, delivery_type_advanced/*, delivery_type_not_advanced*/, use_wallet_cash_type_yes, use_wallet_cash_type_no, type_Cash, type_online;
    AddressesData addressesData, addressesData2;
    JSONObject jsonResult, single_address, temp_jsonObject, single_product;
    int quickDeliveryRadioButtonID, deliveryTypeRadioButtonID, useWalletCashRadioButtonID, paymentTypeRadioButtonID;
    String quickDeliveryRadioButtonText, deliveryTypeRadioButtonText, useWalletCashRadioButtonText, paymentTypeRadioButtonText;
    RadioButton quickDeliverySelectedRadioButton, deliveryTypeSelectedRadioButton, useWalletCashSelectedRadioButton, paymentTypeSelectedRadioButton;
    boolean success;
    JSONArray addressesList;
    Dialog alertDialog;
    int current_year, current_month, current_day, selectedHour, selectedMinute, seconds;
    int selectedYear, selectedMonth, selectedDay, renewalDay, renewalMonth;
    int first_address;
    Date current_date;
    String date_time_selected;
    private Calendar calendar;
    private TimePicker timePicker1;
    private DatePickerDialog.OnDateSetListener myDateListener;
    String cartId, productId, product_name, product_price, product_deposit, productImg_link, product_quantity, product_status, upto_quantity, product_isFresh, product_subTotal;
    String addressId, addressholder_name, address_line1, address_line2, address_landmark,address_mobile, address_pincode, address_type, address_state, address_stateId, address_district, address_districtId, address_status, message;
    String deviceToken = "";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        address_recycler = findViewById(R.id.address_recycler);
        change_address_btn = findViewById(R.id.change_address_btn);
        all_final_products = findViewById(R.id.all_ordering_products);
        no_address_display = findViewById(R.id.no_address_diplay);
        total_price = findViewById(R.id.total_price);
        number_of_products = findViewById(R.id.number_of_products);
        amount_to_be_paid = findViewById(R.id.amount_to_be_paid);
//        quick_delivery_selector = findViewById(R.id.quick_delivery_selector);
        delivery_type_selector = findViewById(R.id.delivery_type_selector);
        use_wallet_cash_selector = findViewById(R.id.use_wallet_cash_selector);
        quick_delivery_type_yes= findViewById(R.id.quick_delivery_type_yes);
        quick_delivery_type_no= findViewById(R.id.quick_delivery_type_no);
        use_wallet_cash_type_yes = findViewById(R.id.use_wallet_cash_yes);
        use_wallet_cash_type_no = findViewById(R.id.use_wallet_cash_no);
        delivery_type_advanced = findViewById(R.id.delivery_type_advanced);
//        delivery_type_not_advanced = findViewById(R.id.delivery_type_not_advanced);
        proceed_to_pay_btn = findViewById(R.id.proceed_to_checkout_btn);
        address_recycler_displayLayout = findViewById(R.id.address_recycler_layout);
        delivery_types_layout = findViewById(R.id.delivery_types_layout);
        delivery_date = findViewById(R.id.delivery_date);
        timePicker1 = findViewById(R.id.timePicker1);
        delivery_selection_layout = findViewById(R.id.delivery_selection_layout);
        show_datepicker = findViewById(R.id.show_datepicker);
        show_timepicker = findViewById(R.id.show_timepicker);
        booked_product = findViewById(R.id.booked_product);
        booking_productName = findViewById(R.id.product_name);
        booking_product_deposit = findViewById(R.id.product_deposit);
        booking_product_isFresh = findViewById(R.id.isfresh_type);
        booking_product_quantity = findViewById(R.id.quantity);
        booking_subTotal = findViewById(R.id.sub_total);
        product_image = findViewById(R.id.product_image);
//        is_quick_layout = findViewById(R.id.is_quick_layout);
        quick_disclaimer = findViewById(R.id.quick_disclaimer);
        use_wallet_cash_layout = findViewById(R.id.use_wallet_cash_layout);
        deposit_layout = findViewById(R.id.deposit_layout);
        type_Cash = findViewById(R.id.type_cash);
        type_online = findViewById(R.id.type_online);
        payment_mode_selector = findViewById(R.id.payment_mode_selector);

        sharedPrefDetails = new UserSharedPrefDetails(PlaceOrderActivity.this);
        userId = sharedPrefDetails.getUser_Id();

        if(!SharedPreferenceManagerFCM.getInstance(PlaceOrderActivity.this).getToken().equals(""))
        {
            Log.i("tokenTestingIf", "onCreate: "+SharedPreferenceManagerFCM.getInstance(this).getToken());
            deviceToken = SharedPreferenceManagerFCM.getInstance(this).getToken(); // need to pass this token id while caling login api
            Log.i("DeviceTockenIf", "onCreate: "+deviceToken);
        }
        else
        {
            deviceToken = SharedPreferenceManagerFCM.getInstance(this).getToken();
            Log.i("tokenTestingIf", "onCreate: "+SharedPreferenceManagerFCM.getInstance(this).getToken());
            Log.i("DeviceokenElse", "onCreate: "+deviceToken);
        }

        addressesDataList = new ArrayList<>();
        firstAddressList = new ArrayList<>();
        products_data = new ArrayList<>();

        selectedDay = 0;
        selectedHour = 0;
        selectedMinute = 0;
        selectedYear = 0;
        selectedMonth = 0;
        date_time_selected = "";
        payment_status = "";

        current_date = (Date) Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = df.format(current_date);

        SimpleDateFormat currentYear = new SimpleDateFormat("yyyy");
        String formatedYear = currentYear.format(current_date);
        current_year = Integer.parseInt(formatedYear);

        SimpleDateFormat currentMonth = new SimpleDateFormat("MM");
        String formatedMonth = currentMonth.format(current_date);
        current_month = Integer.parseInt(formatedMonth)-1;

        SimpleDateFormat currentDay = new SimpleDateFormat("dd");
        String formatedDay = currentDay.format(current_date);
        current_day = Integer.parseInt(formatedDay)+1;

        Intent date_intent = getIntent();
        if(date_intent.hasExtra("end_date"))
        {
            bookingProductEndDate = date_intent.getStringExtra("end_date");
            try {

                SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
                Date ending_date = d.parse(bookingProductEndDate);

                String package_endDate = d.format(ending_date);
                String package_end_month = currentMonth.format(ending_date);
                String package_end_day = currentDay.format(ending_date);

                renewalDay = Integer.parseInt(package_end_day)+1;
                renewalMonth = Integer.parseInt(package_end_month)-1;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        gridLayoutManager = new GridLayoutManager(PlaceOrderActivity.this, 1);
        address_recycler.setLayoutManager(gridLayoutManager);

        gridLayoutManager2 = new GridLayoutManager(PlaceOrderActivity.this, 1);
        all_final_products.setLayoutManager(gridLayoutManager2);

        alertDialog = new Dialog(PlaceOrderActivity.this);
        address_default_id = 0;
        String type_getAddress = "get_address";

        Intent i = getIntent();
        if(i.hasExtra("address_id"))
        {
            bookingProductAddressId = i.getStringExtra("address_id");
            address_default_id = Integer.parseInt(bookingProductAddressId);
            BackgroundWorker getAddresses= new BackgroundWorker(PlaceOrderActivity.this);
            getAddresses.setAsyncTaskListener(this);
            getAddresses.execute(type_getAddress, Integer.toString(userId), Integer.toString(address_default_id));
        }
        else
        {
            BackgroundWorker getAddress= new BackgroundWorker(PlaceOrderActivity.this);
            getAddress.setAsyncTaskListener(this);
            getAddress.execute(type_getAddress, Integer.toString(userId), Integer.toString(address_default_id));
        }

        change_address_btn.setOnClickListener(this);
        proceed_to_pay_btn.setOnClickListener(this);
        delivery_type_advanced.setOnClickListener(this);
//        delivery_type_not_advanced.setOnClickListener(this);
        quick_delivery_type_yes.setOnClickListener(this);
        quick_delivery_type_no.setOnClickListener(this);
        use_wallet_cash_type_yes.setOnClickListener(this);
        use_wallet_cash_type_no.setOnClickListener(this);
        show_timepicker.setOnClickListener(this);
        show_datepicker.setOnClickListener(this);
        type_Cash.setOnClickListener(this);
        type_online.setOnClickListener(this);

        myDateListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                SimpleDateFormat currentSeconds = new SimpleDateFormat("ss");
                String formatedSeconds = currentSeconds.format(current_date);
                seconds = Integer.parseInt(formatedSeconds);

                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;

                Intent intent1 = getIntent();
                if(intent1.hasExtra("end_date"))
                {
                    if(selectedYear == current_year)
                    {
                        if(selectedMonth == renewalMonth)
                        {
                            if(selectedDay >= renewalDay)
                            {
                                delivery_date.setVisibility(View.VISIBLE);
                                delivery_date.getEditText().setEnabled(false);
                                if(show_timepicker.isShown())
                                {
                                    date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
                                    delivery_date.getEditText().setText(date_time_selected);
                                }
                                else
                                {
                                    date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
                                    delivery_date.getEditText().setText(date_time_selected);
                                }
                            }
                            else
                            {
                                new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Day. You cant select past or today's date for advance booking.", "OK", "INFORMATION SAYS:");
                            }
                        }
                        else if(selectedMonth > renewalMonth)
                        {
                            delivery_date.setVisibility(View.VISIBLE);
                            delivery_date.getEditText().setEnabled(false);
                            if(show_timepicker.isShown())
                            {
                                date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
                                delivery_date.getEditText().setText(date_time_selected);
                            }
                            else
                            {
                                date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
                                delivery_date.getEditText().setText(date_time_selected);
                            }
                        }
                        else
                        {
                            delivery_date.setVisibility(View.GONE);
                            new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Month. You cannot select past month", "OK", "INFORMATION SAYS:");
                        }
                    }
                    else if (selectedYear > current_year)
                    {
                        delivery_date.setVisibility(View.VISIBLE);
                        delivery_date.getEditText().setEnabled(false);
                        if(show_timepicker.isShown())
                        {
                            date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
                            delivery_date.getEditText().setText(date_time_selected);
                        }
                        else
                        {
                            date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
                            delivery_date.getEditText().setText(date_time_selected);
                        }
                    }
                    else
                    {
                        delivery_date.setVisibility(View.GONE);
                        new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Year. You cannot select past year", "OK", "INFORMATION SAYS:");
                    }
                }
                else {
                    if(selectedYear == current_year)
                    {
                        if(selectedMonth == current_month)
                        {
                            if(selectedDay >= current_day)
                            {
                                delivery_date.setVisibility(View.VISIBLE);
                                delivery_date.getEditText().setEnabled(false);
                                if(show_timepicker.isShown())
                                {
                                    date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
                                    delivery_date.getEditText().setText(date_time_selected);
                                }
                                else
                                {
                                    date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
                                    delivery_date.getEditText().setText(date_time_selected);
                                }
                            }
                            else
                            {
                                new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Day. You cant select past or today's date for advance booking.", "OK", "INFORMATION SAYS:");
                            }
                        }
                        else if(selectedMonth > current_month)
                        {
                            delivery_date.setVisibility(View.VISIBLE);
                            delivery_date.getEditText().setEnabled(false);
                            if(show_timepicker.isShown())
                            {
                                date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
                                delivery_date.getEditText().setText(date_time_selected);
                            }
                            else
                            {
                                date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
                                delivery_date.getEditText().setText(date_time_selected);
                            }
                        }
                        else
                        {
                            delivery_date.setVisibility(View.GONE);
                            new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Month. You cannot select past month", "OK", "INFORMATION SAYS:");
                        }
                    }
                    else if (selectedYear > current_year)
                    {
                        delivery_date.setVisibility(View.VISIBLE);
                        delivery_date.getEditText().setEnabled(false);
                        if(show_timepicker.isShown())
                        {
                            date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
                            delivery_date.getEditText().setText(date_time_selected);
                        }
                        else
                        {
                            date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
                            delivery_date.getEditText().setText(date_time_selected);
                        }
                    }
                    else
                    {
                        delivery_date.setVisibility(View.GONE);
                        new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Year. You cannot select past year", "OK", "INFORMATION SAYS:");
                    }
                }
            }
        };

        calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        timePicker1.setIs24HourView(true);

        timePicker1.setHour(hour);
        timePicker1.setMinute(min);
        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                SimpleDateFormat currentSeconds = new SimpleDateFormat("ss");
                String formatedSeconds = currentSeconds.format(current_date);
                seconds = Integer.parseInt(formatedSeconds);

                if(delivery_date.isShown())
                {
                    delivery_date.getEditText().setEnabled(false);

                    selectedHour = hourOfDay;
                    selectedMinute = minute;

                    date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
                    delivery_date.getEditText().setText(date_time_selected);
                }
                else
                {
                    new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select The Date First", "OK", "INFORMATION SAYS:");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        int backCount = fragmentManager.getBackStackEntryCount();
        if(backCount > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {

            Intent date_intent = getIntent();
            if(date_intent.hasExtra("end_date"))
            {
                return new DatePickerDialog(this, myDateListener, current_year, renewalMonth, renewalDay);
            }
            else
            {
                return new DatePickerDialog(this, myDateListener, current_year, current_month, current_day);
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void updateResult(String result) {

        if(result != null) {
            try {
                jsonResult = new JSONObject(result);
//            int success = jsonResult.getInt("success");
                success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {
                    delivery_date.setVisibility(View.GONE);
                    if (jsonResult.has("msg")) {
                        message = jsonResult.getString("msg");
                        new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "RETRY AGAIN", "INFORMATION SAYS:");
                    } else if (jsonResult.has("data")) {
                        address_recycler_displayLayout.setVisibility(View.GONE);
                        change_address_btn.setText("No Addresses Added Yet, Please Add New");

                        all_final_products.setVisibility(View.VISIBLE);
                        booked_product.setVisibility(View.GONE);
//                        delivery_types_layout.setVisibility(View.GONE);
                        delivery_selection_layout.setVisibility(View.GONE);
                        timePicker1.setVisibility(View.GONE);
                        String type_viewCart = "view_cart";
                        get_all_products = new BackgroundWorker(PlaceOrderActivity.this);
                        get_all_products.setAsyncTaskListener(this);
                        if(userId == 0)
                        {
                            get_all_products.execute(type_viewCart, "", deviceToken);
                        }
                        else
                        {
                            get_all_products.execute(type_viewCart, Integer.toString(userId), deviceToken);
                        }


                        Intent intent = getIntent();
                        if (intent.hasExtra("product_id") && intent.hasExtra("product_name") &&
                                intent.hasExtra("product_price") && intent.hasExtra("product_deposit")
                                && intent.hasExtra("product_image_link") && intent.hasExtra("isFresh")
                                && intent.hasExtra("product_quantity") && intent.hasExtra("product_category_reference")
                                && intent.hasExtra("product_total_priceHome") && intent.hasExtra("product_total_priceOffice")) {
                            all_final_products.setVisibility(View.GONE);
                            show_timepicker.setVisibility(View.GONE);
                            timePicker1.setVisibility(View.GONE);
                            delivery_selection_layout.setVisibility(View.GONE);

                            bookingImgUrl = intent.getStringExtra("product_image_link");
                            bookingProductId = intent.getStringExtra("product_id");
                            bookingProductName = intent.getStringExtra("product_name");
                            bookingProductPrice = intent.getStringExtra("product_price");
                            bookingProductDeposit = intent.getStringExtra("product_deposit");
                            bookingIsfresh = intent.getStringExtra("isFresh");
                            bookingProductQuantity = intent.getStringExtra("product_quantity");
                            product_category_reference = intent.getIntExtra("product_reference_category", -1);
                            bookingOfficePrice = intent.getIntExtra("product_total_priceOffice", 0);
                            bookingHomePrice = intent.getIntExtra("product_total_priceHome", 0);

                            Log.i("prices", "updateResult: Home Price: " + bookingHomePrice + " office price: " + bookingOfficePrice);

                            Glide.with(PlaceOrderActivity.this)
                                    .asBitmap().load(bookingImgUrl).into(product_image);
                            booking_product_quantity.setText(bookingProductQuantity);
                            booking_product_deposit.setText(bookingProductDeposit);
                            booking_productName.setText(bookingProductName);
                            booking_product_isFresh.setText(bookingIsfresh);
                            number_of_products.setText("1");

                            booking_subTotal.setText(Integer.toString(bookingHomePrice));
                            total_price.setText(Integer.toString(bookingHomePrice));
                            amount_to_be_paid.setText(Integer.toString(bookingHomePrice));
                        }
                    } else {
                        new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Something Went Wrong, Please try again later", "OK", "INFORMATION SAYS:", PlaceOrderActivity.class);
                    }
                } else {
                    if (jsonResult.has("msg")) {
                        String message = jsonResult.getString("msg");
                        new CustomDialogs(PlaceOrderActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "INFORMATION SAYS:", HomeActivity.class);
                    } else if (jsonResult.has("status") && !(jsonResult.has("data")) && jsonResult.has("OrderId")) {
                        String orderId = jsonResult.getString("OrderId");
                        Log.i("Place_orderID", "updateResult: "+orderId);
                        Intent intent1 = new Intent(PlaceOrderActivity.this, PayNowActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Intent intent = getIntent();
                        if (intent.hasExtra("product_category_reference")) {
                            product_category_reference = intent.getIntExtra("product_reference_category", -1);
                            intent1.putExtra("reference", product_category_reference);
                            intent1.putExtra("address_id_selected", Integer.toString(address_default_id));
                            intent1.putExtra("order_id", orderId);
                            intent1.putExtra("payment_type", payment_status);
                            Log.i("Intent_OID", "updateResult: "+orderId);
//                            Log.i("payment_type", "updateResult: "+payment_status);
//                            Log.i("address_id", "updateResult: "+address_default_id);
//                            Log.i("reference", "updateResult: "+product_category_reference);
                            startActivity(intent1);
                        } else {
                            intent1.putExtra("address_id_selected", Integer.toString(address_default_id));
                            intent1.putExtra("payment_type", payment_status);
                            intent1.putExtra("order_id", orderId);
                            Log.i("Intent_OID", "updateResult: "+orderId);
//                            Log.i("payment_type", "updateResult: "+payment_status);
//                            Log.i("address_id", "updateResult: "+address_default_id);
                            startActivity(intent1);
                        }

                    } else if (jsonResult.has("data")) {
                        address_recycler.setVisibility(View.VISIBLE);
//                    no_address_display.setVisibility(View.GONE);
                        addressesList = jsonResult.getJSONArray("data");
                        if (addressesList.length() != 0) {
                            products_data.clear();
                            delivery_date.setVisibility(View.GONE);
                            temp_jsonObject = addressesList.getJSONObject(0);
                            if (temp_jsonObject.has("Line1")) {
                                if (address_default_id == 0) {
                                    for (int i = 0; i < addressesList.length(); i++) {
                                        single_address = addressesList.getJSONObject(i);
                                        addressId = single_address.getString("Id");
                                        addressholder_name = single_address.getString("Name");
                                        address_line1 = single_address.getString("Line1");
                                        address_line2 = single_address.getString("Line2");
                                        address_landmark = single_address.getString("Landmark");
                                        address_mobile = single_address.getString("Mobile");
                                        address_pincode = single_address.getString("Pin");
                                        address_type = single_address.getString("AddressType");
                                        address_state = single_address.getString("State");
                                        address_stateId = single_address.getString("StateId");
                                        address_district = single_address.getString("District");
                                        address_districtId = single_address.getString("DistrictId");
                                        address_status = single_address.getString("Status");

                                        addressesData2 = new AddressesData(addressId, addressholder_name, address_line1, address_line2, address_landmark, address_mobile, address_pincode, address_type, address_state, address_stateId, address_district, address_districtId, address_status);
                                        addressesDataList.add(addressesData2);
                                    }

                                    firstAddressList.clear();
                                    Intent intent = getIntent();
                                    if (intent.hasExtra("end_date")) {
                                        bookingProductAddressId = intent.getStringExtra("address_id");
                                        Log.i("addressId", "updateResult: " + true);
                                        first_address = Integer.parseInt(bookingProductAddressId);
                                    } else {
                                        first_address = addressesList.length() - (addressesList.length() - 1);

                                    }

                                    for (int i = 0; i < first_address; i++) {
                                        single_address = addressesList.getJSONObject(i);
                                        addressId = single_address.getString("Id");
                                        addressholder_name = single_address.getString("Name");
                                        address_line1 = single_address.getString("Line1");
                                        address_line2 = single_address.getString("Line2");
                                        address_landmark = single_address.getString("Landmark");
                                        address_mobile = single_address.getString("Mobile");
                                        address_pincode = single_address.getString("Pin");
                                        address_type = single_address.getString("AddressType");
                                        address_state = single_address.getString("State");
                                        address_stateId = single_address.getString("StateId");
                                        address_district = single_address.getString("District");
                                        address_districtId = single_address.getString("DistrictId");
                                        address_status = single_address.getString("Status");

                                        addressesData = new AddressesData(addressId, addressholder_name, address_line1, address_line2, address_landmark, address_mobile, address_pincode, address_type, address_state, address_stateId, address_district, address_districtId, address_status);
                                        firstAddressList.add(addressesData);
                                    }
                                    addressesRecyclerViewAdapter = new AddressesRecyclerViewAdapter(PlaceOrderActivity.this, firstAddressList, this);
                                    address_recycler.setAdapter(addressesRecyclerViewAdapter);
                                } else {
                                    firstAddressList.clear();
                                    for (int i = 0; i < addressesList.length(); i++) {
                                        single_address = addressesList.getJSONObject(i);
                                        addressId = single_address.getString("Id");
                                        addressholder_name = single_address.getString("Name");
                                        address_line1 = single_address.getString("Line1");
                                        address_line2 = single_address.getString("Line2");
                                        address_landmark = single_address.getString("Landmark");
                                        address_mobile = single_address.getString("Mobile");
                                        address_pincode = single_address.getString("Pin");
                                        address_type = single_address.getString("AddressType");
                                        address_state = single_address.getString("State");
                                        address_stateId = single_address.getString("StateId");
                                        address_district = single_address.getString("District");
                                        address_districtId = single_address.getString("DistrictId");
                                        address_status = single_address.getString("Status");

                                        addressesData = new AddressesData(addressId, addressholder_name, address_line1, address_line2, address_landmark, address_mobile, address_pincode, address_type, address_state, address_stateId, address_district, address_districtId, address_status);
                                        firstAddressList.add(addressesData);
                                    }
                                    addressesRecyclerViewAdapter = new AddressesRecyclerViewAdapter(getContext(), firstAddressList, this);
                                    address_recycler.setAdapter(addressesRecyclerViewAdapter);
                                }

                                Intent intent = getIntent();
                                Intent renew_intent = getIntent();
                                if (intent.hasExtra("product_id") && intent.hasExtra("product_name") &&
                                        intent.hasExtra("product_price") && intent.hasExtra("product_deposit")
                                        && intent.hasExtra("product_image_link") && intent.hasExtra("isFresh")
                                        && intent.hasExtra("product_quantity") && intent.hasExtra("product_category_reference")
                                        && intent.hasExtra("product_total_priceHome") && intent.hasExtra("product_total_priceOffice")) {
                                    all_final_products.setVisibility(View.GONE);
                                    quick_delivery_type_yes.setVisibility(View.GONE);
                                    quick_disclaimer.setVisibility(View.GONE);
//                                    is_quick_layout.setVisibility(View.GONE);
                                    show_timepicker.setVisibility(View.GONE);
                                    timePicker1.setVisibility(View.GONE);
                                    delivery_selection_layout.setVisibility(View.GONE);

                                    bookingImgUrl = intent.getStringExtra("product_image_link");
                                    bookingProductId = intent.getStringExtra("product_id");
                                    bookingProductName = intent.getStringExtra("product_name");
                                    bookingProductPrice = intent.getStringExtra("product_price");
                                    bookingProductDeposit = intent.getStringExtra("product_deposit");
                                    bookingIsfresh = intent.getStringExtra("isFresh");
                                    bookingProductQuantity = intent.getStringExtra("product_quantity");
                                    product_category_reference = intent.getIntExtra("product_reference_category", -1);
                                    bookingOfficePrice = intent.getIntExtra("product_total_priceOffice", 0);
                                    bookingHomePrice = intent.getIntExtra("product_total_priceHome", 0);

                                    Log.i("Testing", "Home" + bookingHomePrice + " \n Office: " + bookingOfficePrice);
                                    Glide.with(PlaceOrderActivity.this)
                                            .asBitmap().load(bookingImgUrl).into(product_image);
                                    booking_product_quantity.setText(bookingProductQuantity);
                                    booking_product_deposit.setText(bookingProductDeposit);
                                    booking_productName.setText(bookingProductName);
                                    booking_product_isFresh.setText(bookingIsfresh);
                                    number_of_products.setText("1");

                                    if(firstAddressList.size() > 0) {
                                        String adType = firstAddressList.get(0).getAddressType();
                                        if (adType.equals("Home")) {
                                            booking_subTotal.setText(Integer.toString(bookingHomePrice));
                                            total_price.setText(Integer.toString(bookingHomePrice));
                                            amount_to_be_paid.setText(Integer.toString(bookingHomePrice));
                                        } else if (adType.equals("Office")) {
                                            booking_subTotal.setText(Integer.toString(bookingOfficePrice));
                                            total_price.setText(Integer.toString(bookingOfficePrice));
                                            amount_to_be_paid.setText(Integer.toString(bookingOfficePrice));
                                        }
                                    } else {
                                        Toast.makeText(this, "Something went Wrong while selecting address type", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (renew_intent.hasExtra("Id") && renew_intent.hasExtra("product_name") &&
                                        renew_intent.hasExtra("total_amount") && renew_intent.hasExtra("product_deposit")
                                        && renew_intent.hasExtra("productImg_link") && renew_intent.hasExtra("isFresh")
                                        && renew_intent.hasExtra("product_quantity") && renew_intent.hasExtra("product_subTotal")
                                        && renew_intent.hasExtra("end_date") && renew_intent.hasExtra("address_id")) {
                                    all_final_products.setVisibility(View.GONE);
                                    quick_delivery_type_yes.setVisibility(View.GONE);
                                    quick_disclaimer.setVisibility(View.GONE);
//                                    is_quick_layout.setVisibility(View.GONE);
                                    show_timepicker.setVisibility(View.GONE);
                                    timePicker1.setVisibility(View.GONE);
                                    delivery_selection_layout.setVisibility(View.GONE);

                                    bookingImgUrl = intent.getStringExtra("productImg_link");
                                    bookingProductId = intent.getStringExtra("Id");
                                    bookingProductName = intent.getStringExtra("product_name");
                                    bookingProductPrice = intent.getStringExtra("total_amount");
                                    bookingProductDeposit = intent.getStringExtra("product_deposit");
                                    bookingIsfresh = intent.getStringExtra("isFresh");
                                    bookingProductQuantity = intent.getStringExtra("product_quantity");
                                    bookingProductSubTotal = intent.getStringExtra("product_subTotal");
                                    bookingProductEndDate = intent.getStringExtra("end_date");
                                    bookingProductAddressId = intent.getStringExtra("address_id");

                                    Glide.with(PlaceOrderActivity.this)
                                            .asBitmap().load(bookingImgUrl).into(product_image);
                                    booking_product_quantity.setText(bookingProductQuantity);
                                    booking_productName.setText(bookingProductName);
                                    booking_product_isFresh.setText(bookingIsfresh);
                                    number_of_products.setText("1");
                                    booking_subTotal.setText(bookingProductPrice);
                                    total_price.setText(bookingProductPrice);
                                    amount_to_be_paid.setText(bookingProductSubTotal);
                                    if (bookingIsfresh.equals("Yes")) {
                                        deposit_layout.setVisibility(View.VISIBLE);
                                        booking_product_deposit.setText(bookingProductDeposit);
                                    } else if (bookingIsfresh.equals("No")) {
                                        deposit_layout.setVisibility(View.GONE);
                                    }

                                } else {
                                    all_final_products.setVisibility(View.VISIBLE);
                                    booked_product.setVisibility(View.GONE);
//                                    delivery_types_layout.setVisibility(View.GONE);
                                    delivery_selection_layout.setVisibility(View.GONE);
                                    timePicker1.setVisibility(View.GONE);
                                    String type_viewCart = "view_cart";
                                    get_all_products = new BackgroundWorker(PlaceOrderActivity.this);
                                    get_all_products.setAsyncTaskListener(this);
                                    if(userId == 0)
                                    {
                                        get_all_products.execute(type_viewCart, "", deviceToken);
                                    }
                                    else
                                    {
                                        get_all_products.execute(type_viewCart, Integer.toString(userId), deviceToken);
                                    }
                                }
                            } else if (temp_jsonObject.has("ProductId")) {

                                for (int i = 0; i < addressesList.length(); i++) {
                                    single_product = addressesList.getJSONObject(i);
                                    cartId = single_product.getString("Id");
                                    productId = single_product.getString("ProductId");
                                    product_name = single_product.getString("Name");
                                    product_price = single_product.getString("Price");
                                    product_deposit = single_product.getString("Deposit");
                                    productImg_link = single_product.getString("FilePath");
                                    product_quantity = single_product.getString("Quantity");
                                    product_status = single_product.getString("ProductStatus");
                                    upto_quantity = single_product.getString("UptoQuantity");
                                    product_isFresh = single_product.getString("IsFresh");
                                    product_subTotal = single_product.getString("SubTotal");

                                    ProductsData productsData = new ProductsData(cartId, productId, product_name, product_price, product_deposit, productImg_link, product_quantity, product_isFresh, upto_quantity, product_subTotal, product_status);
                                    products_data.add(productsData);
                                }

                                String product_total = jsonResult.getString("Total");
                                total_price.setText(product_total);

                                finalProductsRecyclerViewAdapter = new FinalProductsRecyclerViewAdapter(PlaceOrderActivity.this, products_data);
                                all_final_products.setAdapter(finalProductsRecyclerViewAdapter);
                                finalProductsRecyclerViewAdapter.notifyDataSetChanged();

                                amount_to_be_paid.setText(product_total);
                                number_of_products.setText(String.valueOf(finalProductsRecyclerViewAdapter.getItemCount()));
                            }
                        }
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

    @Override
    public void onDeleteClicked(int position) {

        if(alertDialog.isShowing())
        {
            alertDialog.dismiss();
            String type = "get_address";
            BackgroundWorker getAddresses= new BackgroundWorker(PlaceOrderActivity.this);
            getAddresses.setAsyncTaskListener(this);
            address_default_id = position;
            getAddresses.execute(type, Integer.toString(userId), Integer.toString(address_default_id));
        }
        else
        {
            Toast.makeText(PlaceOrderActivity.this, "Please click on Change Button for changing Delivery Address", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onQuantityUpdated(int position, String quantity) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        if(v.equals(change_address_btn))
        {
//            Log.i("size", ""+addressesDataList.size());
            if(addressesDataList.size() == 0)
            {
                Intent intent = new Intent(PlaceOrderActivity.this, NewAddressWhileOrderingActivity.class);
                startActivity(intent);
            }
            else {
                alertDialog = new Dialog(PlaceOrderActivity.this);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(R.layout.address_selection_layout);
                address_list = alertDialog.findViewById(R.id.address_list);
                alert_no_address_display = alertDialog.findViewById(R.id.no_address_diplay);
                addressesRecyclerViewAdapter1 = new AddressesRecyclerViewAdapter(PlaceOrderActivity.this, addressesDataList, PlaceOrderActivity.this);
                address_list.setAdapter(addressesRecyclerViewAdapter1);
                address_list.setLayoutManager(new LinearLayoutManager(PlaceOrderActivity.this));
                addressesRecyclerViewAdapter1.notifyDataSetChanged();
                Intent i = getIntent();
                if(i.hasExtra("end_date")) {
                    new CustomDialogs(PlaceOrderActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, "The price may vary if, address changes", "OK", "INFORMATION SAYS:");
                    alertDialog.show();
                }
                else {
                    alertDialog.show();
                }
            }
        }
        else if(v.equals(proceed_to_pay_btn))
        {
            if(userId ==0)
            {
                new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Not Registered Yet, Please Register and Proceed.", "Ok", "INFORMATION SAYS:", SignUpActivity.class);
            }
            else
            {
                if(quick_delivery_type_yes.isShown())
                {
//                    quickDeliveryRadioButtonID = quick_delivery_selector.getCheckedRadioButtonId();
                    useWalletCashRadioButtonID = use_wallet_cash_selector.getCheckedRadioButtonId();
                    paymentTypeRadioButtonID = payment_mode_selector.getCheckedRadioButtonId();

                        deliveryTypeRadioButtonID = delivery_type_selector.getCheckedRadioButtonId();
                        if (deliveryTypeRadioButtonID != -1 && useWalletCashRadioButtonID != -1 && paymentTypeRadioButtonID != -1)
                        {
                            deliveryTypeSelectedRadioButton = findViewById(deliveryTypeRadioButtonID);
                            useWalletCashSelectedRadioButton = findViewById(useWalletCashRadioButtonID);

                            deliveryTypeRadioButtonText = deliveryTypeSelectedRadioButton.getText().toString();
                            useWalletCashRadioButtonText = useWalletCashSelectedRadioButton.getText().toString();

                            String type = "place_order";

                            if(deliveryTypeRadioButtonText.equals("Normal"))
                            {
                                quickDeliveryRadioButtonText = "No";
                            }
                            else if(deliveryTypeRadioButtonText.equals("Quick"))
                            {
                                quickDeliveryRadioButtonText = "Yes";
                            }
                            else if(deliveryTypeRadioButtonText.equals("Scheduled"))
                            {
                                quickDeliveryRadioButtonText = "No";
                            }

                            if(firstAddressList.size() == 0)
                            {
                                new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Before placing an order, Please Add an Delivery Address First", "RETRY AGAIN", "INFORMATION SAYS:");
                            }
                            else {
                                String address_id = firstAddressList.get(0).getId();
                                place_order = new BackgroundWorker(PlaceOrderActivity.this);
                                place_order.setAsyncTaskListener(this);



                                if (delivery_date.isShown() && !(delivery_date.getEditText().getText().equals(""))) {
                                    place_order.execute(type, String.valueOf(userId), quickDeliveryRadioButtonText, date_time_selected, address_id, useWalletCashRadioButtonText, deviceToken);
                                } else if (!(delivery_date.isShown())) {

                                    place_order.execute(type, String.valueOf(userId), quickDeliveryRadioButtonText, date_time_selected, address_id, useWalletCashRadioButtonText, deviceToken);
                                }
                            }



                        }
                        else{
                            new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select the asked Options", "RETRY AGAIN", "INFORMATION SAYS:");
                        }

//                    else
//                    {
//                        if (useWalletCashRadioButtonID != -1 && paymentTypeRadioButtonID != -1) {
//                            useWalletCashSelectedRadioButton = findViewById(useWalletCashRadioButtonID);
//                            useWalletCashRadioButtonText = useWalletCashSelectedRadioButton.getText().toString();
//
//                            String type = "place_order";
//                            if(firstAddressList.size() == 0)
//                            {
//                                new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Before placing an order, Please Add an Delivery Address First", "RETRY AGAIN", "INFORMATION SAYS:");
//                            }
//                            else {
//                                String address_id = firstAddressList.get(0).getId();
//                                place_order = new BackgroundWorker(PlaceOrderActivity.this);
//                                place_order.setAsyncTaskListener(this);
//                                place_order.execute(type, String.valueOf(userId),quickDeliveryRadioButtonText, "", address_id, useWalletCashRadioButtonText);
//                            }
//                        }
//                        else{
//                            new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select the asked Options", "RETRY AGAIN", "INFORMATION SAYS:");
//                        }
//                    }
                }
                else
                {
                    deliveryTypeRadioButtonID = delivery_type_selector.getCheckedRadioButtonId();
                    useWalletCashRadioButtonID = use_wallet_cash_selector.getCheckedRadioButtonId();
                    paymentTypeRadioButtonID = payment_mode_selector.getCheckedRadioButtonId();
                    // If nothing is selected from Radio Group, then it return -1
                    if (useWalletCashRadioButtonID != -1 && deliveryTypeRadioButtonID != -1 && paymentTypeRadioButtonID != -1) {
                        deliveryTypeSelectedRadioButton = findViewById(deliveryTypeRadioButtonID);
                        useWalletCashSelectedRadioButton = findViewById(useWalletCashRadioButtonID);

                        deliveryTypeRadioButtonText = deliveryTypeSelectedRadioButton.getText().toString();
                        useWalletCashRadioButtonText = useWalletCashSelectedRadioButton.getText().toString();

                        if(deliveryTypeRadioButtonText.equals("Normal"))
                        {
                            quickDeliveryRadioButtonText = "No";
                        }
                        else if(deliveryTypeRadioButtonText.equals("Quick"))
                        {
                            Toast.makeText(this, "Not Applicable for this Booking", Toast.LENGTH_SHORT).show();
                        }

                        String type = "book_now";

                        if(firstAddressList.size() == 0)
                        {
                            new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Before placing an order, Please Add an Delivery Address First", "RETRY AGAIN", "INFORMATION SAYS:");
                        }
                        else
                        {
                            String address_id = firstAddressList.get(0).getId();
                            book_now= new BackgroundWorker(PlaceOrderActivity.this);
                            book_now.setAsyncTaskListener(this);

                            if(delivery_date.isShown() && !(delivery_date.getEditText().getText().equals("")))
                            {
                                book_now.execute(type, String.valueOf(userId), bookingProductQuantity, bookingProductId, address_id,date_time_selected, bookingIsfresh, useWalletCashRadioButtonText);
                            }
                            else if(!(delivery_date.isShown()))
                            {
                                Intent in = getIntent();
                                if(in.hasExtra("end_date"))
                                {
                                    date_time_selected = current_year+"-"+renewalMonth+"-"+renewalDay;
                                    book_now.execute(type, String.valueOf(userId), bookingProductQuantity, bookingProductId, address_id,date_time_selected, bookingIsfresh, useWalletCashRadioButtonText);
                                }
                                else
                                {
                                    date_time_selected = current_year+"-"+(current_month+1)+"-"+current_day;
                                    book_now.execute(type, String.valueOf(userId), bookingProductQuantity, bookingProductId, address_id,date_time_selected, bookingIsfresh, useWalletCashRadioButtonText);
                                }
                            }
                        }
                    }
                    else{
                        new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select the asked Options", "RETRY AGAIN", "INFORMATION SAYS:");
                    }
                }
            }

        }
        else if(v.equals(delivery_type_advanced))
        {
            delivery_selection_layout.setVisibility(View.VISIBLE);

        }
//        else if(v.equals(delivery_type_not_advanced))
//        {
//            delivery_selection_layout.setVisibility(View.GONE);
//            delivery_date.getEditText().setText("");
//            delivery_date.setVisibility(View.GONE);
//        }
        else if(v.equals(quick_delivery_type_yes))
        {
//            delivery_types_layout.setVisibility(View.GONE);
            delivery_selection_layout.setVisibility(View.GONE);
            delivery_date.getEditText().setText("");
            delivery_date.setVisibility(View.GONE);
        }
        else if(v.equals(quick_delivery_type_no))
        {
            delivery_selection_layout.setVisibility(View.GONE);
            delivery_date.getEditText().setText("");
            delivery_date.setVisibility(View.GONE);

//            advance_booking_layout.setVisibility(View.VISIBLE);
        }
        else if(v.equals(show_datepicker))
        {
            showDialog(999);
        }
        else if(v.equals(show_timepicker))
        {
            timePicker1.setVisibility(View.VISIBLE);
        }
        else if(v.equals(type_Cash))
        {
            payment_status = "Cash";
        }
        else if(v.equals(type_online))
        {
            payment_status = "Online";
        }
    }
}


//public class PlaceOrderActivity extends AppCompatActivity implements AsyncTaskListener, RecyclerViewClickHandler, View.OnClickListener {
//
//    RecyclerView address_recycler, address_list, all_final_products;
//    LinearLayout delivery_selection_layout, address_recycler_displayLayout, advance_booking_layout, /*is_quick_layout,*/ use_wallet_cash_layout, deposit_layout;
//    Button change_address_btn, proceed_to_pay_btn, show_datepicker, show_timepicker;
//    UserSharedPrefDetails sharedPrefDetails;
//    CardView booked_product, no_address_display, alert_no_address_display;
//    private GridLayoutManager gridLayoutManager, gridLayoutManager2;
//    private AddressesRecyclerViewAdapter addressesRecyclerViewAdapter, addressesRecyclerViewAdapter1;
//    private List<AddressesData> addressesDataList, firstAddressList;
//    private List<ProductsData> products_data;
//    BackgroundWorker get_all_products, place_order, book_now;
//    TextInputLayout delivery_date;
//    TextView total_price, number_of_products, amount_to_be_paid;
//    TextView booking_productName, booking_product_deposit, booking_product_isFresh, booking_product_quantity, booking_subTotal;
//    ImageView product_image;
//    String bookingProductId, bookingProductName, bookingImgUrl, bookingProductDeposit, bookingIsfresh, bookingProductQuantity, bookingProductPrice, bookingProductSubTotal, bookingProductEndDate, bookingProductAddressId, payment_status;
//    int bookingHomePrice, bookingOfficePrice, product_category_reference;
//    private FinalProductsRecyclerViewAdapter finalProductsRecyclerViewAdapter;
//    int userId, address_default_id;
//    RadioGroup /*quick_delivery_selector,*/ delivery_type_selector, use_wallet_cash_selector, payment_mode_selector;
//    RadioButton quick_delivery_type_yes, quick_delivery_type_no, delivery_type_advanced/*, delivery_type_not_advanced*/, use_wallet_cash_type_yes, use_wallet_cash_type_no, type_Cash, type_online;
//    AddressesData addressesData, addressesData2;
//    JSONObject jsonResult, single_address, temp_jsonObject, single_product;
//    int quickDeliveryRadioButtonID, deliveryTypeRadioButtonID, useWalletCashRadioButtonID, paymentTypeRadioButtonID;
//    String quickDeliveryRadioButtonText, deliveryTypeRadioButtonText, useWalletCashRadioButtonText, paymentTypeRadioButtonText;
//    RadioButton quickDeliverySelectedRadioButton, deliveryTypeSelectedRadioButton, useWalletCashSelectedRadioButton, paymentTypeSelectedRadioButton;
//    boolean success;
//    JSONArray addressesList;
//    Dialog alertDialog;
//    int current_year, current_month, current_day, selectedHour, selectedMinute, seconds;
//    int selectedYear, selectedMonth, selectedDay, renewalDay, renewalMonth;
//    int first_address;
//    Date current_date;
//    String date_time_selected;
//    private Calendar calendar;
//    private TimePicker timePicker1;
//    private DatePickerDialog.OnDateSetListener myDateListener;
//    String cartId, productId, product_name, product_price, product_deposit, productImg_link, product_quantity, product_status, upto_quantity, product_isFresh, product_subTotal;
//    String addressId, addressholder_name, address_line1, address_line2, address_landmark,address_mobile, address_pincode, address_type, address_state, address_stateId, address_district, address_districtId, address_status, message;
//    String deviceToken = "";
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_place_order);
//        address_recycler = findViewById(R.id.address_recycler);
//        change_address_btn = findViewById(R.id.change_address_btn);
//        all_final_products = findViewById(R.id.all_ordering_products);
//        no_address_display = findViewById(R.id.no_address_diplay);
//        total_price = findViewById(R.id.total_price);
//        number_of_products = findViewById(R.id.number_of_products);
//        amount_to_be_paid = findViewById(R.id.amount_to_be_paid);
////        quick_delivery_selector = findViewById(R.id.quick_delivery_selector);
//        delivery_type_selector = findViewById(R.id.delivery_type_selector);
//        use_wallet_cash_selector = findViewById(R.id.use_wallet_cash_selector);
//        quick_delivery_type_yes= findViewById(R.id.quick_delivery_type_yes);
//        quick_delivery_type_no= findViewById(R.id.quick_delivery_type_no);
//        use_wallet_cash_type_yes = findViewById(R.id.use_wallet_cash_yes);
//        use_wallet_cash_type_no = findViewById(R.id.use_wallet_cash_no);
//        delivery_type_advanced = findViewById(R.id.delivery_type_advanced);
////        delivery_type_not_advanced = findViewById(R.id.delivery_type_not_advanced);
//        proceed_to_pay_btn = findViewById(R.id.proceed_to_checkout_btn);
//        address_recycler_displayLayout = findViewById(R.id.address_recycler_layout);
//        advance_booking_layout = findViewById(R.id.advance_booking_layout);
//        delivery_date = findViewById(R.id.delivery_date);
//        timePicker1 = findViewById(R.id.timePicker1);
//        delivery_selection_layout = findViewById(R.id.delivery_selection_layout);
//        show_datepicker = findViewById(R.id.show_datepicker);
//        show_timepicker = findViewById(R.id.show_timepicker);
//        booked_product = findViewById(R.id.booked_product);
//        booking_productName = findViewById(R.id.product_name);
//        booking_product_deposit = findViewById(R.id.product_deposit);
//        booking_product_isFresh = findViewById(R.id.isfresh_type);
//        booking_product_quantity = findViewById(R.id.quantity);
//        booking_subTotal = findViewById(R.id.sub_total);
//        product_image = findViewById(R.id.product_image);
////        is_quick_layout = findViewById(R.id.is_quick_layout);
//        use_wallet_cash_layout = findViewById(R.id.use_wallet_cash_layout);
//        deposit_layout = findViewById(R.id.deposit_layout);
//        type_Cash = findViewById(R.id.type_cash);
//        type_online = findViewById(R.id.type_online);
//        payment_mode_selector = findViewById(R.id.payment_mode_selector);
//
//        sharedPrefDetails = new UserSharedPrefDetails(PlaceOrderActivity.this);
//        userId = sharedPrefDetails.getUser_Id();
//
//        if(!SharedPreferenceManagerFCM.getInstance(PlaceOrderActivity.this).getToken().equals(""))
//        {
//            Log.i("tokenTestingIf", "onCreate: "+SharedPreferenceManagerFCM.getInstance(this).getToken());
//            deviceToken = SharedPreferenceManagerFCM.getInstance(this).getToken(); // need to pass this token id while caling login api
//            Log.i("DeviceTockenIf", "onCreate: "+deviceToken);
//        }
//        else
//        {
//            deviceToken = SharedPreferenceManagerFCM.getInstance(this).getToken();
//            Log.i("tokenTestingIf", "onCreate: "+SharedPreferenceManagerFCM.getInstance(this).getToken());
//            Log.i("DeviceokenElse", "onCreate: "+deviceToken);
//        }
//
//        addressesDataList = new ArrayList<>();
//        firstAddressList = new ArrayList<>();
//        products_data = new ArrayList<>();
//
//        selectedDay = 0;
//        selectedHour = 0;
//        selectedMinute = 0;
//        selectedYear = 0;
//        selectedMonth = 0;
//        date_time_selected = "";
//        payment_status = "";
//
//        current_date = (Date) Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String formattedDate = df.format(current_date);
//
//        SimpleDateFormat currentYear = new SimpleDateFormat("yyyy");
//        String formatedYear = currentYear.format(current_date);
//        current_year = Integer.parseInt(formatedYear);
//
//        SimpleDateFormat currentMonth = new SimpleDateFormat("MM");
//        String formatedMonth = currentMonth.format(current_date);
//        current_month = Integer.parseInt(formatedMonth)-1;
//
//        SimpleDateFormat currentDay = new SimpleDateFormat("dd");
//        String formatedDay = currentDay.format(current_date);
//        current_day = Integer.parseInt(formatedDay)+1;
//
//        Intent date_intent = getIntent();
//        if(date_intent.hasExtra("end_date"))
//        {
//            bookingProductEndDate = date_intent.getStringExtra("end_date");
//            try {
//
//                SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
//                Date ending_date = d.parse(bookingProductEndDate);
//
//                String package_endDate = d.format(ending_date);
//                String package_end_month = currentMonth.format(ending_date);
//                String package_end_day = currentDay.format(ending_date);
//
//                renewalDay = Integer.parseInt(package_end_day)+1;
//                renewalMonth = Integer.parseInt(package_end_month)-1;
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        gridLayoutManager = new GridLayoutManager(PlaceOrderActivity.this, 1);
//        address_recycler.setLayoutManager(gridLayoutManager);
//
//        gridLayoutManager2 = new GridLayoutManager(PlaceOrderActivity.this, 1);
//        all_final_products.setLayoutManager(gridLayoutManager2);
//
//        alertDialog = new Dialog(PlaceOrderActivity.this);
//        address_default_id = 0;
//        String type_getAddress = "get_address";
//
//        Intent i = getIntent();
//        if(i.hasExtra("address_id"))
//        {
//            bookingProductAddressId = i.getStringExtra("address_id");
//            address_default_id = Integer.parseInt(bookingProductAddressId);
//            BackgroundWorker getAddresses= new BackgroundWorker(PlaceOrderActivity.this);
//            getAddresses.setAsyncTaskListener(this);
//            getAddresses.execute(type_getAddress, Integer.toString(userId), Integer.toString(address_default_id));
//        }
//        else
//        {
//            BackgroundWorker getAddress= new BackgroundWorker(PlaceOrderActivity.this);
//            getAddress.setAsyncTaskListener(this);
//            getAddress.execute(type_getAddress, Integer.toString(userId), Integer.toString(address_default_id));
//        }
//
//        change_address_btn.setOnClickListener(this);
//        proceed_to_pay_btn.setOnClickListener(this);
//        delivery_type_advanced.setOnClickListener(this);
//        delivery_type_not_advanced.setOnClickListener(this);
//        quick_delivery_type_yes.setOnClickListener(this);
//        quick_delivery_type_no.setOnClickListener(this);
//        use_wallet_cash_type_yes.setOnClickListener(this);
//        use_wallet_cash_type_no.setOnClickListener(this);
//        show_timepicker.setOnClickListener(this);
//        show_datepicker.setOnClickListener(this);
//        type_Cash.setOnClickListener(this);
//        type_online.setOnClickListener(this);
//
//        myDateListener= new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                SimpleDateFormat currentSeconds = new SimpleDateFormat("ss");
//                String formatedSeconds = currentSeconds.format(current_date);
//                seconds = Integer.parseInt(formatedSeconds);
//
//                selectedYear = year;
//                selectedMonth = month;
//                selectedDay = dayOfMonth;
//
//                Intent intent1 = getIntent();
//                if(intent1.hasExtra("end_date"))
//                {
//                    if(selectedYear == current_year)
//                    {
//                        if(selectedMonth == renewalMonth)
//                        {
//                            if(selectedDay >= renewalDay)
//                            {
//                                delivery_date.setVisibility(View.VISIBLE);
//                                delivery_date.getEditText().setEnabled(false);
//                                if(show_timepicker.isShown())
//                                {
//                                    date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
//                                    delivery_date.getEditText().setText(date_time_selected);
//                                }
//                                else
//                                {
//                                    date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
//                                    delivery_date.getEditText().setText(date_time_selected);
//                                }
//                            }
//                            else
//                            {
//                                new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Day. You cant select past or today's date for advance booking.", "OK", "INFORMATION SAYS:");
//                            }
//                        }
//                        else if(selectedMonth > renewalMonth)
//                        {
//                            delivery_date.setVisibility(View.VISIBLE);
//                            delivery_date.getEditText().setEnabled(false);
//                            if(show_timepicker.isShown())
//                            {
//                                date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
//                                delivery_date.getEditText().setText(date_time_selected);
//                            }
//                            else
//                            {
//                                date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
//                                delivery_date.getEditText().setText(date_time_selected);
//                            }
//                        }
//                        else
//                        {
//                            delivery_date.setVisibility(View.GONE);
//                            new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Month. You cannot select past month", "OK", "INFORMATION SAYS:");
//                        }
//                    }
//                    else if (selectedYear > current_year)
//                    {
//                        delivery_date.setVisibility(View.VISIBLE);
//                        delivery_date.getEditText().setEnabled(false);
//                        if(show_timepicker.isShown())
//                        {
//                            date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
//                            delivery_date.getEditText().setText(date_time_selected);
//                        }
//                        else
//                        {
//                            date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
//                            delivery_date.getEditText().setText(date_time_selected);
//                        }
//                    }
//                    else
//                    {
//                        delivery_date.setVisibility(View.GONE);
//                        new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Year. You cannot select past year", "OK", "INFORMATION SAYS:");
//                    }
//                }
//                else {
//                    if(selectedYear == current_year)
//                    {
//                        if(selectedMonth == current_month)
//                        {
//                            if(selectedDay >= current_day)
//                            {
//                                delivery_date.setVisibility(View.VISIBLE);
//                                delivery_date.getEditText().setEnabled(false);
//                                if(show_timepicker.isShown())
//                                {
//                                    date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
//                                    delivery_date.getEditText().setText(date_time_selected);
//                                }
//                                else
//                                {
//                                    date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
//                                    delivery_date.getEditText().setText(date_time_selected);
//                                }
//                            }
//                            else
//                            {
//                                new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Day. You cant select past or today's date for advance booking.", "OK", "INFORMATION SAYS:");
//                            }
//                        }
//                        else if(selectedMonth > current_month)
//                        {
//                            delivery_date.setVisibility(View.VISIBLE);
//                            delivery_date.getEditText().setEnabled(false);
//                            if(show_timepicker.isShown())
//                            {
//                                date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
//                                delivery_date.getEditText().setText(date_time_selected);
//                            }
//                            else
//                            {
//                                date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
//                                delivery_date.getEditText().setText(date_time_selected);
//                            }
//                        }
//                        else
//                        {
//                            delivery_date.setVisibility(View.GONE);
//                            new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Month. You cannot select past month", "OK", "INFORMATION SAYS:");
//                        }
//                    }
//                    else if (selectedYear > current_year)
//                    {
//                        delivery_date.setVisibility(View.VISIBLE);
//                        delivery_date.getEditText().setEnabled(false);
//                        if(show_timepicker.isShown())
//                        {
//                            date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
//                            delivery_date.getEditText().setText(date_time_selected);
//                        }
//                        else
//                        {
//                            date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay;
//                            delivery_date.getEditText().setText(date_time_selected);
//                        }
//                    }
//                    else
//                    {
//                        delivery_date.setVisibility(View.GONE);
//                        new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select Future Year. You cannot select past year", "OK", "INFORMATION SAYS:");
//                    }
//                }
//            }
//        };
//
//        calendar = Calendar.getInstance();
//
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int min = calendar.get(Calendar.MINUTE);
//        timePicker1.setIs24HourView(true);
//
//        timePicker1.setHour(hour);
//        timePicker1.setMinute(min);
//        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//
//                SimpleDateFormat currentSeconds = new SimpleDateFormat("ss");
//                String formatedSeconds = currentSeconds.format(current_date);
//                seconds = Integer.parseInt(formatedSeconds);
//
//                if(delivery_date.isShown())
//                {
//                    delivery_date.getEditText().setEnabled(false);
//
//                    selectedHour = hourOfDay;
//                    selectedMinute = minute;
//
//                    date_time_selected = selectedYear+"-"+(selectedMonth+1)+"-"+selectedDay+" "+selectedHour+":"+selectedMinute+":"+seconds;
//                    delivery_date.getEditText().setText(date_time_selected);
//                }
//                else
//                {
//                    new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select The Date First", "OK", "INFORMATION SAYS:");
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        FragmentManager fragmentManager = getFragmentManager();
//        int backCount = fragmentManager.getBackStackEntryCount();
//        if(backCount > 1) {
//            super.onBackPressed();
//        } else {
//            finish();
//        }
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        if (id == 999) {
//
//            Intent date_intent = getIntent();
//            if(date_intent.hasExtra("end_date"))
//            {
//                return new DatePickerDialog(this, myDateListener, current_year, renewalMonth, renewalDay);
//            }
//            else
//            {
//                return new DatePickerDialog(this, myDateListener, current_year, current_month, current_day);
//            }
//        }
//        return null;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void updateResult(String result) {
//
//        if(result != null) {
//            try {
//                jsonResult = new JSONObject(result);
////            int success = jsonResult.getInt("success");
//                success = jsonResult.getBoolean("status");
////            status code positive = true, negative = false;
//                if (!success) {
//                    delivery_date.setVisibility(View.GONE);
//                    if (jsonResult.has("msg")) {
//                        message = jsonResult.getString("msg");
//                        new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "RETRY AGAIN", "INFORMATION SAYS:");
//                    } else if (jsonResult.has("data")) {
//                        address_recycler_displayLayout.setVisibility(View.GONE);
//                        change_address_btn.setText("No Addresses Added Yet, Please Add New");
//
//                        all_final_products.setVisibility(View.VISIBLE);
//                        booked_product.setVisibility(View.GONE);
//                        advance_booking_layout.setVisibility(View.GONE);
//                        delivery_selection_layout.setVisibility(View.GONE);
//                        timePicker1.setVisibility(View.GONE);
//                        String type_viewCart = "view_cart";
//                        get_all_products = new BackgroundWorker(PlaceOrderActivity.this);
//                        get_all_products.setAsyncTaskListener(this);
//                        if(userId == 0)
//                        {
//                            get_all_products.execute(type_viewCart, "", deviceToken);
//                        }
//                        else
//                        {
//                            get_all_products.execute(type_viewCart, Integer.toString(userId), deviceToken);
//                        }
//
//
//                        Intent intent = getIntent();
//                        if (intent.hasExtra("product_id") && intent.hasExtra("product_name") &&
//                                intent.hasExtra("product_price") && intent.hasExtra("product_deposit")
//                                && intent.hasExtra("product_image_link") && intent.hasExtra("isFresh")
//                                && intent.hasExtra("product_quantity") && intent.hasExtra("product_category_reference")
//                                && intent.hasExtra("product_total_priceHome") && intent.hasExtra("product_total_priceOffice")) {
//                            all_final_products.setVisibility(View.GONE);
//                            is_quick_layout.setVisibility(View.GONE);
//                            show_timepicker.setVisibility(View.GONE);
//                            timePicker1.setVisibility(View.GONE);
//                            delivery_selection_layout.setVisibility(View.GONE);
//
//                            bookingImgUrl = intent.getStringExtra("product_image_link");
//                            bookingProductId = intent.getStringExtra("product_id");
//                            bookingProductName = intent.getStringExtra("product_name");
//                            bookingProductPrice = intent.getStringExtra("product_price");
//                            bookingProductDeposit = intent.getStringExtra("product_deposit");
//                            bookingIsfresh = intent.getStringExtra("isFresh");
//                            bookingProductQuantity = intent.getStringExtra("product_quantity");
//                            product_category_reference = intent.getIntExtra("product_reference_category", -1);
//                            bookingOfficePrice = intent.getIntExtra("product_total_priceOffice", 0);
//                            bookingHomePrice = intent.getIntExtra("product_total_priceHome", 0);
//
//                            Log.i("prices", "updateResult: Home Price: " + bookingHomePrice + " office price: " + bookingOfficePrice);
//
//                            Glide.with(PlaceOrderActivity.this)
//                                    .asBitmap().load(bookingImgUrl).into(product_image);
//                            booking_product_quantity.setText(bookingProductQuantity);
//                            booking_product_deposit.setText(bookingProductDeposit);
//                            booking_productName.setText(bookingProductName);
//                            booking_product_isFresh.setText(bookingIsfresh);
//                            number_of_products.setText("1");
//
//                            booking_subTotal.setText(Integer.toString(bookingHomePrice));
//                            total_price.setText(Integer.toString(bookingHomePrice));
//                            amount_to_be_paid.setText(Integer.toString(bookingHomePrice));
//                        }
//                    } else {
//                        new CustomDialogs(PlaceOrderActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Something Went Wrong, Please try again later", "OK", "INFORMATION SAYS:", PlaceOrderActivity.class);
//                    }
//                } else {
//                    if (jsonResult.has("msg")) {
//                        String message = jsonResult.getString("msg");
//                        new CustomDialogs(PlaceOrderActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "INFORMATION SAYS:", HomeActivity.class);
//                    } else if (jsonResult.has("status") && !(jsonResult.has("data")) && jsonResult.has("OrderId")) {
//                        String orderId = jsonResult.getString("OrderId");
//                        Log.i("Place_orderID", "updateResult: "+orderId);
//                        Intent intent1 = new Intent(PlaceOrderActivity.this, PayNowActivity.class);
//                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        Intent intent = getIntent();
//                        if (intent.hasExtra("product_category_reference")) {
//                            product_category_reference = intent.getIntExtra("product_reference_category", -1);
//                            intent1.putExtra("reference", product_category_reference);
//                            intent1.putExtra("address_id_selected", Integer.toString(address_default_id));
//                            intent1.putExtra("order_id", orderId);
//                            intent1.putExtra("payment_type", payment_status);
//                            Log.i("Intent_OID", "updateResult: "+orderId);
////                            Log.i("payment_type", "updateResult: "+payment_status);
////                            Log.i("address_id", "updateResult: "+address_default_id);
////                            Log.i("reference", "updateResult: "+product_category_reference);
//                            startActivity(intent1);
//                        } else {
//                            intent1.putExtra("address_id_selected", Integer.toString(address_default_id));
//                            intent1.putExtra("payment_type", payment_status);
//                            intent1.putExtra("order_id", orderId);
//                            Log.i("Intent_OID", "updateResult: "+orderId);
////                            Log.i("payment_type", "updateResult: "+payment_status);
////                            Log.i("address_id", "updateResult: "+address_default_id);
//                            startActivity(intent1);
//                        }
//
//                    } else if (jsonResult.has("data")) {
//                        address_recycler.setVisibility(View.VISIBLE);
////                    no_address_display.setVisibility(View.GONE);
//                        addressesList = jsonResult.getJSONArray("data");
//                        if (addressesList.length() != 0) {
//                            products_data.clear();
//                            delivery_date.setVisibility(View.GONE);
//                            temp_jsonObject = addressesList.getJSONObject(0);
//                            if (temp_jsonObject.has("Line1")) {
//                                if (address_default_id == 0) {
//                                    for (int i = 0; i < addressesList.length(); i++) {
//                                        single_address = addressesList.getJSONObject(i);
//                                        addressId = single_address.getString("Id");
//                                        addressholder_name = single_address.getString("Name");
//                                        address_line1 = single_address.getString("Line1");
//                                        address_line2 = single_address.getString("Line2");
//                                        address_landmark = single_address.getString("Landmark");
//                                        address_mobile = single_address.getString("Mobile");
//                                        address_pincode = single_address.getString("Pin");
//                                        address_type = single_address.getString("AddressType");
//                                        address_state = single_address.getString("State");
//                                        address_stateId = single_address.getString("StateId");
//                                        address_district = single_address.getString("District");
//                                        address_districtId = single_address.getString("DistrictId");
//                                        address_status = single_address.getString("Status");
//
//                                        addressesData2 = new AddressesData(addressId, addressholder_name, address_line1, address_line2, address_landmark, address_mobile, address_pincode, address_type, address_state, address_stateId, address_district, address_districtId, address_status);
//                                        addressesDataList.add(addressesData2);
//                                    }
//
//                                    firstAddressList.clear();
//                                    Intent intent = getIntent();
//                                    if (intent.hasExtra("end_date")) {
//                                        bookingProductAddressId = intent.getStringExtra("address_id");
//                                        Log.i("addressId", "updateResult: " + true);
//                                        first_address = Integer.parseInt(bookingProductAddressId);
//                                    } else {
//                                        first_address = addressesList.length() - (addressesList.length() - 1);
//
//                                    }
//
//                                    for (int i = 0; i < first_address; i++) {
//                                        single_address = addressesList.getJSONObject(i);
//                                        addressId = single_address.getString("Id");
//                                        addressholder_name = single_address.getString("Name");
//                                        address_line1 = single_address.getString("Line1");
//                                        address_line2 = single_address.getString("Line2");
//                                        address_landmark = single_address.getString("Landmark");
//                                        address_mobile = single_address.getString("Mobile");
//                                        address_pincode = single_address.getString("Pin");
//                                        address_type = single_address.getString("AddressType");
//                                        address_state = single_address.getString("State");
//                                        address_stateId = single_address.getString("StateId");
//                                        address_district = single_address.getString("District");
//                                        address_districtId = single_address.getString("DistrictId");
//                                        address_status = single_address.getString("Status");
//
//                                        addressesData = new AddressesData(addressId, addressholder_name, address_line1, address_line2, address_landmark, address_mobile, address_pincode, address_type, address_state, address_stateId, address_district, address_districtId, address_status);
//                                        firstAddressList.add(addressesData);
//                                    }
//                                    addressesRecyclerViewAdapter = new AddressesRecyclerViewAdapter(PlaceOrderActivity.this, firstAddressList, this);
//                                    address_recycler.setAdapter(addressesRecyclerViewAdapter);
//                                } else {
//                                    firstAddressList.clear();
//                                    for (int i = 0; i < addressesList.length(); i++) {
//                                        single_address = addressesList.getJSONObject(i);
//                                        addressId = single_address.getString("Id");
//                                        addressholder_name = single_address.getString("Name");
//                                        address_line1 = single_address.getString("Line1");
//                                        address_line2 = single_address.getString("Line2");
//                                        address_landmark = single_address.getString("Landmark");
//                                        address_mobile = single_address.getString("Mobile");
//                                        address_pincode = single_address.getString("Pin");
//                                        address_type = single_address.getString("AddressType");
//                                        address_state = single_address.getString("State");
//                                        address_stateId = single_address.getString("StateId");
//                                        address_district = single_address.getString("District");
//                                        address_districtId = single_address.getString("DistrictId");
//                                        address_status = single_address.getString("Status");
//
//                                        addressesData = new AddressesData(addressId, addressholder_name, address_line1, address_line2, address_landmark, address_mobile, address_pincode, address_type, address_state, address_stateId, address_district, address_districtId, address_status);
//                                        firstAddressList.add(addressesData);
//                                    }
//                                    addressesRecyclerViewAdapter = new AddressesRecyclerViewAdapter(getContext(), firstAddressList, this);
//                                    address_recycler.setAdapter(addressesRecyclerViewAdapter);
//                                }
//
//                                Intent intent = getIntent();
//                                Intent renew_intent = getIntent();
//                                if (intent.hasExtra("product_id") && intent.hasExtra("product_name") &&
//                                        intent.hasExtra("product_price") && intent.hasExtra("product_deposit")
//                                        && intent.hasExtra("product_image_link") && intent.hasExtra("isFresh")
//                                        && intent.hasExtra("product_quantity") && intent.hasExtra("product_category_reference")
//                                        && intent.hasExtra("product_total_priceHome") && intent.hasExtra("product_total_priceOffice")) {
//                                    all_final_products.setVisibility(View.GONE);
//                                    is_quick_layout.setVisibility(View.GONE);
//                                    show_timepicker.setVisibility(View.GONE);
//                                    timePicker1.setVisibility(View.GONE);
//                                    delivery_selection_layout.setVisibility(View.GONE);
//
//                                    bookingImgUrl = intent.getStringExtra("product_image_link");
//                                    bookingProductId = intent.getStringExtra("product_id");
//                                    bookingProductName = intent.getStringExtra("product_name");
//                                    bookingProductPrice = intent.getStringExtra("product_price");
//                                    bookingProductDeposit = intent.getStringExtra("product_deposit");
//                                    bookingIsfresh = intent.getStringExtra("isFresh");
//                                    bookingProductQuantity = intent.getStringExtra("product_quantity");
//                                    product_category_reference = intent.getIntExtra("product_reference_category", -1);
//                                    bookingOfficePrice = intent.getIntExtra("product_total_priceOffice", 0);
//                                    bookingHomePrice = intent.getIntExtra("product_total_priceHome", 0);
//
//                                    Log.i("Testing", "Home" + bookingHomePrice + " \n Office: " + bookingOfficePrice);
//                                    Glide.with(PlaceOrderActivity.this)
//                                            .asBitmap().load(bookingImgUrl).into(product_image);
//                                    booking_product_quantity.setText(bookingProductQuantity);
//                                    booking_product_deposit.setText(bookingProductDeposit);
//                                    booking_productName.setText(bookingProductName);
//                                    booking_product_isFresh.setText(bookingIsfresh);
//                                    number_of_products.setText("1");
//
//                                    if(firstAddressList.size() > 0) {
//                                        String adType = firstAddressList.get(0).getAddressType();
//                                        if (adType.equals("Home")) {
//                                            booking_subTotal.setText(Integer.toString(bookingHomePrice));
//                                            total_price.setText(Integer.toString(bookingHomePrice));
//                                            amount_to_be_paid.setText(Integer.toString(bookingHomePrice));
//                                        } else if (adType.equals("Office")) {
//                                            booking_subTotal.setText(Integer.toString(bookingOfficePrice));
//                                            total_price.setText(Integer.toString(bookingOfficePrice));
//                                            amount_to_be_paid.setText(Integer.toString(bookingOfficePrice));
//                                        }
//                                    } else {
//                                        Toast.makeText(this, "Something went Wrong while selecting address type", Toast.LENGTH_SHORT).show();
//                                    }
//                                } else if (renew_intent.hasExtra("Id") && renew_intent.hasExtra("product_name") &&
//                                        renew_intent.hasExtra("total_amount") && renew_intent.hasExtra("product_deposit")
//                                        && renew_intent.hasExtra("productImg_link") && renew_intent.hasExtra("isFresh")
//                                        && renew_intent.hasExtra("product_quantity") && renew_intent.hasExtra("product_subTotal")
//                                        && renew_intent.hasExtra("end_date") && renew_intent.hasExtra("address_id")) {
//                                    all_final_products.setVisibility(View.GONE);
//                                    is_quick_layout.setVisibility(View.GONE);
//                                    show_timepicker.setVisibility(View.GONE);
//                                    timePicker1.setVisibility(View.GONE);
//                                    delivery_selection_layout.setVisibility(View.GONE);
//
//                                    bookingImgUrl = intent.getStringExtra("productImg_link");
//                                    bookingProductId = intent.getStringExtra("Id");
//                                    bookingProductName = intent.getStringExtra("product_name");
//                                    bookingProductPrice = intent.getStringExtra("total_amount");
//                                    bookingProductDeposit = intent.getStringExtra("product_deposit");
//                                    bookingIsfresh = intent.getStringExtra("isFresh");
//                                    bookingProductQuantity = intent.getStringExtra("product_quantity");
//                                    bookingProductSubTotal = intent.getStringExtra("product_subTotal");
//                                    bookingProductEndDate = intent.getStringExtra("end_date");
//                                    bookingProductAddressId = intent.getStringExtra("address_id");
//
//                                    Glide.with(PlaceOrderActivity.this)
//                                            .asBitmap().load(bookingImgUrl).into(product_image);
//                                    booking_product_quantity.setText(bookingProductQuantity);
//                                    booking_productName.setText(bookingProductName);
//                                    booking_product_isFresh.setText(bookingIsfresh);
//                                    number_of_products.setText("1");
//                                    booking_subTotal.setText(bookingProductPrice);
//                                    total_price.setText(bookingProductPrice);
//                                    amount_to_be_paid.setText(bookingProductSubTotal);
//                                    if (bookingIsfresh.equals("Yes")) {
//                                        deposit_layout.setVisibility(View.VISIBLE);
//                                        booking_product_deposit.setText(bookingProductDeposit);
//                                    } else if (bookingIsfresh.equals("No")) {
//                                        deposit_layout.setVisibility(View.GONE);
//                                    }
//
//                                } else {
//                                    all_final_products.setVisibility(View.VISIBLE);
//                                    booked_product.setVisibility(View.GONE);
//                                    advance_booking_layout.setVisibility(View.GONE);
//                                    delivery_selection_layout.setVisibility(View.GONE);
//                                    timePicker1.setVisibility(View.GONE);
//                                    String type_viewCart = "view_cart";
//                                    get_all_products = new BackgroundWorker(PlaceOrderActivity.this);
//                                    get_all_products.setAsyncTaskListener(this);
//                                    if(userId == 0)
//                                    {
//                                        get_all_products.execute(type_viewCart, "", deviceToken);
//                                    }
//                                    else
//                                    {
//                                        get_all_products.execute(type_viewCart, Integer.toString(userId), deviceToken);
//                                    }
//                                }
//                            } else if (temp_jsonObject.has("ProductId")) {
//
//                                for (int i = 0; i < addressesList.length(); i++) {
//                                    single_product = addressesList.getJSONObject(i);
//                                    cartId = single_product.getString("Id");
//                                    productId = single_product.getString("ProductId");
//                                    product_name = single_product.getString("Name");
//                                    product_price = single_product.getString("Price");
//                                    product_deposit = single_product.getString("Deposit");
//                                    productImg_link = single_product.getString("FilePath");
//                                    product_quantity = single_product.getString("Quantity");
//                                    product_status = single_product.getString("ProductStatus");
//                                    upto_quantity = single_product.getString("UptoQuantity");
//                                    product_isFresh = single_product.getString("IsFresh");
//                                    product_subTotal = single_product.getString("SubTotal");
//
//                                    ProductsData productsData = new ProductsData(cartId, productId, product_name, product_price, product_deposit, productImg_link, product_quantity, product_isFresh, upto_quantity, product_subTotal, product_status);
//                                    products_data.add(productsData);
//                                }
//
//                                String product_total = jsonResult.getString("Total");
//                                total_price.setText(product_total);
//
//                                finalProductsRecyclerViewAdapter = new FinalProductsRecyclerViewAdapter(PlaceOrderActivity.this, products_data);
//                                all_final_products.setAdapter(finalProductsRecyclerViewAdapter);
//                                finalProductsRecyclerViewAdapter.notifyDataSetChanged();
//
//                                amount_to_be_paid.setText(product_total);
//                                number_of_products.setText(String.valueOf(finalProductsRecyclerViewAdapter.getItemCount()));
//                            }
//                        }
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        else
//        {
//            Toast.makeText(this, "We didnt got any response...Ooopss Network Error!!", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    @Override
//    public void onDeleteClicked(int position) {
//
//        if(alertDialog.isShowing())
//        {
//            alertDialog.dismiss();
//            String type = "get_address";
//            BackgroundWorker getAddresses= new BackgroundWorker(PlaceOrderActivity.this);
//            getAddresses.setAsyncTaskListener(this);
//            address_default_id = position;
//            getAddresses.execute(type, Integer.toString(userId), Integer.toString(address_default_id));
//        }
//        else
//        {
//            Toast.makeText(PlaceOrderActivity.this, "Please click on Change Button for changing Delivery Address", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    @Override
//    public void onQuantityUpdated(int position, String quantity) {
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onClick(View v) {
//
//        if(v.equals(change_address_btn))
//        {
////            Log.i("size", ""+addressesDataList.size());
//            if(addressesDataList.size() == 0)
//            {
//                Intent intent = new Intent(PlaceOrderActivity.this, NewAddressWhileOrderingActivity.class);
//                startActivity(intent);
//            }
//            else {
//                alertDialog = new Dialog(PlaceOrderActivity.this);
//                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                alertDialog.setContentView(R.layout.address_selection_layout);
//                address_list = alertDialog.findViewById(R.id.address_list);
//                alert_no_address_display = alertDialog.findViewById(R.id.no_address_diplay);
//                addressesRecyclerViewAdapter1 = new AddressesRecyclerViewAdapter(PlaceOrderActivity.this, addressesDataList, PlaceOrderActivity.this);
//                address_list.setAdapter(addressesRecyclerViewAdapter1);
//                address_list.setLayoutManager(new LinearLayoutManager(PlaceOrderActivity.this));
//                addressesRecyclerViewAdapter1.notifyDataSetChanged();
//                Intent i = getIntent();
//                if(i.hasExtra("end_date")) {
//                    new CustomDialogs(PlaceOrderActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, "The price may vary if, address changes", "OK", "INFORMATION SAYS:");
//                    alertDialog.show();
//                }
//                else {
//                    alertDialog.show();
//                }
//            }
//        }
//        else if(v.equals(proceed_to_pay_btn))
//        {
//            if(userId ==0)
//            {
//                new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Not Registered Yet, Please Register and Proceed.", "Ok", "INFORMATION SAYS:", SignUpActivity.class);
//            }
//            else
//            {
//                if(is_quick_layout.isShown())
//                {
//                    quickDeliveryRadioButtonID = quick_delivery_selector.getCheckedRadioButtonId();
//                    useWalletCashRadioButtonID = use_wallet_cash_selector.getCheckedRadioButtonId();
//                    paymentTypeRadioButtonID = payment_mode_selector.getCheckedRadioButtonId();
//
//                    if(advance_booking_layout.isShown())
//                    {
//                        deliveryTypeRadioButtonID = delivery_type_selector.getCheckedRadioButtonId();
//                        if (quickDeliveryRadioButtonID != -1 && deliveryTypeRadioButtonID != -1 && useWalletCashRadioButtonID != -1 && paymentTypeRadioButtonID != -1)
//                        {
//                            quickDeliverySelectedRadioButton = findViewById(quickDeliveryRadioButtonID);
//                            deliveryTypeSelectedRadioButton = findViewById(deliveryTypeRadioButtonID);
//                            useWalletCashSelectedRadioButton = findViewById(useWalletCashRadioButtonID);
//
//                            quickDeliveryRadioButtonText= quickDeliverySelectedRadioButton.getText().toString();
//                            deliveryTypeRadioButtonText = deliveryTypeSelectedRadioButton.getText().toString();
//                            useWalletCashRadioButtonText = useWalletCashSelectedRadioButton.getText().toString();
//
//                            String type = "place_order";
//
//                            if(firstAddressList.size() == 0)
//                            {
//                                new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Before placing an order, Please Add an Delivery Address First", "RETRY AGAIN", "INFORMATION SAYS:");
//                            }
//                            else {
//                                String address_id = firstAddressList.get(0).getId();
//                                place_order = new BackgroundWorker(PlaceOrderActivity.this);
//                                place_order.setAsyncTaskListener(this);
//
//                                if (delivery_date.isShown() && !(delivery_date.getEditText().getText().equals(""))) {
//                                    place_order.execute(type, String.valueOf(userId), quickDeliveryRadioButtonText, date_time_selected, address_id, useWalletCashRadioButtonText);
//                                } else if (!(delivery_date.isShown())) {
//                                    place_order.execute(type, String.valueOf(userId), quickDeliveryRadioButtonText, date_time_selected, address_id, useWalletCashRadioButtonText);
//                                }
//                            }
//
//
//
//                        }
//                        else{
//                            new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select the asked Options", "RETRY AGAIN", "INFORMATION SAYS:");
//                        }
//                    }
//                    else
//                    {
//                        if (quickDeliveryRadioButtonID != -1  && useWalletCashRadioButtonID != -1 && paymentTypeRadioButtonID != -1) {
//                            quickDeliverySelectedRadioButton = findViewById(quickDeliveryRadioButtonID);
//                            useWalletCashSelectedRadioButton = findViewById(useWalletCashRadioButtonID);
//
//                            quickDeliveryRadioButtonText= quickDeliverySelectedRadioButton.getText().toString();
//                            useWalletCashRadioButtonText = useWalletCashSelectedRadioButton.getText().toString();
//
//                            String type = "place_order";
//                            if(firstAddressList.size() == 0)
//                            {
//                                new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Before placing an order, Please Add an Delivery Address First", "RETRY AGAIN", "INFORMATION SAYS:");
//                            }
//                            else {
//                                String address_id = firstAddressList.get(0).getId();
//                                place_order = new BackgroundWorker(PlaceOrderActivity.this);
//                                place_order.setAsyncTaskListener(this);
//                                place_order.execute(type, String.valueOf(userId),quickDeliveryRadioButtonText, "", address_id, useWalletCashRadioButtonText);
//                            }
//                        }
//                        else{
//                            new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select the asked Options", "RETRY AGAIN", "INFORMATION SAYS:");
//                        }
//                    }
//                }
//                else
//                {
//                    deliveryTypeRadioButtonID = delivery_type_selector.getCheckedRadioButtonId();
//                    useWalletCashRadioButtonID = use_wallet_cash_selector.getCheckedRadioButtonId();
//                    paymentTypeRadioButtonID = payment_mode_selector.getCheckedRadioButtonId();
//                    // If nothing is selected from Radio Group, then it return -1
//                    if (useWalletCashRadioButtonID != -1 && deliveryTypeRadioButtonID != -1 && paymentTypeRadioButtonID != -1) {
//                        deliveryTypeSelectedRadioButton = findViewById(deliveryTypeRadioButtonID);
//                        useWalletCashSelectedRadioButton = findViewById(useWalletCashRadioButtonID);
//
//                        deliveryTypeRadioButtonText = deliveryTypeSelectedRadioButton.getText().toString();
//                        useWalletCashRadioButtonText = useWalletCashSelectedRadioButton.getText().toString();
//
//                        String type = "book_now";
//
//                        if(firstAddressList.size() == 0)
//                        {
//                            new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Before placing an order, Please Add an Delivery Address First", "RETRY AGAIN", "INFORMATION SAYS:");
//                        }
//                        else
//                        {
//                            String address_id = firstAddressList.get(0).getId();
//                            book_now= new BackgroundWorker(PlaceOrderActivity.this);
//                            book_now.setAsyncTaskListener(this);
//
//                            if(delivery_date.isShown() && !(delivery_date.getEditText().getText().equals("")))
//                            {
//                                book_now.execute(type, String.valueOf(userId), bookingProductQuantity, bookingProductId, address_id,date_time_selected, bookingIsfresh, useWalletCashRadioButtonText);
//                            }
//                            else if(!(delivery_date.isShown()))
//                            {
//                                Intent in = getIntent();
//                                if(in.hasExtra("end_date"))
//                                {
//                                    date_time_selected = current_year+"-"+renewalMonth+"-"+renewalDay;
//                                    book_now.execute(type, String.valueOf(userId), bookingProductQuantity, bookingProductId, address_id,date_time_selected, bookingIsfresh, useWalletCashRadioButtonText);
//                                }
//                                else
//                                {
//                                    date_time_selected = current_year+"-"+(current_month+1)+"-"+current_day;
//                                    book_now.execute(type, String.valueOf(userId), bookingProductQuantity, bookingProductId, address_id,date_time_selected, bookingIsfresh, useWalletCashRadioButtonText);
//                                }
//                            }
//                        }
//                    }
//                    else{
//                        new CustomDialogs(this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select the asked Options", "RETRY AGAIN", "INFORMATION SAYS:");
//                    }
//                }
//            }
//
//        }
//        else if(v.equals(delivery_type_advanced))
//        {
//            delivery_selection_layout.setVisibility(View.VISIBLE);
//
//        }
//        else if(v.equals(delivery_type_not_advanced))
//        {
//            delivery_selection_layout.setVisibility(View.GONE);
//            delivery_date.getEditText().setText("");
//            delivery_date.setVisibility(View.GONE);
//        }
//        else if(v.equals(quick_delivery_type_yes))
//        {
//            advance_booking_layout.setVisibility(View.GONE);
//        }
//        else if(v.equals(quick_delivery_type_no))
//        {
//            advance_booking_layout.setVisibility(View.VISIBLE);
//        }
//        else if(v.equals(show_datepicker))
//        {
//            showDialog(999);
//        }
//        else if(v.equals(show_timepicker))
//        {
//            timePicker1.setVisibility(View.VISIBLE);
//        }
//        else if(v.equals(type_Cash))
//        {
//            payment_status = "Cash";
//        }
//        else if(v.equals(type_online))
//        {
//            payment_status = "Online";
//        }
//    }
//}
//
