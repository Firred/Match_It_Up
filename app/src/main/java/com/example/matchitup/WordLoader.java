package com.example.matchitup;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class WordLoader extends AsyncTaskLoader<List<Word>> {

    private Object paramQuery;
    private List<Integer> optionalParams;


    /**
     * String word: Se refiere a la palabra que quieras buscar para el menu del diccionario
     * */
    public WordLoader(Context context, String word) {
        super(context);
        this.paramQuery = word;
        this.optionalParams = null;
    }

    /**
    * Integer randomLimit: Se refiere al número de palabras aleatorias que quieras buscar para el juego
     * OptionalParams: Hace referencia a la frecuencia de uso de las palabras que se quieran buscar (Relacionado con el modo de juego)
    * */
    public WordLoader(Context context, Integer randomLimit, List<Integer> optionalParams) {
        super(context);
        this.paramQuery = randomLimit;
        this.optionalParams = optionalParams;
    }

    protected void onStartLoading() {
        forceLoad();
    }


    private List<Word> loadWord(String wordQuery) {
        List<Word> data = new ArrayList<>();
        String def = DictionaryService.getDefinition(wordQuery);
        String audio = DictionaryService.getAudio(wordQuery);
        List<String> examples = DictionaryService.getExamples(wordQuery, 2);

        Word word = new Word(wordQuery, def, audio, examples);

        data.add(word);

        return data;
    }

    private List<Word> loadRandom(int randomLimit) {
        List<String> words = null;
        List<Word> data = new ArrayList<>();

        if (optionalParams != null && optionalParams.size() >= 2) {
            words = DictionaryService.getRandomWords(randomLimit, optionalParams.get(0), optionalParams.get(1));
        }

        for(String w : words) {
            String def = DictionaryService.getDefinition(w);

            data.add(new Word(w, def));
        }

        return data;
    }

    @Override
    @Nullable
    public List<Word> loadInBackground() {
        List<Word> data = null;

        try {
            if (paramQuery instanceof String) {
                data = loadWord((String) paramQuery);
            } else if (paramQuery instanceof Integer) {
                data = loadRandom((int) paramQuery);
            } else {
                Log.d("WordLoader; back", "param type error");
            }
        } catch (RuntimeException e){

        }
        return data;
    }
}
