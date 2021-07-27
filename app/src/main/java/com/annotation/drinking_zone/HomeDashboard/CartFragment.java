package com.annotation.drinking_zone.HomeDashboard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annotation.drinking_zone.AdapterLayouts.CartRecyclerViewAdapter;
import com.annotation.drinking_zone.CheckOutBillingStage.PlaceOrderActivity;
import com.annotation.drinking_zone.Login.SignInActivity;
import com.annotation.drinking_zone.Login.SignUpActivity;
import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.RecyclerViewClickHandler;
import com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment implements AsyncTaskListener, RecyclerViewClickHandler {
    View view, divider;
    RecyclerView cartRecyclerView;
    LinearLayout price_layout;
    private GridLayoutManager gridLayoutManager;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private List<ProductsData> products_data;
    UserSharedPrefDetails userSharedPrefDetails;
    int userId;
    MenuItem menuItem;
    Button proceed_to_checkout_btn;
    TextView total_price, cart_count_tv;
    Menu menu;
    BackgroundWorker backgroundWorker, deleteTask;
    String deviceToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        cartRecyclerView = view.findViewById(R.id.cart_products_list);
        products_data = new ArrayList<>();
        total_price= view.findViewById(R.id.total_price);
        divider = view.findViewById(R.id.divider);
        price_layout = view.findViewById(R.id.layout);
        proceed_to_checkout_btn = view.findViewById(R.id.proceed_to_checkout_btn);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        cartRecyclerView.setLayoutManager(gridLayoutManager);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        divider.setVisibility(View.GONE);
        price_layout.setVisibility(View.GONE);
        proceed_to_checkout_btn.setVisibility(View.GONE);

        userSharedPrefDetails = new UserSharedPrefDetails(view.getContext());
        userId = userSharedPrefDetails.getUser_Id();

        if (!SharedPreferenceManagerFCM.getInstance(getActivity()).getToken().equals("")) {
            Log.i("tokenTestingIf", "onCreate: " + SharedPreferenceManagerFCM.getInstance(getActivity()).getToken());
            deviceToken = SharedPreferenceManagerFCM.getInstance(getActivity()).getToken(); // need to pass this token id while caling login api
            Log.i("DeviceTockenIf", "onCreate: " + deviceToken);
        } else {
            deviceToken = SharedPreferenceManagerFCM.getInstance(getActivity()).getToken();
            Log.i("tokenTestingIf", "onCreate: " + SharedPreferenceManagerFCM.getInstance(getActivity()).getToken());
            Log.i("DeviceokenElse", "onCreate: " + deviceToken);
        }

        String type = "view_cart";
        backgroundWorker = new BackgroundWorker(getActivity());
        backgroundWorker.setAsyncTaskListener(this);
        if (userId == 0) {
            backgroundWorker.execute(type, "", deviceToken);
        } else
        {
            backgroundWorker.execute(type, Integer.toString(userId), deviceToken);
        }


        proceed_to_checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userId ==0)
                {
                    new CustomDialogs(getActivity(), R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Not Registered Yet, Please Register and Proceed.", "Ok", "INFORMATION SAYS:", SignUpActivity.class);
                }
                else
                {
                    Intent intent = new Intent(getActivity().getApplicationContext(), PlaceOrderActivity.class);
                    startActivity(intent);
                }

            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("##tag", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//                    Log.i("##confirmation", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    if(getActivity().getIntent().hasExtra("Help"))
                    {
                        Intent intent = new Intent(getActivity().getApplicationContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else
                    {
                        Intent intent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }

        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuItem = menu.findItem(R.id.cart_menu_icon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i("TAG", "onOptionsItemSelected: "+item);
        Log.i("check_id", "onCreateOptionsMenu: "+item.getItemId());
        if (id == R.id.cart_menu_icon) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void updateResult(String result) {

        Log.i("#Result All Cart", "updateResult: " + result);

        if(result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);
//            int success = jsonResult.getInt("success");
                boolean success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {
                    if (jsonResult.has("msg")) {
                        String message = jsonResult.getString("msg");
                        new CustomDialogs(view.getContext(), R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "RETRY AGAIN", "INFORMATION SAYS:");
//                        cart_count_tv.setText(String.valueOf(userSharedPrefDetails.getCart_count()));
                    } else {
                        new CustomDialogs(view.getContext(), R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Nothing is yet Added to cart", "Lets Shop", "INFORMATION SAYS:", HomeActivity.class);
//                        cart_count_tv.setText(String.valueOf(userSharedPrefDetails.getCart_count()));
                    }
                } else {
                    if (jsonResult.has("msg")) {
                        String message = jsonResult.getString("msg");
                        new CustomDialogs(view.getContext(), R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "INFORMATION SAYS:");
                        cartRecyclerViewAdapter.notifyDataSetChanged();
                        String type = "view_cart";
                        backgroundWorker = new BackgroundWorker(getActivity());
                        backgroundWorker.setAsyncTaskListener(this);
                        if (userId == 0) {
                            backgroundWorker.execute(type, "", deviceToken);
                        } else
                        {
                            backgroundWorker.execute(type, Integer.toString(userId), deviceToken);
                        }
                    } else if (jsonResult.has("data")) {
                        JSONArray productsDetails = jsonResult.getJSONArray("data");
                        if (productsDetails.length() != 0) {
                            divider.setVisibility(View.VISIBLE);
                            price_layout.setVisibility(View.VISIBLE);
                            proceed_to_checkout_btn.setVisibility(View.VISIBLE);
                            products_data.clear();
                            for (int i = 0; i < productsDetails.length(); i++) {
                                JSONObject single_product = productsDetails.getJSONObject(i);
                                String cartId = single_product.getString("Id");
                                String productId = single_product.getString("ProductId");
                                String product_name = single_product.getString("Name");
                                String product_price = single_product.getString("Price");
                                String product_deposit = single_product.getString("Deposit");
                                String productImg_link = single_product.getString("FilePath");
                                String product_quantity = single_product.getString("Quantity");
                                String product_status = single_product.getString("ProductStatus");
                                String upto_quantity = single_product.getString("UptoQuantity");
                                String product_isFresh = single_product.getString("IsFresh");
                                String product_subTotal = single_product.getString("SubTotal");

                                ProductsData productsData = new ProductsData(cartId, productId, product_name, product_price, product_deposit, productImg_link, product_quantity, product_isFresh, upto_quantity, product_subTotal, product_status);
                                products_data.add(productsData);
                            }

                            int cartCount = productsDetails.length();
                            userSharedPrefDetails.setCart_count(cartCount);
                            writeBadge(cartCount);
//                            cart_count_tv.setText(String.valueOf(userSharedPrefDetails.getCart_count()));
                            String product_total = jsonResult.getString("Total");
                            total_price.setText(product_total);

                            cartRecyclerViewAdapter = new CartRecyclerViewAdapter(view.getContext(), products_data, this);
                            cartRecyclerView.setAdapter(cartRecyclerViewAdapter);
                            cartRecyclerViewAdapter.notifyDataSetChanged();


                        } else {
                            divider.setVisibility(View.GONE);
                            price_layout.setVisibility(View.GONE);
                            proceed_to_checkout_btn.setVisibility(View.GONE);
                            new CustomDialogs(view.getContext(), R.color.colorgreen, R.drawable.ic_mood_black_24dp, "Nothing is yet Added to cart", "OK", "INFORMATION SAYS:", getActivity().getClass());
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
        String type = "delete_cart";
        deleteTask = new BackgroundWorker(getActivity());
        deleteTask.setAsyncTaskListener(this);
        if (userId == 0) {
            deleteTask.execute(type, cid, "", deviceToken);
        } else
        {
            deleteTask.execute(type, cid, Integer.toString(userId), deviceToken);
        }
//        deleteTask.execute(type, cid, Integer.toString(userId));
    }

    @Override
    public void onQuantityUpdated(int position, String quantity) {

        String cartId = products_data.get(position).getId();
//        String cartId = Integer.toString(position);
        String product_id = products_data.get(position).getProduct_id();
        String product_quantity = quantity;
        String isFresh = products_data.get(position).getProduct_isFresh();
        String type = "update_cart";

        Log.i("Strings", "onQuantityUpdated: "+cartId+" "+product_id+" "+product_quantity+" "+isFresh+" "+userId);
        BackgroundWorker updateCartTask = new BackgroundWorker(getActivity());
        updateCartTask.setAsyncTaskListener(this);
        if (userId == 0) {
            updateCartTask.execute(type, cartId, "", product_id, product_quantity, isFresh, deviceToken);
        } else
        {
            updateCartTask.execute(type, cartId, Integer.toString(userId), product_id, product_quantity, isFresh, deviceToken);
        }

    }
}

