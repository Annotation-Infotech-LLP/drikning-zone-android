package com.annotation.drinking_zone.ProductRelated;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annotation.drinking_zone.HomeDashboard.CartFragment;
import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.Login.SignUpActivity;
import com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM;
import com.bumptech.glide.Glide;
import com.annotation.drinking_zone.CheckOutBillingStage.PlaceOrderActivity;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.InputFilterMinMax;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleProductActivity extends AppCompatActivity implements AsyncTaskListener {

    int count = 0;
    RadioGroup type_selector;
    int selectedRadioButtonID;
    String selectedRadioButtonText;
    RadioButton selectedRadioButton;
    String product_quantity;
    TextView first_quantity, second_quantity, third_quantity, first_price, second_price, third_price, tonly, sonly, first_start_quantity, second_start_quantity, last_start_quantity;
    String image_url, product_id, product_name, product_price, product_deposit, product_category, product_upto_quantity, product_min_quantity, product_max_quantity;
    int product_reference_category;
    LinearLayout category_layout, deposit_layout, refill_layout;
    TextView cart_count_tv;
    int userId;
    MenuItem menuItem;
    UserSharedPrefDetails userSharedPrefDetails;
    String deviceToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        userSharedPrefDetails = new UserSharedPrefDetails(this);
        userId = userSharedPrefDetails.getUser_Id();
        if(!SharedPreferenceManagerFCM.getInstance(SingleProductActivity.this).getToken().equals(""))
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


        getProductDataIntents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        menuItem = menu.findItem(R.id.cart_menu_icon);
        writeBadge(userSharedPrefDetails.getCart_count());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart_menu_icon) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("Cart", "Cart");
            startActivity(intent);
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeBadge(int count) {

        MenuItemCompat.setActionView(menuItem, R.layout.actionbar_badge_layout);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(menuItem);

        cart_count_tv = notifCount.findViewById(R.id.actionbar_textview);

        userSharedPrefDetails.setCart_count(count);
        if(userSharedPrefDetails.getCart_count() == 0)
        {
            cart_count_tv.setVisibility(View.GONE);
        }
        else
        {
            cart_count_tv.setVisibility(View.VISIBLE);
            cart_count_tv.setText(String.valueOf(userSharedPrefDetails.getCart_count()));
        }
        // An icon, it also must be clicked.
        ImageView imageView = (ImageView) notifCount.findViewById(R.id.image);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        };
        menuItem.getActionView().setOnClickListener(onClickListener);
        imageView.setOnClickListener(onClickListener);
    }

    private void getProductDataIntents()
    {
        Intent intent = getIntent();
        if(intent.hasExtra("product_id") && intent.hasExtra("product_name") && intent.hasExtra("product_price") &&
                intent.hasExtra("product_deposit") && intent.hasExtra("product_category") && intent.hasExtra("product_image_link")
                && intent.hasExtra("product_upto_quantity") && intent.hasExtra("product_min_quantity") && intent.hasExtra("product_max_quantity") && !(intent.hasExtra("product_reference_category")))
        {
            image_url = intent.getStringExtra("product_image_link");
            product_id = intent.getStringExtra("product_id");
            product_name = intent.getStringExtra("product_name");
            product_price = intent.getStringExtra("product_price");
            product_deposit = intent.getStringExtra("product_deposit");
            product_category = intent.getStringExtra("product_category");
            product_upto_quantity = intent.getStringExtra("product_upto_quantity");
            product_min_quantity = intent.getStringExtra("product_min_quantity");
            product_max_quantity = intent.getStringExtra("product_max_quantity");

            setDataToLayouts(image_url, product_id, product_name, product_price, product_deposit, product_upto_quantity, product_min_quantity, product_max_quantity);
        }
        else if(intent.hasExtra("product_id") && intent.hasExtra("product_name") && intent.hasExtra("product_price") &&
                intent.hasExtra("product_deposit") && intent.hasExtra("product_category") && intent.hasExtra("product_image_link")
                && intent.hasExtra("product_upto_quantity") && intent.hasExtra("product_min_quantity") && intent.hasExtra("product_max_quantity")
                && intent.hasExtra("product_reference_category"))
        {
            image_url = intent.getStringExtra("product_image_link");
            product_id = intent.getStringExtra("product_id");
            product_name = intent.getStringExtra("product_name");
            product_price = intent.getStringExtra("product_price");
            product_deposit = intent.getStringExtra("product_deposit");
            product_category = intent.getStringExtra("product_category");
            product_upto_quantity = intent.getStringExtra("product_upto_quantity");
            product_min_quantity = intent.getStringExtra("product_min_quantity");
            product_max_quantity = intent.getStringExtra("product_max_quantity");
            product_reference_category = intent.getIntExtra("product_reference_category", -1);

            setDataToLayouts(image_url, product_id, product_name, product_price, product_deposit, product_upto_quantity, product_min_quantity, product_max_quantity);
            refill_layout.setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(SingleProductActivity.this, "Error in loading Data as there are no incoming Intents", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataToLayouts(final String image_url, final String product_id, final String product_name, final String product_price, final String product_deposit, final String product_upto_quantity, final String product_min_quantity, final String product_max_quantity)
    {
        final int prodt_id = Integer.parseInt(product_id);
        ImageView single_product_img = findViewById(R.id.product_img);
        TextView single_product_name = findViewById(R.id.sproduct_name);
        TextView single_product_price = findViewById(R.id.sproduct_price);
        TextView single_product_deposit = findViewById(R.id.sproduct_deposit);
        TextView single_product_category = findViewById(R.id.sproduct_category);
        TextView bottles_info = findViewById(R.id.bottles_info);
        TextView prices_info = findViewById(R.id.prices_info);
        final Button add_to_cart = findViewById(R.id.add_to_cart_btn);
        category_layout = findViewById(R.id.category_layout);
        refill_layout = findViewById(R.id.refill_layout);
        deposit_layout = findViewById(R.id.deposit_layout);
        type_selector = findViewById(R.id.type_selector);
        first_quantity = findViewById(R.id.first_quantity);
        second_quantity = findViewById(R.id.second_quantity);
        third_quantity = findViewById(R.id.third_quantity);
        first_price = findViewById(R.id.first_price);
        second_price = findViewById(R.id.second_price);
        third_price = findViewById(R.id.third_price);
        tonly = findViewById(R.id.tonly);
        sonly = findViewById(R.id.sonly);
        first_start_quantity = findViewById(R.id.initial_starts_from_quantity);
        second_start_quantity = findViewById(R.id.second_initial_quantity);
        last_start_quantity = findViewById(R.id.last_initial_quantity);

        Glide.with(SingleProductActivity.this)
                .asBitmap().load(image_url).into(single_product_img);
        single_product_name.setText(product_name);
        single_product_price.setText(product_price);
        bottles_info.setText("The Quantity should be between "+product_min_quantity+" to "+product_max_quantity+".");
        prices_info.setText("The Prices Vary as per the range of Quantity \n Accordingly overall cost would be calculated. \n\n The Jar Will be delivered at 11 a.m. daily and previous jar will be collected at the same time.");

        category_layout.setVisibility(View.GONE);

        String type = "get_price_chart";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.setAsyncTaskListener(this);
        backgroundWorker.execute(type, product_id);

        if(product_reference_category > 0)
        {
            add_to_cart.setText("BOOK NOW");
        }

        if(Integer.parseInt(product_deposit)== 0)
        {
            deposit_layout.setVisibility(View.GONE);
            refill_layout.setVisibility(View.GONE);
        }
        else
        {
            refill_layout.setVisibility(View.VISIBLE);
            single_product_deposit.setText(product_deposit);
        }

        ImageButton quantity_inc = findViewById(R.id.product_quantity_up);
        final ImageButton quantity_dec = findViewById(R.id.product_quantity_down);
        final EditText quantity = findViewById(R.id.sproduct_quantity);
        quantity.setFilters(new InputFilter[]{ new InputFilterMinMax(product_min_quantity, product_max_quantity)});
        quantity.setText(String.valueOf(product_min_quantity));

        quantity_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity.getText().toString().equals(""))
                {
                    quantity.setText(String.valueOf(product_min_quantity));
                }
                count = Integer.parseInt(String.valueOf(quantity.getText()));

                if(count < Integer.parseInt(String.valueOf(product_max_quantity)))
                {
                    count++;
                    Log.i("inc count", "onClick: "+count);
                    quantity.setText(String.valueOf(count));
                }
                else if(count == Integer.parseInt(String.valueOf(product_max_quantity)))
                {
                    quantity.setText(String.valueOf(count));
                    Toast.makeText(SingleProductActivity.this, "The Maximum Quantity can be only Upto "+product_max_quantity, Toast.LENGTH_LONG).show();
                }
                else
                {
                    quantity.setText(String.valueOf(count));
                    Toast.makeText(SingleProductActivity.this, "The Maximum Quantity can be only upto "+product_max_quantity, Toast.LENGTH_LONG).show();
                }
            }
        });

        quantity_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(quantity.getText().toString().equals(""))
                {
                    quantity.setText(String.valueOf(product_min_quantity));
                }
                count = Integer.parseInt(String.valueOf(quantity.getText()));
                if(count>Integer.parseInt(String.valueOf(product_min_quantity)))
                {
                    count--;
                    quantity.setText(String.valueOf(count));
                }
                else if(count == Integer.parseInt(String.valueOf(product_min_quantity)))
                {
                    quantity.setText(String.valueOf(count));
                    Toast.makeText(SingleProductActivity.this, "The Minimum Quantity can be "+product_min_quantity, Toast.LENGTH_LONG).show();
                }
                else
                {
                    quantity.setText(String.valueOf(count));
                    Toast.makeText(SingleProductActivity.this, "The minimum Quantity can be "+product_min_quantity, Toast.LENGTH_LONG).show();
                }
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(add_to_cart.getText().equals("BOOK NOW") && userId == 0)
                {
                    new CustomDialogs(SingleProductActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Not Registered Yet, Please Register and Proceed.", "Ok", "INFORMATION SAYS:", SignUpActivity.class);
                }
                else
                {
                    if(Integer.parseInt(product_deposit) != 0)
                    {
                        if(refill_layout.isShown())
                        {
                            selectedRadioButtonID = type_selector.getCheckedRadioButtonId();

                            if (selectedRadioButtonID != -1 && !(quantity.getText().toString().equals("")) && product_reference_category < 4) {
                                selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                                selectedRadioButtonText = selectedRadioButton.getText().toString();

                                product_quantity = quantity.getText().toString();
                                int cartId = 0;
                                String type = "update_cart";
                                BackgroundWorker backgroundWorker = new BackgroundWorker(SingleProductActivity.this);
                                backgroundWorker.setAsyncTaskListener(SingleProductActivity.this);

                                if(userId == 0)
                                {
                                    if(selectedRadioButtonText.equals("Yes") )
                                    {
                                        backgroundWorker.execute(type, Integer.toString(cartId), "", Integer.toString(prodt_id), product_quantity, "No", deviceToken);
                                    }
                                    else if(selectedRadioButtonText.equals("No"))
                                    {
                                        backgroundWorker.execute(type, Integer.toString(cartId), "", Integer.toString(prodt_id), product_quantity, "Yes", deviceToken);
                                    }
                                }
                                else
                                {
                                    if(selectedRadioButtonText.equals("Yes") )
                                    {
                                        backgroundWorker.execute(type, Integer.toString(cartId), Integer.toString(userId), Integer.toString(prodt_id), product_quantity, "No", deviceToken);
                                    }
                                    else if(selectedRadioButtonText.equals("No"))
                                    {
                                        backgroundWorker.execute(type, Integer.toString(cartId), Integer.toString(userId), Integer.toString(prodt_id), product_quantity, "Yes", deviceToken);
                                    }
                                }

                            }
                            else{
                                new CustomDialogs(SingleProductActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select the Refill Type or Quantity cannot be Empty", "RETRY AGAIN", "INFORMATION SAYS:");
                            }
                        }
                        else if(!(refill_layout.isShown()))
                        {
                            if(product_reference_category > 0 && !(quantity.getText().toString().equals("")))
                            {
                                product_quantity = quantity.getText().toString();

                                int durationHome = 30;
                                int durationOffice = 26;
                                int total_priceHome = 0, total_priceOffice = 0;
                                int f_price = Integer.parseInt(first_price.getText().toString());
                                int s_price = Integer.parseInt(second_price.getText().toString());
                                int t_price = Integer.parseInt(third_price.getText().toString());
                                int deposit_amt = Integer.parseInt(product_deposit);
                                int p_int_quantity = Integer.parseInt(product_quantity);
                                if( p_int_quantity > 0 && p_int_quantity < 5)
                                {
                                    total_priceHome = (p_int_quantity * f_price * durationHome) + (deposit_amt * p_int_quantity);
                                    total_priceOffice = (p_int_quantity * f_price * durationOffice) + (deposit_amt * p_int_quantity);
                                }
                                else if(p_int_quantity > 4 && p_int_quantity < 10)
                                {
                                    total_priceHome = (p_int_quantity * s_price * durationHome) + (deposit_amt * p_int_quantity);
                                    total_priceOffice = (p_int_quantity * s_price * durationOffice) + (deposit_amt * p_int_quantity);
                                }
                                else if(p_int_quantity > 9 && p_int_quantity < 101)
                                {
                                    total_priceHome = (p_int_quantity * t_price * durationHome) + (deposit_amt * p_int_quantity);
                                    total_priceOffice = (p_int_quantity * t_price * durationOffice) + (deposit_amt * p_int_quantity);
                                }

                                Log.i("Testing", "Home"+total_priceHome+" \n Office: "+total_priceOffice);
                                Intent intent = new Intent(SingleProductActivity.this, PlaceOrderActivity.class);
                                intent.putExtra("product_category_reference", product_reference_category);
                                intent.putExtra("product_id", product_id);
                                intent.putExtra("product_image_link", image_url);
                                intent.putExtra("product_name", product_name);
                                intent.putExtra("product_price", product_price);
                                intent.putExtra("product_deposit", product_deposit);
                                intent.putExtra("product_total_priceHome", total_priceHome);
                                intent.putExtra("product_total_priceOffice", total_priceOffice);
                                intent.putExtra("product_quantity", product_quantity);
                                intent.putExtra("isFresh", "Yes");
                                startActivity(intent);
                            }
                            else{
                                new CustomDialogs(SingleProductActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Please Select the Quantity, It cannot be Empty", "RETRY AGAIN", "INFORMATION SAYS:");
                            }
                        }


                    }
                    else
                    {
                        product_quantity = quantity.getText().toString();
                        if(!(quantity.getText().toString().equals("")))
                        {
                            UserSharedPrefDetails userSharedPrefDetails = new UserSharedPrefDetails(SingleProductActivity.this);
                            int cartId = 0;
                            String isFresh = "No";
                            String type = "update_cart";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(SingleProductActivity.this);
                            backgroundWorker.setAsyncTaskListener(SingleProductActivity.this);
                            if(userId ==0)
                            {
                                backgroundWorker.execute(type, Integer.toString(cartId), "", Integer.toString(prodt_id), product_quantity, isFresh, deviceToken);
                            }
                            else
                            {
                                backgroundWorker.execute(type, Integer.toString(cartId), Integer.toString(userId), Integer.toString(prodt_id), product_quantity, isFresh, deviceToken);
                            }

                        }
                        else
                        {
                            new CustomDialogs(SingleProductActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Quantity cannot be Empty", "RETRY AGAIN", "INFORMATION SAYS:");
                        }
                    }
                }
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void updateResult(String result) {
        if(result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);

                boolean success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {
                    String message = jsonResult.getString("msg");
//                Log.i("cart msg", "updateResult: "+message);
//                displaying the message from the server dynamically in alert box
                    new CustomDialogs(SingleProductActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "RETRY AGAIN", "INFORMATION SAYS:");
                } else {
                    if (jsonResult.has("msg")) {
                        String message = jsonResult.getString("msg");
                        new CustomDialogs(SingleProductActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "CONTINUE SHOPPING", "INFORMATION SAYS:", HomeActivity.class);
                    } else if (jsonResult.has("data")) {
                        JSONArray price_quantity_array = jsonResult.getJSONArray("data");
                        int arrayLength = price_quantity_array.length();
                        Log.i("arrayLength", "updateResult: " + price_quantity_array.length());
                        if (arrayLength == 1) {
                            second_quantity.setVisibility(View.GONE);
                            second_price.setVisibility(View.GONE);
                            second_start_quantity.setVisibility(View.GONE);
                            third_price.setVisibility(View.GONE);
                            third_quantity.setVisibility(View.GONE);
                            last_start_quantity.setVisibility(View.GONE);
                            tonly.setVisibility(View.GONE);
                            sonly.setVisibility(View.GONE);
                        } else if (arrayLength == 2) {
                            JSONObject price_quantity_object = price_quantity_array.getJSONObject(0);
                            String quantity = price_quantity_object.getString("UptoQuantity");
                            int integerQuantity = Integer.parseInt(quantity);
                            second_start_quantity.setText((integerQuantity + 1) + " - ");
                            third_price.setVisibility(View.GONE);
                            third_quantity.setVisibility(View.GONE);
                            last_start_quantity.setVisibility(View.GONE);
                            tonly.setVisibility(View.GONE);
                        }

                        for (int i = 0; i < arrayLength; i++) {
                            JSONObject price_quantity_object = price_quantity_array.getJSONObject(i);
                            String price = price_quantity_object.getString("Price");
                            String quantity = price_quantity_object.getString("UptoQuantity");
                            int integerQuantity = Integer.parseInt(quantity);

                            if (second_quantity.isShown() && third_quantity.isShown()) {
                                if (integerQuantity > 0 && integerQuantity < 6) {
                                    first_quantity.setText(quantity);
                                    first_price.setText(price);
                                    second_start_quantity.setText((integerQuantity + 1) + " - ");
                                } else if (integerQuantity > 5 && integerQuantity < 10) {
                                    second_quantity.setText(quantity);
                                    second_price.setText(price);
                                    last_start_quantity.setText((integerQuantity + 1) + " - ");
                                } else if (integerQuantity > 10 && integerQuantity < 101) {
                                    third_quantity.setText(quantity);
                                    third_price.setText(price);
                                }
                            } else if (second_quantity.isShown()) {
                                if (integerQuantity > 0 && integerQuantity < 11) {
                                    first_quantity.setText(quantity);
                                    first_price.setText(price);
                                    second_start_quantity.setText((integerQuantity + 1) + " - ");
                                } else if (integerQuantity > 10 && integerQuantity < 101) {
                                    second_quantity.setText(quantity);
                                    second_price.setText(price);
                                }
                            } else if (arrayLength == 1) {
                                first_quantity.setText(quantity);
                                first_price.setText(price);
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
}

