package com.example.matchitup;

import com.example.matchitup.game.GameActivity;

public class Constants{

    public static final String START_GAME = "com.example.matchitup.Constants.START_GAME";

    public static final int[] SLIDE_IMAGES = {
            R.drawable.jugar,
            R.drawable.dict,
            R.drawable.perfil
    };

    public static final Class[] SLIDE_CLASSES = {
            GameActivity.class,
            DictionaryActivity.class,
            ProfileActivity.class
    };

    public static final String[] SLIDE_TITLES = {
            "JUGAR",
            "DICCIONARIO",
            "PERFIL"
    };

    public static final String[] SLIDE_DESCRIPTIONS = {
            "Comienza a jugar e intenta emparejar el mayor número de palabras posibles con su significado."
                    + " Cuantas más emparejes, mayor puntuación obtendrás.",
            "Consulta las palabras con las que hayas dudado al jugar y apréndelas de una manera más " +
                    "tradicional.",
            "Encuentra las mejores puntuaciones que has conseguido en todos los niveles jugados" +
                    " y cambia el idioma de la aplicación por otro distinto."
    };
}
