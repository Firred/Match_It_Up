package com.example.matchitup;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class WordLoader extends AsyncTaskLoader<List<Word>> {
    public static final int WORD_LOADER_ID = 501;
    private Object paramQuery;
    private List<Integer> optionalParams;


    /**
     * First constructor for WordLoader object. Mostly used for dictionary's functionality
     * @param context Application context
     * @param word Word that need to be searched.
     */
    public WordLoader(Context context, String word) {
        super(context);
        this.paramQuery = word;
        this.optionalParams = null;
    }


    /**
     * Second constructor for WordLoader object. Mostly used for game's functionality
     * @param context Application context
     * @param randomLimit Number of random words which need to be generated for the game
     * @param optionalParams List of integers which represent the range of use frecuency of the words
     *                       to be generated
     */
    public WordLoader(Context context, Integer randomLimit, List<Integer> optionalParams) {
        super(context);
        this.paramQuery = randomLimit;
        this.optionalParams = optionalParams;
    }

    /**
     * This function will normally be called for you automatically when the associated
     * fragment/activity is being started.
     */
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This function will perform the necessary requests to retrieve the information associated to
     * a certain word
     * @param wordQuery Word to be searched
     * @return List of words
     */
    private List<Word> loadWord(String wordQuery) {
        List<Word> data = new ArrayList<>();
        String def = DictionaryService.getDefinition(wordQuery);
        String audio = DictionaryService.getAudio(wordQuery);
        List<String> examples = DictionaryService.getExamples(wordQuery, 2);

        Word word = new Word(wordQuery, def, audio, examples);

        data.add(word);

        return data;
    }

    /**
     * This function will perform the necessary requests to retrieve the information associated to
     * load RandomLimit words in a list
     * @param randomLimit Number of words to be searched
     * @return List of words
     */
    private List<Word> loadRandom(int randomLimit) {
        List<String> words = null;
        List<Word> data = new ArrayList<>();

        if (optionalParams != null && optionalParams.size() >= 2) {
            words = DictionaryService.getRandomWords(randomLimit, optionalParams.get(0), optionalParams.get(1));
        }
        if(words != null) {
            for (String w : words) {
                String def = DictionaryService.getDefinition(w);

                data.add(new Word(w, def));
            }
        } else {
            data.add(new Word(null, null));
        }

        return data;
    }

    /**
     *  Called on a worker thread to perform the actual load and to return the result of the load operation.
     * @return List of words
     */
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
