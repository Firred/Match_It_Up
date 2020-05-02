package com.example.matchitup.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.widget.TextView;

import com.example.matchitup.Constants;
import com.example.matchitup.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.matchitup.Word;
import com.example.matchitup.WordLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity {
    private final int WORD_LOADER_ID = 501;


    private Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        GameFactory games = new GameFactory();

        //Recogemos el tipo de juego
        Intent intent = getIntent();
        String gameMode = intent.getStringExtra(Constants.START_GAME);

        /**
         * EL RECORD DE ESTAS LLAMADAS DEPENDE DE LO QUE TENGA GUARDADO EL USUARIO
         */
        switch(gameMode){
            case "Facil": game = games.easyGame(5); break;
            case "Medio": game = games.mediumGame(5); break;
            case "Dificil": game = games.hardGame(5); break;
        }


        /**
         * AQUI ES DONDE SE EMPIEZA A UTILIZAR LAS LLAMADAS QUE NECESITAN EL BACKGROUND
         */
        /*Map<String, String> words = game.generateWords();

        for (Map.Entry<String, String> entry : words.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        //Realiza una animación en una vista
        //YoYo.with(Techniques.ZoomIn).duration(700).repeat(5).playOn(findViewById(R.id.));*/
    }

    /**
     * La idea es pasar las palabras al activity con esta función para luego
     * pasarlas al Game (cómo? Ni zorra).
     */
    private void getRandomWords(List<Word> words) {
        game.updateWords(words);
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

            //TODO: Aquí habría que actualizar la interfaz que no tenemos
            if (data!= null) {
                getRandomWords(data);
            }
            else {
                getRandomWords(new ArrayList<Word>());
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
