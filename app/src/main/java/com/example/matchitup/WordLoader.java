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

    public WordLoader(Context context, String paramQuery) {
        super(context);
        this.paramQuery = paramQuery;
        this.optionalParams = null;
    }

    public WordLoader(Context context, Integer paramQuery, List<Integer> optionalParams) {
        super(context);
        this.paramQuery = paramQuery;
        this.optionalParams = optionalParams;
    }

    protected void onStartLoading() {
        forceLoad();
    }

    private List<Word> loadWord(String queryString) {
        List<Word> data = new ArrayList<>();
        String def = DictionaryService.getDefinition(queryString);
        String audio = DictionaryService.getAudio(queryString);
        List<String> examples = DictionaryService.getExamples(queryString, 2);

        Word word = new Word(queryString, def, audio, examples);

        data.add(word);

        return data;
    }

    private List<Word> loadRandom(int queryString) {
        List<String> words;
        List<Word> data = new ArrayList<>();

        if (optionalParams != null && optionalParams.size() > 2) {
            words = DictionaryService.getRandomWords (queryString, optionalParams.get(0), optionalParams.get(1));
        }
        else {
            Log.d("WordLoader; loadRandom", "optionalParam is null or empty");

            /**
             * Esto es para que no pete toda la app si ocurre.
             * Si entra aquí es porque hay algo mal en el código asi que hay que arreglarlo.
             * Cuando se compruebe que funciona habría que borrarlo.
             */
            words = DictionaryService.getRandomWords (queryString, 0, 10000);
        }

        for(String w : words) {
            String def = DictionaryService.getDefinition(w);
            String audio = DictionaryService.getAudio(w);

            data.add(new Word(w, def, audio));
        }

        return data;
    }

    @Override
    @Nullable
    public List<Word> loadInBackground() {
        List<Word> data = null;

        if(paramQuery instanceof String) {
            data = loadWord((String)paramQuery);
        }
        else if (paramQuery instanceof Integer){
            data = loadRandom((int)paramQuery);
        }
        else {
            Log.d("WordLoader; back", "param type error");
        }

        return data;
    }
}
