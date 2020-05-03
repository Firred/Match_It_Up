package com.example.matchitup.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    private WordLoaderCallbacks wordLoaderCallbacks = new WordLoaderCallbacks();
    private Game game;
    private TextView level, points, pointsString, gameState;
    private Button nextBtn;
    private RelativeLayout nextBtnLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        GameFactory games = new GameFactory();

        /* Cargamos los elementos de la vista que deben modificarse luego*/
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

        //Recogemos el tipo de juego
        Intent intent = getIntent();
        int gameMode = intent.getIntExtra("start_game", 0);

        // TODO: EL RECORD DEPENDE DE LO QUE TENGA GUARDADO EL USUARIO
        switch(gameMode){
            case R.id.btnEasy: game = games.easyGame(getString(R.string.level_easy) ,5); break;
            case R.id.btnMedium: game = games.mediumGame(getString(R.string.level_medium), 5); break;
            case R.id.btnHard: game = games.hardGame(getString(R.string.level_hard), 5); break;
        }
        // Añadimos la clase como observer del modelo, para que cuando cambie efectue los cambios
        game.addObserver(this);

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
        }


        //Realiza una animación en una vista
        //YoYo.with(Techniques.ZoomIn).duration(700).repeat(5).playOn(findViewById(R.id.));*/
    }

    private boolean internetConnectionAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (connMgr != null) {
            activeNetwork = connMgr.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

    //TODO: Funcion de patron observer
    @Override
    public void update(Observable observable, Object arg) {
        if (observable !=null && observable instanceof Game) {
            Game game = (Game)observable;
            pointsString.setText(getString(R.string.points));
            points.setText(Integer.toString(game.getCurrentPoints()));
            level.setText(game.getGameModeString());
            gameState.setText("");
            nextBtnLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* remove observer at this point */
        if(game != null)
            game.deleteObserver(this);
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
