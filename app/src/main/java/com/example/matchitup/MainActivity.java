package com.example.matchitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
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
import com.example.matchitup.game.Game;
import com.example.matchitup.game.GameActivity;
import com.example.matchitup.game.GameFactory;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private ImageView logo;
    private SliderAdapter sliderAdapter;
    private RelativeLayout slidesLayout, mainLayout;
    private SharedPreferences infoUsuario;
    private Dialog popUpPlayMenu;
    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setAnimation();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popUpPlayMenu = new Dialog(this);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        slidesLayout = (RelativeLayout) findViewById(R.id.slidesLayout);
        logo = (ImageView) findViewById(R.id.logo);
        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        mSlideViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        //Inicia la animación
        handler = new Handler();
        handler.postDelayed(runnable, 2500);

        //Pruebas, borrar
        new TestWorker().execute();
    }

    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));
    }

    private void setAnimation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(mainLayout);
        }
        logo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, convertDpToPx(100)));
        slidesLayout.setVisibility(View.VISIBLE);
        logo.setBackgroundResource(R.drawable.gradient_menu);
        logo.setImageResource(R.drawable.logo_lado);
    }

    public void onPlayPressed(){
        popUpPlayMenu.setContentView(R.layout.play_mode_layout);
        popUpPlayMenu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSlideViewPager.getCurrentItem();
        popUpPlayMenu.show();
    }

    public void onPlayModePressed(View v){
        Button btnPressed = (Button) v;
        String buttonText = btnPressed.getText().toString();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(Constants.START_GAME, buttonText);
        startActivity(intent);
    }

    /*Clase que permite crear el menu con distintas pantallas*/
    private class SliderAdapter extends PagerAdapter {
        private Context context;
        private LayoutInflater layoutInflater;


        public SliderAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return Constants.SLIDE_TITLES.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == (LinearLayout)object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position){
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slider_card, container, false);

            ImageView slideLogo = (ImageView) view.findViewById(R.id.slide_logo);
            TextView slideTitle = (TextView) view.findViewById(R.id.slide_title);
            TextView slideDescription = (TextView) view.findViewById(R.id.slide_description);

            slideLogo.setImageResource(Constants.SLIDE_IMAGES[position]);
            slideTitle.setText(Constants.SLIDE_TITLES[position]);
            slideDescription.setText(Constants.SLIDE_DESCRIPTIONS[position]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, (Class<?>) Constants.SLIDE_CLASSES[position]);
                    switch(position){
                        // Jugar
                        case 0:
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

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            container.removeView((LinearLayout) object);
        }

    }


}
