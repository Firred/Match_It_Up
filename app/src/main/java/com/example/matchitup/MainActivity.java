package com.example.matchitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.LayoutTransition;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.*;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private ImageView logo;
    private SliderAdapter sliderAdapter;
    private RelativeLayout slidesLayout, mainLayout;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                TransitionManager.beginDelayedTransition(mainLayout);
            }
            logo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, convertDpToPx(100)));
            slidesLayout.setVisibility(View.VISIBLE);
            logo.setBackgroundResource(R.drawable.gradient_menu);
            logo.setImageResource(R.drawable.logo_lado);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        LayoutTransition lt = new LayoutTransition();
        lt.disableTransitionType(LayoutTransition.DISAPPEARING);
        mainLayout.setLayoutTransition(lt);

        slidesLayout = (RelativeLayout) findViewById(R.id.slidesLayout);
        logo = (ImageView) findViewById(R.id.logo);
        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        handler.postDelayed(runnable, 2600);
    }

    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));
    }
}
