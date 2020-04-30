package com.example.matchitup.game;

import com.example.matchitup.DictionaryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public abstract class Game extends Observable {

    private int record, currentPoints;
    private Map<String, String> word_definition;

    public Game(int record) {
        this.record = record;
        this.currentPoints = 0;
        this.word_definition = new HashMap<>();
    }


    protected Map<String, String> generateWords(){
        List<String> words = getWords();
        List<String> definitions = getDefinitions(words);
        word_definition.clear();

        for(int i = 0; i < words.size(); i++){
            word_definition.put(words.get(i), definitions.get(i));
        }
        return word_definition;
    }

    private List<String> getDefinitions(List<String> words){
        ArrayList<String> definitions = new ArrayList<>();
        for (String w: words){
            definitions.add(DictionaryService.getDefinition(w));
        }
        return definitions;
    };


    protected abstract List<String> getWords();



    /*TODO: Quizas se pueda hacer en un sitio de manera común si todos los juegos guardan la info en
      TODO: en el mismo sitio*/
    protected abstract boolean saveRecord(int record);
}
