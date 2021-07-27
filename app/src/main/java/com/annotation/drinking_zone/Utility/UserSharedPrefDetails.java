package com.annotation.drinking_zone.Utility;

import android.content.Context;
import android.content.SharedPreferences;


public class UserSharedPrefDetails {

    Context ctx;
    SharedPreferences sharedPreferences;
    private String user_email;
    private String user_password;
    private String user_name;
    private String user_firstname, user_lastname;
    private String user_contact;
    private int user_Id, cart_count, notification_count;
    private String orderId;
    private String user_referral_code;
    private String welcomeScreenViewed;


    public UserSharedPrefDetails(Context context) {
        this.ctx = context;
        sharedPreferences = context.getSharedPreferences("user_login_details", Context.MODE_PRIVATE);
    }


    public String getUser_contact(){
        user_contact= sharedPreferences.getString("user_contact", "");
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
        sharedPreferences.edit().putString("user_contact",user_contact).commit();
    }

    public String getWelcomeScreenViewed() {
        welcomeScreenViewed = sharedPreferences.getString("welcomeScreen_viewed", "No");
        return welcomeScreenViewed;
    }

    public void setWelcomeScreenViewed(String welcomeScreenViewed) {
        this.welcomeScreenViewed = welcomeScreenViewed;
        sharedPreferences.edit().putString("welcomeScreen_viewed",welcomeScreenViewed).commit();
    }

    public String getUser_referral_code() {
        user_referral_code= sharedPreferences.getString("user_referral_code", "");
        return user_referral_code;
    }

    public void setUser_referral_code(String user_referral_code) {
        this.user_referral_code = user_referral_code;
        sharedPreferences.edit().putString("user_referral_code",user_referral_code).commit();
    }

    public int getUser_Id() {
        user_Id = sharedPreferences.getInt("user_Id",0);
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
        sharedPreferences.edit().putInt("user_Id", user_Id).commit();
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
        sharedPreferences.edit().putString("user_name",user_name).commit();
    }

    public String getUser_name() {
        user_name= sharedPreferences.getString("user_name", "");
        return user_name;
    }

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
        sharedPreferences.edit().putString("user_firstname",user_firstname).commit();
    }

    public String getUser_firstname() {
        user_firstname= sharedPreferences.getString("user_firstname", "");
        return user_firstname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
        sharedPreferences.edit().putString("user_lastname",user_lastname).commit();
    }

    public String getUser_lastname() {
        user_lastname= sharedPreferences.getString("user_lastname", "");
        return user_lastname;
    }


    public String getUser_email() {
        user_email= sharedPreferences.getString("user_email", "");
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
        sharedPreferences.edit().putString("user_email",user_email).commit();
    }


    public String getOrderId() {
        orderId= sharedPreferences.getString("order_id", "");
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId= orderId;
        sharedPreferences.edit().putString("order_id",orderId).commit();
    }

    public String getUser_password() {
        sharedPreferences.getString("user_password","");
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
        sharedPreferences.edit().putString("user_password",user_password);
    }

    public int getCart_count() {
        cart_count = sharedPreferences.getInt("cart_count",-1);
        return cart_count;
    }

    public void setCart_count(int cart_count) {
        this.cart_count = cart_count;
        sharedPreferences.edit().putInt("cart_count", cart_count).commit();
    }

//    public int getNotification_count() {
//        notification_count = sharedPreferences.getInt("notification_count",-1);
//        return notification_count;
//    }
//
//    public void setNotification_count(int notification_count) {
//        this.notification_count = notification_count;
//        sharedPreferences.edit().putInt("notification_count", notification_count).commit();
//    }

    public void removeUser()
    {
        sharedPreferences.edit().clear().commit();
    }

}
