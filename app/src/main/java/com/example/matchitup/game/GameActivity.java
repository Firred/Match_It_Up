package com.example.matchitup.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.widget.TextView;

import com.example.matchitup.Constants;
import com.example.matchitup.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

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
}
