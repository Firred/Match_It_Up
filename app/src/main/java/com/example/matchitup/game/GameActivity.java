package com.example.matchitup.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.matchitup.CustomViewPager;
import com.example.matchitup.LocaleManager;
import com.example.matchitup.R;
import com.example.matchitup.Word;
import com.example.matchitup.WordLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class GameActivity extends AppCompatActivity implements Observer {
    private final String STATE_LANGUAGE = "language", STATE_GAME = "game", STATE_NOTIFICATION="not";
    private final int WORD_LOADER_ID = 501;
    private static final int WORDS_VIEW = 0;
    private static final int DEFINITIONS_VIEW = 1;
    private static final int GAME_POINTS = 5;

    private WordLoaderCallbacks wordLoaderCallbacks = new WordLoaderCallbacks();
    private Game game;
    private TextView level, points, pointsString, gameState;
    private RelativeLayout nextBtnLayout;
    private CustomViewPager gameViewPager;
    private GameViewPagerAdapter gameViewPagerAdapter;
    private Dialog popUpNotification;


    /**
     * Method which is called when an activity is created
     * @param savedInstanceState Bundle with past states of variables
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeViews();

        if(savedInstanceState != null) {
            LocaleManager.setLocale(this, savedInstanceState.getString(STATE_LANGUAGE));
            game = (Game)savedInstanceState.getSerializable(STATE_GAME);

            game.addObserver(this);
            game.addObserver(gameViewPagerAdapter);

            game.forceUpdate();

            if(game.isNextRound()) {
                nextBtnLayout.setVisibility(View.VISIBLE);
            }

            gameViewPager.setAdapter(gameViewPagerAdapter);
        }
        else {
            Intent intent = getIntent();
            int gameMode = intent.getIntExtra("start_game", 0);

            GameFactory games = new GameFactory();

            switch(gameMode){
                case R.id.btnEasy: game = games.easyGame(getString(R.string.level_easy),
                        this.getSharedPreferences("matchPref", Context.MODE_PRIVATE)
                                .getInt("easy", 0)); break;
                case R.id.btnMedium: game = games.mediumGame(getString(R.string.level_medium),
                        this.getSharedPreferences("matchPref", Context.MODE_PRIVATE)
                                .getInt("medium", 0)); break;
                case R.id.btnHard: game = games.hardGame(getString(R.string.level_hard),
                        this.getSharedPreferences("matchPref", Context.MODE_PRIVATE)
                                .getInt("hard", 0)); break;
            }

            game.addObserver(this);
            game.addObserver(gameViewPagerAdapter);

            requestNewWords();
        }
    }

    /**
     * Method which is called when the observable notify to make changes
     * @param observable Object representing which observable has been the notifier
     */
    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Game) {
            Game game = (Game)observable;
            if(! game.lackOfInfo()) {
                pointsString.setText(getString(R.string.points));
                points.setText(Integer.toString(game.getCurrentPoints()));
                level.setText(game.getGameModeString());
                gameState.setText("");

                if (game.isRoundFinished()) {
                    gameViewPager.setAdapter(gameViewPagerAdapter);

                    if(!game.isNextRound())
                        nextBtnLayout.setVisibility(View.GONE);
                }
            } else {
                game.updateWords(new ArrayList<Word>());
                showNotification();
            }
        }
    }

    /**
     * Method which is called when the activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* remove observer at this point */
        if(game != null) {
            game.deleteObserver(this);
            game.deleteObserver(gameViewPagerAdapter);

            SharedPreferences.Editor editor =
                    this.getSharedPreferences("matchPref", Context.MODE_PRIVATE).edit();
            editor.putInt(game.getGameModeId(), game.getCurrentPoints());
            editor.apply();
        }

        popUpNotification.dismiss();
    }

    /**
     * Private method responsible for initializing all interface views
     */
    private void initializeViews(){
        gameViewPager = findViewById(R.id.viewPagerGame);
        gameViewPager.setTime(250);
        gameViewPagerAdapter = new GameViewPagerAdapter(this);
        popUpNotification = new Dialog(this);
        level = findViewById(R.id.level);
        points = findViewById(R.id.pointsNumber);
        pointsString = findViewById(R.id.points);
        gameState = findViewById(R.id.gameState);
        nextBtnLayout = findViewById(R.id.nextBtnLayout);
        pointsString.setText("");

        LoaderManager loaderManager = LoaderManager.getInstance(this);
        if(loaderManager.getLoader(WORD_LOADER_ID) != null){
            loaderManager.initLoader(WORD_LOADER_ID, null, wordLoaderCallbacks);
        }
    }

    /**
     * Private method responsible for showing a notification when the information to be displayed
     * on the screen is incomplete
     */
    private void showNotification(){
        popUpNotification.setContentView(R.layout.notification_layout);
        popUpNotification.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameState.setText(getString(R.string.reset));
        popUpNotification.show();

    }

    /**
     * Private method responsible for checking whether there is internet connection available on the
     * device
     * @return Boolean representing the state
     */
    private boolean internetConnectionAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (connMgr != null) {
            activeNetwork = connMgr.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * Public method which is executed when a game round is finished and the user is asking for
     * more words
     */
    public void  onRequestNewWords(View view){
        requestNewWords();
    }

    /**
     * Private method responsible for requesting more words, by checking the internet connection and
     * performing the call to the API through the Loader
     */
    private void requestNewWords(){
        if (internetConnectionAvailable()) {
            Bundle queryBundle = new Bundle();
            queryBundle.putInt(WordLoaderCallbacks.PARAM_QUERY, game.getLimitWords());
            queryBundle.putIntegerArrayList(WordLoaderCallbacks.OPTIONAL_PARAM,
                    new ArrayList<Integer>(Arrays.asList(game.getLowFrecuency(), game.getHighFrecuency())));
            LoaderManager.getInstance(this).restartLoader(WORD_LOADER_ID, queryBundle, wordLoaderCallbacks);
            gameState.setText(getString(R.string.loading));
        } else {
            showNotification();
        }
    }

    /**
     * Receive the interaction from the user when it clicks an option in the game.
     * @param view ToggleButton pressed
     */
    public void onClickToggleButtons(View view){
        ToggleButton pressedOption = (ToggleButton) view;
        int pagePosition = (int) pressedOption.getTag();
        boolean checked = pressedOption.isChecked();
        String infoButton = (String) ((ToggleButton) view).getText();

        if(pagePosition == WORDS_VIEW){
            // Se ha pulsado una palabra previamente
            if(game.isCheckedWord()){
                if(game.getChosenWord().equals(infoButton)){
                    // Se aprieta la misma palabra para cancelarla
                    game.setCheckedWord(checked);
                    game.setChosenWord("");
                    ((ToggleButton) view).setChecked(false);
                } else{
                    // Se aprieta una palabra distinta de la misma pagina
                    ((ToggleButton) view).setChecked(false);
                    changePage(pagePosition);
                }
            } else{
                game.setCheckedWord(checked);
                game.setChosenWord(infoButton);
                changePage(pagePosition);
            }
        } else if(pagePosition == DEFINITIONS_VIEW){
            // Se ha pulsado una definition previamente
            if(game.isCheckedDefinition()){
                if(game.getChosenDefinition().equals(infoButton)) {
                    // Se aprieta la misma definition para cancelarla
                    game.setCheckedDefinition(checked);
                    game.setChosenDefinition("");
                    ((ToggleButton) view).setChecked(false);
                } else {
                    // Se aprieta una definition distinta de la misma pagina
                    ((ToggleButton) view).setChecked(false);
                    changePage(pagePosition);
                }
            } else{
                game.setCheckedDefinition(checked);
                game.setChosenDefinition(infoButton);
                changePage(pagePosition);
            }
        }
        game.setRoundFinished(false);

        performClickResponse((ToggleButton) view);
    }

    /**
     * Perform a certain behaviour depending on the game state.
     * @param pressedButton It is the button that has been pressed by the user
     */
    private void performClickResponse(final ToggleButton pressedButton){
        final LinearLayout backgroundLayout = findViewById(R.id.mainLayout);
        final LinearLayout topLayout = findViewById(R.id.topLayout);
        final LinearLayout bottomLayout = findViewById(R.id.bottomLayout);

        Handler handler;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                game.setChosenWord("");
                game.setChosenDefinition("");
                game.setCheckedDefinition(false);
                game.setCheckedWord(false);
                changeLayout(backgroundLayout, R.drawable.grad_bg_game_normal,
                        topLayout, R.drawable.gradient_menu_game_normal,
                        bottomLayout, R.drawable.gradient_menu_game_normal_inverse, "");
                enableOrDisableLayout(true);

                if(game.isNextRound()){
                    nextBtnLayout.setVisibility(View.VISIBLE);
                    game.setRoundFinished(true);
                }
            }
        };

        if(game.pairSelected()){
            ToggleButton associatedButton = game.getFirstButtonPressed();

            if(associatedButton == null)
                return;

            if(game.correctPair() == 1){
                // Animación de dar la vuelta al boton
                YoYo.with(Techniques.FlipOutY).duration(400).repeat(0).playOn(associatedButton);
                pressedButton.setVisibility(View.INVISIBLE);
                //pressedButton.setOnClickListener(null);
                associatedButton.setVisibility(View.INVISIBLE);

                game.setCurrentPoints(game.getCurrentPoints() + GAME_POINTS);
                game.setCorrectWords(game.getCorrectWords() + 1);

                game.getWordMap().remove(game.getChosenWord());

                if(game.isRecord()){
                    pointsString.setText(getString(R.string.record));
                    YoYo.with(Techniques.DropOut).duration(1300).repeat(0).playOn(findViewById(R.id.points));
                }
                // Realiza la animación de cambiar los colores a correcto
                animateCorrectOrError(backgroundLayout, topLayout, bottomLayout,
                        R.drawable.grad_bg_game_correct, R.drawable.gradient_menu_game_correct, R.drawable.gradient_menu_game_correct_inverse,
                        getString(R.string.success_matchup), Techniques.Landing);
            } else if (game.correctPair() == 0) {
                game.setCurrentPoints(game.getCurrentPoints() - GAME_POINTS);

                // Realiza la animación de cambiar los colores a error
                animateCorrectOrError(backgroundLayout, topLayout, bottomLayout,
                        R.drawable.grad_bg_game_error, R.drawable.gradient_menu_game_error, R.drawable.gradient_menu_game_error_inverse,
                        getString(R.string.error_matchup), Techniques.Shake);
            }

            enableOrDisableLayout(false);

            game.setChosenWord("");
            game.setChosenDefinition("");

            game.setFirstButtonPressed(null);

            handler = new Handler();
            handler.postDelayed(runnable, 2000);
            associatedButton.setChecked(false);
            pressedButton.setChecked(false);

        } else{
            if (game.getFirstButtonPressed() == null)
                game.setFirstButtonPressed(pressedButton);
            else if (game.getFirstButtonPressed() == pressedButton)
                game.setFirstButtonPressed(null);
        }
    }

    /**
     * Private method responsible for enabling or disabling the interface when an animation is being
     * performed
     * @param value Boolean with the state: True for enabling, false for disabling
     */
    private void enableOrDisableLayout(boolean value){
        LinearLayout layout = findViewById(R.id.layoutToggleButtons);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(value);
        }
        gameViewPager.setPagingEnabled(value);
    }

    /**
     * Private method responsible for animating the interface on correct or error states
     * @param bgLayout Game's background layout
     * @param topLayout Game's top layout
     * @param bottomLayout Game's bottom layout
     * @param background Game's background layout id
     * @param top Game's top layout id
     * @param bottom Game's bottom layout id
     * @param state String to be displayed on correct or error state
     * @param stateAnimation Technique representing the animation to be performed
     */
    private void animateCorrectOrError(LinearLayout bgLayout, LinearLayout topLayout, LinearLayout bottomLayout,
                                       int background, int top, int bottom, String state, Techniques stateAnimation){

        changeLayout(bgLayout, background, topLayout, top, bottomLayout, bottom, state);

        YoYo.with(stateAnimation).duration(1000).repeat(0).playOn(findViewById(R.id.gameState));
        YoYo.with(Techniques.DropOut).duration(1300).repeat(0).playOn(findViewById(R.id.pointsNumber));
    }

    /**
     * Private method responsible for changing the GameViewPager's current page
     * @param pagePosition
     */
    private void changePage(int pagePosition){
        if(pagePosition == WORDS_VIEW){
            gameViewPager.setCurrentItem(DEFINITIONS_VIEW);

        } else{
            gameViewPager.setCurrentItem(WORDS_VIEW);
        }
    }

    /**
     * Private method responsible for changing the game's layout
     * @param bgLayout Game's background layout
     * @param topLayout Game's top layout
     * @param bottomLayout Game's bottom layout
     * @param background Game's background layout id
     * @param top Game's top layout id
     * @param bottom Game's bottom layout id
     * @param state String to be displayed on correct or error state
     */
    private void changeLayout(LinearLayout bgLayout, int background, LinearLayout topLayout, int top,
                              LinearLayout bottomLayout, int bottom, String state){
        bgLayout.setBackgroundResource(background);
        topLayout.setBackgroundResource(top);
        bottomLayout.setBackgroundResource(bottom);
        gameState.setText(state);
    }


    private class WordLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Word>>  {
        public static final String PARAM_QUERY = "queryParam";
        public static final String OPTIONAL_PARAM = "optionalParam";


        /**
         * This function will be executed when a Loader has to be created
         * @return Loader<List<Word>> Loader instanciado con una lista de palabras
         */
        @Override
        public Loader<List<Word>> onCreateLoader(int id, @Nullable Bundle args) {
            return new WordLoader(GameActivity.this, args.getInt(PARAM_QUERY),
                    args.getIntegerArrayList(OPTIONAL_PARAM));
        }


        /**
         * This function will be executed when a Loader has finished its task
         * @param loader
         * @param data Data returned by the task executed
         */
        @Override
        public void onLoadFinished(@NonNull Loader<List<Word>> loader, List<Word> data) {

            if (data != null) {
                game.updateWords(data);

                if (!game.getWordMap().isEmpty() && !game.lackOfInfo())
                    game.setCorrectWords(0);
            }
            else {
                game.updateWords(new ArrayList<Word>());
            }

            destroyLoader(loader.getId());
        }

        /**
         * This function is called when the loader has a reset
         * @param loader
         */
        @Override
        public void onLoaderReset(@NonNull Loader<List<Word>> loader) {
            loader.reset();
        }
    }

    /**
     * Used by the LoaderCallback to destroy the current Loader.
     * DO NOT USE IT IN ANY OTHER CASE.
     * @param id the id of the Loader.
     */
    protected void destroyLoader(int id) {
        LoaderManager.getInstance(this).destroyLoader(id);
    }

    /**
     * This function is called when there's any change on the device and the current state must be
     * preserved
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_LANGUAGE,
                this.getSharedPreferences("matchPref", Context.MODE_PRIVATE)
                        .getString("language_key", Locale.getDefault().getLanguage()));
        savedInstanceState.putSerializable(STATE_GAME, game);
        super.onSaveInstanceState(savedInstanceState);
    }
}
