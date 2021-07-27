package com.annotation.drinking_zone.WelcomeSlides;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.annotation.drinking_zone.Login.SignInActivity;
import com.annotation.drinking_zone.R;


public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    private int[] layouts = {R.layout.first_slide, R.layout.second_slide, R.layout.third_slide,
            R.layout.fourth_slide, R.layout.fifth_slide, R.layout.sixth_slide};
    private SliderAdapter sliderAdapter;
    private LinearLayout dots_layout;
    private ImageView[] dots;
    private Button next_btn, skip_btn;
    UserSharedPrefDetails userSharedPrefDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        userSharedPrefDetails = new UserSharedPrefDetails(WelcomeActivity.this);
        userSharedPrefDetails.setWelcomeScreenViewed("Yes");

        if(new PreferenceManager(this).checkPreference())
        {
            loadHome();
        }

        if(Build.VERSION.SDK_INT>=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        next_btn = findViewById(R.id.next_btn);
        skip_btn = findViewById(R.id.skip_btn);
        viewPager = findViewById(R.id.viewPager);
        sliderAdapter = new SliderAdapter(layouts, this);
        viewPager.setAdapter(sliderAdapter);

        next_btn.setOnClickListener(this);
        skip_btn.setOnClickListener(this);
        dots_layout = findViewById(R.id.dotsLayout);
        createDots(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                createDots(i);
                if(i == layouts.length-1)
                {
                    next_btn.setText("START");
                    skip_btn.setVisibility(View.INVISIBLE);
                }
                else
                {
                    next_btn.setText("NEXT");
                    skip_btn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Animatoo.animateSlideRight(WelcomeActivity.this); //fire the slide left animation
    }

    private void createDots(int current_position)
    {
        if(dots_layout != null)
            dots_layout.removeAllViews();

        dots = new ImageView[layouts.length];

        for(int i = 0; i <layouts.length; i++)
        {
            dots[i] = new ImageView(this);
            if(i == current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0 , 4, 0);
            dots_layout.addView(dots[i], params);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.next_btn:
                loadNextSlide();
                break;

            case R.id.skip_btn:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;
        }

    }

    private void loadHome()
    {
        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
        startActivity(intent);
        Animatoo.animateFade(WelcomeActivity.this);
        finish();
    }

    private void loadNextSlide()
    {
        int next_slide = viewPager.getCurrentItem()+1;

        if(next_slide<layouts.length)
        {
            viewPager.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }
}
