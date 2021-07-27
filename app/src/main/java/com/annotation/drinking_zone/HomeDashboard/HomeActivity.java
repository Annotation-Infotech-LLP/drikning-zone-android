package com.annotation.drinking_zone.HomeDashboard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.annotation.drinking_zone.AdapterLayouts.CartRecyclerViewAdapter;
import com.annotation.drinking_zone.Login.SignInActivity;
import com.annotation.drinking_zone.Login.SignUpActivity;
import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AsyncTaskListener {
    TextView user_fullname, user_email;
    String referralCode, checkUpdatType;
    private DrawerLayout drawer;
    Dialog alertDialog;
    Button alert_btn;
    TextView alert_title;
    TextView alert_message, notif_count;
    TextView cart_count_tv;
    ImageView alert_image;
    int userId = 0;
    int cartCount = 0;
    static  int count =0;
    BackgroundWorker backgroundWorker, updateWorker;
    MenuItem menuItem;
    Menu menu;
    UserSharedPrefDetails userSharedPrefDetails;
    LayoutInflater inflater;
    View alertLayout;
    TextView updateTitle;
    TextView updateNotes;
    TextView versionCode1 ;
    TextView updateButton;
    TextView cancelButton ;
    android.app.AlertDialog.Builder alert;
    AlertDialog dialog;
    String type, deviceToken = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        fetching the views
        NavigationView navigationView= findViewById(R.id.nav_view);
        user_fullname=navigationView.getHeaderView(0).findViewById(R.id.user_fullname);
        user_email=navigationView.getHeaderView(0).findViewById(R.id.user_email);
//        setting toolbar actions
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        if(!SharedPreferenceManagerFCM.getInstance(HomeActivity.this).getToken().equals(""))
        {
            Log.i("tokenTestingIf", "onCreate: "+SharedPreferenceManagerFCM.getInstance(HomeActivity.this).getToken());
            deviceToken = SharedPreferenceManagerFCM.getInstance(this).getToken(); // need to pass this token id while caling login api
            Log.i("DeviceTockenIf", "onCreate: "+deviceToken);
        }
        else
        {
            deviceToken = SharedPreferenceManagerFCM.getInstance(this).getToken();
            Log.i("tokenTestingIf", "onCreate: "+SharedPreferenceManagerFCM.getInstance(HomeActivity.this).getToken());
            Log.i("DeviceokenElse", "onCreate: "+deviceToken);
        }




        if(count == 0)
        {
            checkUpdate();
        }

        userSharedPrefDetails = new UserSharedPrefDetails(HomeActivity.this);
        String name =userSharedPrefDetails.getUser_name();
        String email =userSharedPrefDetails.getUser_email();
        userId = userSharedPrefDetails.getUser_Id();
        referralCode = userSharedPrefDetails.getUser_referral_code();

//        if(userId == 0 && !getIntent().hasExtra("Help"))
//        {
//            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
//            startActivity(intent);
//            finish();
//        }

        if(userId == 0 && !deviceToken.equals(""))
        {
            user_fullname.setText("Not an User? SIGN IN");
            user_email.setText("Please Register");

            navigationView.getMenu().findItem(R.id.nav_my_orders).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_my_wallet).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_orders).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_refer_friend).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }
        else
        {
            user_fullname.setText(name);
            user_email.setText(email);
        }

        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) HomeActivity.this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(getIntent().hasExtra("Help"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HelpFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_help);
            toolbar.setVisibility(View.GONE);
        }
        else if(getIntent().hasExtra("Cart"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_cart);
        }
        else if(getIntent().hasExtra("Contact"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }
        else
        {
            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MenuFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_menu);
            }
        }

        alertDialog = new Dialog(HomeActivity.this);
        alertDialog.setContentView(R.layout.epic_popup_positive);
        alert_btn = alertDialog.findViewById(R.id.pos_btn_ok);
        alert_title = alertDialog.findViewById(R.id.pos_pop_title);
        alert_message = alertDialog.findViewById(R.id.pos_pop_msg);
        alert_image = alertDialog.findViewById(R.id.alert_img);
        alert_title.setText("INFORMATION SAYS:");
        alert_title.setTextColor(this.getResources().getColor(R.color.colorRedDark));
        alert_image.setImageResource(R.drawable.ic_mood_bad_black_24dp);
        alert_message.setText("Do you want to Exit Drinking Zone?");
        alert_message.setTextColor(this.getResources().getColor(R.color.colorRedDark));
        alert_btn.setText("Yes");
        alert_btn.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(R.color.colorRedDark)));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });


        user_fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userId == 0)
                {
                    Intent registerIntent = new Intent(HomeActivity.this, SignUpActivity.class);
                    startActivity(registerIntent);
                    finish();
                }
                else
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        this.menu = menu;
        writeBadge(userSharedPrefDetails.getCart_count());
        if(menu != null)
        {
            type = "view_cart";
            backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.setAsyncTaskListener(this);

            if(userId == 0)
            {
                backgroundWorker.execute(type,"", deviceToken);
            }
            else
            {
                backgroundWorker.execute(type, Integer.toString(userId), deviceToken);
            }


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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeBadge(int count) {

        menuItem = menu.findItem(R.id.cart_menu_icon).setVisible(true);
//            writeBadge(userSharedPrefDetails.getNotification_count());

        MenuItemCompat.setActionView(menuItem, R.layout.actionbar_badge_layout);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(menuItem);

        notif_count = notifCount.findViewById(R.id.actionbar_textview);

        userSharedPrefDetails.setCart_count(count);
        if(userSharedPrefDetails.getCart_count() == 0)
        {
//            notif_count.setVisibility(View.INVISIBLE);
            notif_count.setVisibility(View.GONE);

        }
        else
        {
            notif_count.setVisibility(View.VISIBLE);
            notif_count.setText(String.valueOf(userSharedPrefDetails.getCart_count()));
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
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            if(getIntent().hasExtra("Help"))
            {
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                alertDialog.show();
            }


        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId())
        {
            case R.id.nav_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MenuFragment()).commit();
                break;
            case R.id.nav_cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
                break;
            case R.id.nav_orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyBookingsFragment()).commit();
                break;
            case R.id.nav_my_orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyOrdersFragment()).commit();
                break;
            case R.id.nav_my_wallet:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyWalletFragment()).commit();
                break;
            case R.id.nav_aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUsFragment()).commit();
                break;
            case R.id.nav_privacy:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrivacyFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HelpFragment()).commit();
                break;
            case R.id.nav_faq:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FAQFragment()).commit();
                break;
            case R.id.nav_refer_friend:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String urlLink = "https://play.google.com/store/apps/details?id=com.annotation.drinking_zone";
                String textualInfo = "Hey, Its a best app for ordering all type of drinkables which includes Water Cans, Soft Drinks, Water Bottles, etc. for corporate as well as domestic purposes. Everything in very reasonable cost. Get a discount of rupees 20/- on first purchase. Also refer to your friends after downloading this app and earn Rupees 20/- on each friend.";
                String textinfo2 = "Use my referral code and get started. Hurry up..!! My Referral Code is: ";
                String referralCodeText = referralCode;
                sendIntent.putExtra(Intent.EXTRA_TEXT,textualInfo+" \n\n "+textinfo2+"*"+referralCodeText+"*"+"\n\n "+urlLink);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.nav_terms_conditions:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TermsAndConditionsFragment()).commit();
                break;
            case R.id.nav_logout:
                new UserSharedPrefDetails(HomeActivity.this).removeUser();
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateResult(String result) {
//        Log.i("#Result All Cart", "updateResult: " + result);

        if(result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);
//            int success = jsonResult.getInt("success");
                boolean success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {
                    if(jsonResult.has("data") && jsonResult.get("data") instanceof JSONObject)
                    {
                        JSONObject object = jsonResult.getJSONObject("data");
                    }
                    else if(jsonResult.has("Total"))
                    {
                        userSharedPrefDetails.setCart_count(0);
                        writeBadge(0);
                        Log.i("FalseCount", "updateResult: Entered Count");
                    }
                    else
                    {
                        userSharedPrefDetails.setCart_count(0);
                        writeBadge(0);
                    }

                } else {
                    if (jsonResult.has("data") && jsonResult.get("data") instanceof JSONArray && jsonResult.has("Total")) {
                        JSONArray productsDetails = jsonResult.getJSONArray("data");
                        Log.i("Cond1", "updateResult: Entered this");
                        if (productsDetails.length() != 0) {
                            cartCount = productsDetails.length();
                            Log.i("CartCount", "updateResult: "+cartCount);
                            userSharedPrefDetails.setCart_count(cartCount);
                            writeBadge(cartCount);
                        } else {
                            userSharedPrefDetails.setCart_count(0);
                        }
                    }
                    else if (jsonResult.has("data") && jsonResult.get("data")  instanceof JSONArray && !jsonResult.has("Total")) {
                        JSONArray updateDetails = jsonResult.getJSONArray("data");
                        Log.i("UpdaTE", "updateResult: Entered Update");
                        if(updateDetails.getJSONObject(0).has("IsForceUpdate"))
                        {
                            JSONObject object = updateDetails.getJSONObject(0);
                            String isForceUpdate =  object.getString("IsForceUpdate");
                            String updateTitle = object.getString("UpdateTitle");
                            String updateNotes =  object.getString("UpdateNotes");
                            String versionCode = object.getString("VersionCode");
                            String versionName =  object.getString("VersionName");
                            int latestAppVersion =Integer.parseInt(versionCode);

                            if (latestAppVersion > getCurrentVersionCode()) {
                                updateApp(updateTitle, updateNotes, versionCode, versionName, isForceUpdate);
                            } else {
                                Log.v("app update", "This app is up to date");
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
            Toast.makeText(this, "Oops..!! Did not got any response, please check your network and try again", Toast.LENGTH_SHORT).show();
//            userSharedPrefDetails.setCart_count(0);
        }

    }

    private int getCurrentVersionCode() {
        try {
            Log.i("VersionCode", "getCurrentVersionCode: "+getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void checkUpdate()
    {
        count = 1;
        checkUpdatType= "check_update";
        updateWorker = new BackgroundWorker(HomeActivity.this);
        updateWorker.setAsyncTaskListener(this);
        updateWorker.execute(checkUpdatType);
    }

    private void updateApp(String updateTitleTxt, String updateDesc, String versionCodeTxt, String versionName, String isForceUpdate) {

        inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.update_app, null);
        updateTitle = alertLayout.findViewById(R.id.updateTitle);
        updateNotes = alertLayout.findViewById(R.id.updateNotes);
        versionCode1 = alertLayout.findViewById(R.id.versionCode);
        updateButton = alertLayout.findViewById(R.id.updateButton);
        cancelButton = alertLayout.findViewById(R.id.cancelButton);
        alert = new AlertDialog.Builder(HomeActivity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        dialog = alert.create();

        updateTitle.setText(updateTitleTxt);
        updateNotes.setText(updateDesc);//object.getString("updateNotes")
        versionCode1.setText("New Version: "+versionName);//object.getString("versionCode")
        updateButton.setText("Update Now");//object.getString("isForceUpdate")

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.annotation.drinking_zone")));
            }
        });


        if(isForceUpdate.equalsIgnoreCase("Yes")){
            cancelButton.setText("Cancel");//object.getString("isForceUpdate")
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAffinity();
                }
            });
        }else {
            cancelButton.setText("Remind me Later");
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
        dialog.show();

    }



}




