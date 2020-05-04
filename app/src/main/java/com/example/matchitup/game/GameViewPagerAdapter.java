package com.example.matchitup.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.matchitup.DictionaryActivity;
import com.example.matchitup.ProfileActivity;
import com.example.matchitup.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class GameViewPagerAdapter extends PagerAdapter implements Observer {

    public static final int VIEWS = 2;
    private static final int WORDS_VIEW = 0;
    private static final int DEFINITIONS_VIEW = 1;
    private Context context;
    private LayoutInflater layoutInflater;
    private Map<String, String> word_definition;
    private int limitWords;


    public GameViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return VIEWS;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }


    @Override
    public void update(Observable observable, Object arg) {
        Game game = (Game)observable;
        word_definition = game.getWordMap();
        limitWords = game.getLimitWords();
    }

    private void prepareButtons(List<Button> buttons){
        int i = 0;
        while(i < limitWords){
            buttons.get(i).setVisibility(View.VISIBLE);
            i++;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.game_views, container, false);
        List<Button> buttons = new ArrayList<>();
        buttons.add((Button) view.findViewById(R.id.button0));
        buttons.add((Button) view.findViewById(R.id.button1));
        buttons.add((Button) view.findViewById(R.id.button2));
        buttons.add((Button) view.findViewById(R.id.button3));
        buttons.add((Button) view.findViewById(R.id.button4));

        prepareButtons(buttons);

        // TODO: Esto quizá se pueda refactorizar
        int i = 0;
        if(position == WORDS_VIEW){
            for(String word : word_definition.keySet()) {
                buttons.get(i).setText(word);
                i++;
            }

        } else if (position == DEFINITIONS_VIEW){
            for(String definition : word_definition.values()) {
                buttons.get(i).setText(definition);
                i++;
            }

        }


        /*ImageView slideLogo = (ImageView) view.findViewById(R.id.slide_logo);
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
                    case 0:
                        onPlayPressed();
                        break;
                    // Diccionario y perfil
                    default:
                        context.startActivity(intent);
                }
            }
        });*/
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout) object);
    }
}