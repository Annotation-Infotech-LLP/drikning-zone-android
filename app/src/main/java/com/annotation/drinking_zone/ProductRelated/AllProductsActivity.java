package com.annotation.drinking_zone.ProductRelated;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annotation.drinking_zone.AdapterLayouts.ProductsViewRecyclerAdapter;
import com.annotation.drinking_zone.HomeDashboard.CartFragment;
import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity implements AsyncTaskListener {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ProductsViewRecyclerAdapter recyclerAdapter;
    private List<ProductsData> products_data;
    int category, userId;
    TextView products_heading, cart_count_tv;
    String category_name;
    Menu menu;
    MenuItem menuItem;
    String type;
    int cartCount = 0;
    BackgroundWorker backgroundWorker;
    UserSharedPrefDetails userSharedPrefDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        recyclerView = findViewById(R.id.products_display);
        products_heading = findViewById(R.id.p_heading);
        products_data = new ArrayList<>();

        userSharedPrefDetails = new UserSharedPrefDetails(this);
        userId = userSharedPrefDetails.getUser_Id();


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String type = "get_all_products";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.setAsyncTaskListener(this);
        if(intent.hasExtra("product_category") && intent.hasExtra("product_category_name"))
        {
            category = b.getInt("product_category");
            category_name = b.getString("product_category_name");
            products_heading.setText(category_name);
            if(category == 0 || category == 1 || category == 2 || category == 3)
            {
                backgroundWorker.execute(type, Integer.toString(category));
            }
            else if(category == 4)
            {
                String package_type = "get_bookable_products";
                backgroundWorker.execute(package_type);
            }
        }
        else
        {
            category = 0;
            category_name = "All Products";
            products_heading.setText(category_name);
            backgroundWorker.execute(type, Integer.toString(category));
        }
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        this.menu = menu;
        if(menu != null)
        {
            type = "view_cart";
//            backgroundWorker = new BackgroundWorker(this);
//            backgroundWorker.setAsyncTaskListener(this);
//            backgroundWorker.execute(type, Integer.toString(userId));
//            notifCountType = "get_notifications_count";
//            backgroundWorker = new BackgroundWorker(this);
//            backgroundWorker.setAsyncTaskListener(this);
//            backgroundWorker.execute(notifCountType, Integer.toString(userId));
        }
        else
        {
            getMenuInflater().inflate(R.menu.cart_menu, menu);
            this.menu = menu;
            Toast.makeText(this, "Didnt set menu properly", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i("TAG", "onOptionsItemSelected: "+item);
        Log.i("check_id", "onCreateOptionsMenu: "+item.getItemId());
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

        menuItem = menu.findItem(R.id.cart_menu_icon).setVisible(true);
        Log.i("Check", "onCreateOptionsMenu: "+menuItem.getItemId());
//            writeBadge(userSharedPrefDetails.getNotification_count());

        Log.i("writeBage", "writeBadge: "+menuItem.getItemId());
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AllProductsActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void updateResult(String result) {
        Log.i("#Result All product", "updateResult: " + result);
//                Parsing JSON DATA
        if(result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);
//            int success = jsonResult.getInt("success");
                boolean success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {

                    Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();

                } else{
                    JSONArray productsDetails = jsonResult.getJSONArray("data");
                    for (int i = 0; i < productsDetails.length(); i++) {
                        JSONObject single_product = productsDetails.getJSONObject(i);
                        String productId = single_product.getString("Id");
                        String product_name = single_product.getString("Name");
                        String product_category = single_product.getString("CategoryId");
                        String product_category_name = single_product.getString("Category");
                        String product_price = single_product.getString("Price");
                        String product_deposit = single_product.getString("Deposit");
                        String productImg_link = single_product.getString("FilePath");
                        Log.i("product", "updateResult: " + productImg_link);
                        String product_upto_quantity = single_product.getString("UptoQuantity");
                        String product_min_quantity = single_product.getString("MinQuantity");
                        String product_max_quantity = single_product.getString("MaxQuantity");

                        if (category == 0 || category == 1 || category == 2 || category == 3) {
                            ProductsData productsData = new ProductsData(productId, product_category, product_category_name, product_name, product_price, product_deposit, productImg_link, product_upto_quantity, product_min_quantity, product_max_quantity);
                            products_data.add(productsData);
                        } else if (category == 4) {
                            ProductsData productsData = new ProductsData(productId, product_category, product_category_name, product_name, product_price, product_deposit, productImg_link, product_upto_quantity, product_min_quantity, product_max_quantity, category);
                            products_data.add(productsData);
                        }
                    }
                    recyclerAdapter = new ProductsViewRecyclerAdapter(this, products_data);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
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
