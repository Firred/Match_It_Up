package com.example.matchitup.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.matchitup.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    }

    private void prepareButtons(List<ToggleButton> buttons, Collection<String> infoToInsert, int position){
        int i = 0;
        for(String info : infoToInsert) {
            buttons.get(i).setVisibility(View.VISIBLE);
            buttons.get(i).setText(info);
            buttons.get(i).setTextOn(info);
            buttons.get(i).setTextOff(info);
            buttons.get(i).setTag(position);
            i++;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.game_views, container, false);
        List<ToggleButton> buttons = new ArrayList<>();
        buttons.add((ToggleButton) view.findViewById(R.id.button0));
        buttons.add((ToggleButton) view.findViewById(R.id.button1));
        buttons.add((ToggleButton) view.findViewById(R.id.button2));
        buttons.add((ToggleButton) view.findViewById(R.id.button3));
        buttons.add((ToggleButton) view.findViewById(R.id.button4));


        List shuffled;
        if(position == WORDS_VIEW){
            shuffled = new ArrayList<>(word_definition.keySet());
            Collections.shuffle(shuffled);
            prepareButtons(buttons, shuffled, position);
        } else if (position == DEFINITIONS_VIEW){
            shuffled = new ArrayList<>(word_definition.values());
            Collections.shuffle(shuffled);
            prepareButtons(buttons, shuffled, position);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout) object);
    }
}