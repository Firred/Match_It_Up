package com.example.matchitup;

import android.os.AsyncTask;

public class TestWorker extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            /**
             * Colocar la función que se quiera probar.
             * Se pueden poner múltiples funciones consecutivas.
             * Las funciones tienen que tener un Log que muestre por consola el resultado.
             */
            /*DictionaryService.getRandomWords(5);
            DictionaryService.getFrequency("house");
            DictionaryService.getAudio("house");
            DictionaryService.getExamples("house", 5);
            DictionaryService.getDefinition("house");*/

        } catch (Exception e) {
            Exception exception = e;

            return null;
        }

        return null;
    }
}
