package com.annotation.drinking_zone.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManagerFCM {

    Context ctx;
    private static com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM instance;
    private static final String SHARED_PREF_NAME =  "DrinkingZone_FCMSharedPrefs";
    private static final String KEY_ACCESS_TOKEN =  "token";


    SharedPreferenceManagerFCM(Context c)
    {
        ctx = c;
    }

    public static synchronized com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM getInstance(Context c)
    {
        if(instance == null)
            instance = new com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM(c);
        return instance;
    }
    public boolean storeToken(String token)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
        return true;
    }

    public String getToken()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, "");
    }
}
