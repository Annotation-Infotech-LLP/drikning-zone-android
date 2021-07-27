package com.annotation.drinking_zone.SplashScreen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;
import com.annotation.drinking_zone.WelcomeSlides.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    Animation fromLeft, fromRight, fromTop;
    String deviceToken = "";
    UserSharedPrefDetails userSharedPrefDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.logo);

        fromRight=AnimationUtils.loadAnimation(this, R.anim.from_right);
        logo.setAnimation(fromRight);

        userSharedPrefDetails = new UserSharedPrefDetails(SplashActivity.this);
        Log.i("ViewedWelc", "Oncreate" + userSharedPrefDetails.getWelcomeScreenViewed());
//        userSharedPrefDetails.removeUser();

        if(Build.VERSION.SDK_INT>=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initialize();
    }

    public void initialize()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if user registered , then will be redirected to home screen, else to login screen
                if(userSharedPrefDetails.getUser_Id() != 0)
                {
//                    it is for the user who is not registered
                    Log.i("Cond1", "run: Entered");
                    Log.i("UserId", "run: Entered" + userSharedPrefDetails.getUser_Id());
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Animatoo.animateFade(SplashActivity.this);
                    finish();
                }
                else if(userSharedPrefDetails.getWelcomeScreenViewed().equals("No"))
                {
                    Log.i("Cond2", "run: Entered");
                    Log.i("ViewedWelc", "run: Entered" + userSharedPrefDetails.getWelcomeScreenViewed());
                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    Animatoo.animateFade(SplashActivity.this);
                    finish();
                }
                else
                {
//                    it is for the user who is registered
                    Log.i("Cond3", "run: Entered");
                    Log.i("ViewedWelc3", "run: Entered" + userSharedPrefDetails.getWelcomeScreenViewed());
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Animatoo.animateFade(SplashActivity.this);
                    finish();
                }
                finish();
            }
        }, 2500);
    }
}

