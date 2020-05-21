package com.example.matchitup.game;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private Dialog popUpDef;
    private Map<String, String> word_definition;
    private int wordNumber;
    private View def_dial;

    /**
     * GameViewPagerAdapter Constructor
     * @param context Application context
     */
    public GameViewPagerAdapter(Context context){
        this.context = context;
    }

    /**
     * Get the count of current pages used by the adapter
     * @return Integer representing the number of pages
     */
    @Override
    public int getCount() {
        return VIEWS;
    }

    /**
     * Function used by the adapter which checks whether a view correspond to a specific object
     * @param view
     * @param object
     * @return Boolean representing the state
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    /**
     * Method which is called when the observable notify to make changes
     * @param observable Object representing which observable has been the notifier
     */
    @Override
    public void update(Observable observable, Object arg) {
        Game game = (Game)observable;
        word_definition = game.getWordMap();
        wordNumber = game.getLimitWords();
    }

    /**
     * Private function responsible for getting ready all the buttons used inside the game
     * @param buttons List of Toggle Buttons
     * @param infoToInsert Collection of information to be displayed on the buttons
     * @param position Integer which symbolizes the ViewPager's position
     */
    private void prepareButtons(List<ToggleButton> buttons, Collection<String> infoToInsert, int position){
        int i = 0;
        for(String info : infoToInsert) {
            if(info == null || info.isEmpty()) {
                buttons.get(i).setVisibility(View.INVISIBLE);
                buttons.get(i).setOnClickListener(null);
            } else
                buttons.get(i).setVisibility(View.VISIBLE);

            buttons.get(i).setText(info);
            buttons.get(i).setTextOn(info);
            buttons.get(i).setTextOff(info);
            buttons.get(i).setTag(position);
            i++;
        }
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
        final View view = layoutInflater.inflate(R.layout.game_views, container, false);
        List<ToggleButton> buttons = new ArrayList<>();
        buttons.add((ToggleButton) view.findViewById(R.id.button0));
        buttons.add((ToggleButton) view.findViewById(R.id.button1));
        buttons.add((ToggleButton) view.findViewById(R.id.button2));
        buttons.add((ToggleButton) view.findViewById(R.id.button3));
        buttons.add((ToggleButton) view.findViewById(R.id.button4));

        popUpDef = new Dialog(context);
        def_dial = layoutInflater.inflate(R.layout.definition_dialog, container, false);
        popUpDef.setContentView(def_dial);
        popUpDef.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(position == WORDS_VIEW){
            prepareButtons(buttons, generateAndShuffleList(word_definition.keySet()), position);
        } else if (position == DEFINITIONS_VIEW){
            prepareButtons(buttons, generateAndShuffleList(word_definition.values()), position);

            for(final ToggleButton btn : buttons) {
                btn.setOnLongClickListener(new View.OnLongClickListener() {
                   @Override
                   public boolean onLongClick(View v) {
                       TextView textView = def_dial.findViewById(R.id.defText);
                       textView.setText(btn.getText());
                       popUpDef.show();

                       return true;
                   }
               });
            }
        }

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
        popUpDef.dismiss();
    }

    /**
     * Generates a Collection from another Collection with its values shuffled and adds empty
     * Strings in case the Set doesn't contain enough values.
     * @param data Collection of Strings
     * @return A shuffled Collection from the data which may contain addition values
     */
    private Collection generateAndShuffleList(Collection<String> data) {
        List shuffled = new ArrayList<>(data);

        for (int i = shuffled.size(); i < wordNumber; i++) {
            shuffled.add("");
        }

        Collections.shuffle(shuffled);

        return shuffled;
    }
}