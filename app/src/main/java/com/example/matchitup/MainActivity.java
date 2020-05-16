package com.example.matchitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.eftimoff.viewpagertransformers.*;
import com.example.matchitup.dictionary.DictionaryActivity;
import com.example.matchitup.game.GameActivity;
import com.example.matchitup.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public static final String START_GAME = "start_game";
    private CustomViewPager mSlideViewPager;
    private ImageView logo;
    private LinearLayout slidesLayout;
    private RelativeLayout mainLayout;
    private Dialog popUpPlayMenu;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setAnimation();
        }
    };

    /**
     * Method which is called when an activity is created
     * @param savedInstanceState Bundle with past states of variables
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocaleManager.setLocale(this,
                this.getSharedPreferences("matchPref", Context.MODE_PRIVATE)
                        .getString("language_key", Locale.getDefault().getLanguage()));
        
        setContentView(R.layout.activity_main);
        popUpPlayMenu = new Dialog(this);
        mainLayout = findViewById(R.id.mainLayout);
        slidesLayout = findViewById(R.id.slidesLayout);
        logo = findViewById(R.id.logo);
        BottomNavigationView navigationMenu = findViewById(R.id.menuNavigation);
        navigationMenu.setSelectedItemId(R.id.play);
        mSlideViewPager = findViewById(R.id.slideViewPager);
        SliderAdapter sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        mSlideViewPager.setCurrentItem(1);
        mSlideViewPager.setPagingEnabled(false);
        mSlideViewPager.setTime(250);
        mSlideViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());

        navigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.play:
                        mSlideViewPager.setCurrentItem(1);
                        break;
                    case R.id.dictionary:
                        mSlideViewPager.setCurrentItem(0);
                        break;
                    case R.id.profile:
                        mSlideViewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });

        //Inicia la animación
        Handler handler = new Handler();
        handler.postDelayed(runnable, 2500);

        //Realiza una animación del logo
        YoYo.with(Techniques.Landing).duration(1700).repeat(0).playOn(findViewById(R.id.logo));
    }

    /**
     * Method which is called when the activity is destroyed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popUpPlayMenu != null) {
            popUpPlayMenu.dismiss();
            popUpPlayMenu = null;
        }
    }

    /**
     * Private method responsible for converting an integer which represents dp into pixels
     * @param dp
     * @return Pixels
     */
    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Private method which modify the interface to perform an animation on it.
     */
    private void setAnimation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(mainLayout);
        }
        logo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, convertDpToPx(80)));
        slidesLayout.setVisibility(View.VISIBLE);
        logo.setBackgroundResource(R.drawable.gradient_menu);
        logo.setImageResource(R.drawable.logo_lado);
    }

    /**
     * Method called when the user press the option "Play"
     */
    public void onPlayPressed(){
        popUpPlayMenu.setContentView(R.layout.play_mode_layout);
        popUpPlayMenu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSlideViewPager.getCurrentItem();
        popUpPlayMenu.show();
    }

    /**
     * Method called when the user press a level to play
     * @param v
     */
    public void onPlayModePressed(View v){
        Button btnPressed = (Button) v;
        int buttonText = btnPressed.getId();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(START_GAME, buttonText);
        startActivity(intent);
    }

    private class SliderAdapter extends PagerAdapter {

        final int[] SLIDE_IMAGES = {
                R.drawable.dictionary,
                R.drawable.play,
                R.drawable.profile
        };

        final Class[] SLIDE_CLASSES = {
                DictionaryActivity.class,
                GameActivity.class,
                ProfileActivity.class
        };

        final String[] SLIDE_TITLES = {
                getString(R.string.dictionary_menu),
                getString(R.string.play_menu),
                getString(R.string.profile_menu)
        };

        final String[] SLIDE_DESCRIPTIONS = {
                getString(R.string.description_dictionary_menu),
                getString(R.string.description_play_menu),
                getString(R.string.description_profile_menu)
        };

        private Context context;

        /**
         * Constructor for the Slider Adapter
         * @param context
         */
        public SliderAdapter(Context context){
            this.context = context;
        }

        /**
         * Get the count of current pages used by the adapter
         * @return Integer representing the number of pages
         */
        @Override
        public int getCount() {
            return SLIDE_TITLES.length;
        }

        /**
         * Function used by the adapter which checks whether a view correspond to a specific object
         * @param view
         * @param object
         * @return Boolean representing the state
         */
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == (LinearLayout)object;
        }

        /**
         * This is the most important function in this class. It's responsible for instantiate each page used
         * inside the game, with the appropriate information
         * @param container ViewGroup where the views are instantiated
         * @param position Page to be instantiated
         * @return Instantiated view
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slider_card, container, false);

            ImageView slideLogo = (ImageView) view.findViewById(R.id.slide_logo);
            TextView slideTitle = (TextView) view.findViewById(R.id.slide_title);
            TextView slideDescription = (TextView) view.findViewById(R.id.slide_description);

            slideLogo.setImageResource(SLIDE_IMAGES[position]);
            slideTitle.setText(SLIDE_TITLES[position]);
            slideDescription.setText(SLIDE_DESCRIPTIONS[position]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, (Class<?>) SLIDE_CLASSES[position]);
                    switch(position){
                        // Jugar
                        case 1:
                            onPlayPressed();
                            break;
                        // Diccionario y perfil
                        default:
                            context.startActivity(intent);
                    }
                }
            });
            container.addView(view);
            return view;
        }

        /**
         * Function called when the ViewPagerAdapter is removed
         * @param container ViewGroup where the views are deleted
         * @param position Page to be deleted
         * @param object Object to be deleted
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            container.removeView((LinearLayout) object);
        }
    }
}
