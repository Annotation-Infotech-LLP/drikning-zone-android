package com.annotation.drinking_zone.Utility;

import android.content.Context;
import android.content.SharedPreferences;


public class ContactSession {

    Context ctx;
    SharedPreferences sharedPreferences;
    private String user_contact;

    public ContactSession(Context context) {
        this.ctx = context;
        sharedPreferences = context.getSharedPreferences("user_Contact_details", Context.MODE_PRIVATE);
    }


    public String getUser_contact(){
        user_contact= sharedPreferences.getString("user_contact", "");
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
        sharedPreferences.edit().putString("user_contact",user_contact).commit();
    }

    public void removeUser()
    {
        sharedPreferences.edit().clear().commit();
    }

}
