package com.example.matchitup.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.eftimoff.viewpagertransformers.ZoomOutSlideTransformer;
import com.example.matchitup.MainActivity;
import com.example.matchitup.R;
import com.example.matchitup.Word;
import com.example.matchitup.WordLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GameActivity extends AppCompatActivity implements Observer {
    private final int WORD_LOADER_ID = 501;
    private static final int WORDS_VIEW = 0;
    private static final int DEFINITIONS_VIEW = 1;

    private WordLoaderCallbacks wordLoaderCallbacks = new WordLoaderCallbacks();
    private Game game;
    private TextView level, points, pointsString, gameState;
    private Button nextBtn;
    private RelativeLayout nextBtnLayout;
    private ViewPager gameViewPager;
    private GameViewPagerAdapter gameViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        int gameMode = intent.getIntExtra("start_game", 0);

        GameFactory games = new GameFactory();
        // TODO: EL RECORD DEPENDE DE LO QUE TENGA GUARDADO EL USUARIO
        switch(gameMode){
            case R.id.btnEasy: game = games.easyGame(getString(R.string.level_easy) ,5); break;
            case R.id.btnMedium: game = games.mediumGame(getString(R.string.level_medium), 5); break;
            case R.id.btnHard: game = games.hardGame(getString(R.string.level_hard), 5); break;
        }

        initializeViews();

        game.addObserver(this);
        game.addObserver(gameViewPagerAdapter);

        //TODO: BUG-> Cuando se bloquea y desbloquea el movil se vuelven a cargar nuevas palabras

        /* Comenzamos el juego */
        if (internetConnectionAvailable()) {
            Bundle queryBundle = new Bundle();
            queryBundle.putInt(WordLoaderCallbacks.PARAM_QUERY, game.getLimitWords());
            queryBundle.putIntegerArrayList(WordLoaderCallbacks.OPTIONAL_PARAM,
                    new ArrayList<Integer>(Arrays.asList(game.getLowFrecuency(), game.getHighFrecuency())));
            LoaderManager.getInstance(this).restartLoader(WORD_LOADER_ID, queryBundle, wordLoaderCallbacks);
            gameState.setText(getString(R.string.loading));
        } else {
            // TODO: Mensaje de no hay conexion
            // TODO Usar el mismo dialog que se use para cuando no hay palabras por el error 429
        }
    }


    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Game) {
            Game game = (Game)observable;
            pointsString.setText(getString(R.string.points));
            points.setText(Integer.toString(game.getCurrentPoints()));
            level.setText(game.getGameModeString());

            // TODO: ESTO HAY QUE CAMBIARLO CUANDO FUNCIONE
            //nextBtnLayout.setVisibility(View.VISIBLE);

            if(game.isRoundFinished()) {
                gameState.setText("");
                gameViewPager.setAdapter(gameViewPagerAdapter);
            }


            //TODO: Poner roundFinished a true cuando se restauren palabras
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* remove observer at this point */
        if(game != null) {
            game.deleteObserver(this);
            game.deleteObserver(gameViewPagerAdapter);
        }
    }


    private void initializeViews(){
        gameViewPager = findViewById(R.id.viewPagerGame);
        //gameViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        gameViewPagerAdapter = new GameViewPagerAdapter(this);
        level = findViewById(R.id.level);
        points = findViewById(R.id.pointsNumber);
        pointsString = findViewById(R.id.points);
        gameState = findViewById(R.id.gameState);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtnLayout = findViewById(R.id.nextBtnLayout);
        pointsString.setText("");

        LoaderManager loaderManager = LoaderManager.getInstance(this);
        if(loaderManager.getLoader(WORD_LOADER_ID) != null){
            loaderManager.initLoader(WORD_LOADER_ID, null, wordLoaderCallbacks);
        }

    }

    private boolean internetConnectionAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (connMgr != null) {
            activeNetwork = connMgr.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnected();
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
            if(game.isCheckedWord()){
                if(game.getChosenWord().equals(infoButton)){
                    game.setCheckedWord(checked);
                    game.setChosenWord("");
                    ((ToggleButton) view).setChecked(false);
                } else{
                    ((ToggleButton) view).setChecked(false);
                    changePage(pagePosition);
                }
            } else{
                game.setCheckedWord(checked);
                game.setChosenWord(infoButton);
                changePage(pagePosition);
            }
        } else if(pagePosition == DEFINITIONS_VIEW){
            if(game.isCheckedDefinition()){
                if(game.getChosenDefinition().equals(infoButton)) {
                    game.setCheckedDefinition(checked);
                    game.setChosenDefinition("");
                    ((ToggleButton) view).setChecked(false);
                } else {
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

        performClickResponse(pagePosition, (ToggleButton) view);
    }

    /**
     * Perform a certain behaviour depending on the game state.
     * @param pagePosition Integer referred to the actual page position: words or definitions
     */
    private void performClickResponse(int pagePosition, final ToggleButton pressedButton){
        final LinearLayout backgroundLayout = findViewById(R.id.mainLayout);
        final LinearLayout topLayout = findViewById(R.id.topLayout);
        final LinearLayout bottomLayout = findViewById(R.id.bottomLayout);

        // TODO: Debe ser el boton anterior pulsado
        //final ToggleButton associatedButton = findViewById(secondPressedButton.getId());

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

                //associatedButton.setChecked(false);
                pressedButton.setChecked(false);
            }
        };
        if(game.pairSelected()){
            if(game.correctPair()){
                YoYo.with(Techniques.FlipOutY).duration(400).repeat(0).playOn(findViewById(pressedButton.getId()));
                pressedButton.setVisibility(View.INVISIBLE);
                //associatedButton.setVisibility(View.INVISIBLE);

                game.setCurrentPoints(game.getCurrentPoints() + 5);

                animateCorrectOrError(backgroundLayout, topLayout, bottomLayout,
                        R.drawable.grad_bg_game_correct, R.drawable.gradient_menu_game_correct, R.drawable.gradient_menu_game_correct_inverse,
                        getString(R.string.success_matchup), Techniques.Landing);
            } else {
                game.setCurrentPoints(game.getCurrentPoints() - 5);

                animateCorrectOrError(backgroundLayout, topLayout, bottomLayout,
                        R.drawable.grad_bg_game_error, R.drawable.gradient_menu_game_error, R.drawable.gradient_menu_game_error_inverse,
                        getString(R.string.error_matchup), Techniques.Shake);
            }
            //Inicia la animación
            handler = new Handler();
            handler.postDelayed(runnable, 1500);
        }
    }


    private void animateCorrectOrError(LinearLayout bgLayout, LinearLayout topLayout, LinearLayout bottomLayout,
                                       int background, int top, int bottom, String state, Techniques stateAnimation){

        changeLayout(bgLayout, background, topLayout, top, bottomLayout, bottom, state);

        if(game.isRecord()){
            pointsString.setText(getString(R.string.record));
            YoYo.with(Techniques.DropOut).duration(1300).repeat(0).playOn(findViewById(R.id.points));
        }

        YoYo.with(stateAnimation).duration(1000).repeat(0).playOn(findViewById(R.id.gameState));
        YoYo.with(Techniques.DropOut).duration(1300).repeat(0).playOn(findViewById(R.id.pointsNumber));
    }


    private void changePage(int pagePosition){
        if(pagePosition == WORDS_VIEW){
            gameViewPager.setCurrentItem(DEFINITIONS_VIEW);

        } else{
            gameViewPager.setCurrentItem(WORDS_VIEW);
        }
    }

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
         * Si al llamar a initLoader() en onCreate() de MainActivity el ID no existe, se ejecuta esta
         * función. Si existía, borra el loader que había y vuelve aquí.
         */
        @Override
        public Loader<List<Word>> onCreateLoader(int id, @Nullable Bundle args) {
            return new WordLoader(GameActivity.this, args.getInt(PARAM_QUERY),
                    args.getIntegerArrayList(OPTIONAL_PARAM));
        }


        /**
         * Si en initLoader() del onCreate de MainActivity existiera el ID y ya ha generado datos, se
         * llama a esta función ya que significa que habría otro loader creado con el mismo ID.
         *
         * Esta función se ejecuta también cuando un Loader previo se ha terminado. Este suele ser el
         * punto en el que mueve los datos a las View mediante el adapter. La ejecución del
         * loadInBackground() pasa a esta función.
         */
        @Override
        public void onLoadFinished(@NonNull Loader<List<Word>> loader, List<Word> data) {
            // TODO: Comprobar primero que no haya ninguna palabra o definicion a null (Error 429)

            if (data != null) {
                game.updateWords(data);
            }
            else {
                game.updateWords(new ArrayList<Word>());
            }
        }

        /**
         * Cuando un Loader previamente creado se está resetando. En este punto la app debería eliminar
         * cualquier referencia que tenga de los datos del Loader.
         *
         * Esta función se llama solamente cuando el Loader actual se está destruyendo, por lo que se puede
         * dejar en blanco la mayoría del tiempo. No intentaremos acceder a datos después de hayan sido
         * borrados
         */
        @Override
        public void onLoaderReset(@NonNull Loader<List<Word>> loader) {
            loader.reset();
            //TODO: Se podría actulizar la interfaz (eliminar las palabras y descripciones). OPCIONAL
        }
    }
}
