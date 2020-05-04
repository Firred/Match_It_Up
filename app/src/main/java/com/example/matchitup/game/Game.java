package com.example.matchitup.game;

import com.example.matchitup.DictionaryService;
import com.example.matchitup.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public abstract class Game extends Observable {

    private int record, currentPoints, lowFrecuency, highFrecuency, limitWords;
    private Map<String, String> wordMap;
    private String gameModeString;

    public Game(String gameModeString, int record, int limitWords, int lowFrecuency, int highFrecuency) {
        this.gameModeString = gameModeString;
        this.record = record;
        this.limitWords = limitWords;
        this.lowFrecuency = lowFrecuency;
        this.highFrecuency = highFrecuency;
        this.currentPoints = 0;
    }

    public int getLowFrecuency() {
        return lowFrecuency;
    }

    public int getHighFrecuency() {
        return highFrecuency;
    }

    public int getLimitWords() {
        return limitWords;
    }

    public int getCurrentPoints() { return currentPoints; }

    public String getGameModeString() { return gameModeString; }

    public Map<String, String> getWordMap() {
        return wordMap;
    }


    /*private List<String> getDefinitions(List<String> words){
        ArrayList<String> definitions = new ArrayList<>();
        for (String w: words){
            definitions.add(DictionaryService.getDefinition(w));
        }
        return definitions;
    };*/


    /**
     * Updates the list of words and definitions of the game.
     * @param words a list of word to update
     */
    void updateWords(List<Word> words) {
        wordMap = new HashMap<>();
        for(Word w : words) {
            wordMap.put(w.getWord(), w.getDef());
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Obtains the definitions of the words stored in the list
     * @return a list of definitions
     */
    //TODO: Esto se podría hacer en updateWords almacenando las definiciones en la lista
    // según se va creando el map (evitando así recorrer la lista de nuevo), pero implciaría tener
    // una instancia de la lista almacenada tod0 el rato y no sé si es worth it.
    // Esta función sobreescribe a la otra de getDefinions, no la borro por si al final esto
    // es una mierda y hay que borrarlo.
    private List<String> getDefinitions() {
        List<String> defs = new ArrayList<>();

        for(String definition : wordMap.values()) {
            defs.add(definition);
        }

        return defs;
    }

    /**
     * Obtains the list of words of the game.
     * @return a list of words
     */
    //TODO: Al igual que getDefinitions, esto se podría hacer en el updateWords
    // a costa de tener otra lista en la clase.
    // Con esta clase no haría falta el getWords abstracto.
    // También se podría devolver un Set<String> y así no hace falta crear el list,
    // pero solo se podría usar para recorrer el set, ya que no tiene un get(index).
    private List<String> getWords() {

        return new ArrayList<>(wordMap.keySet());
    }




    /*TODO: Quizas se pueda hacer en un sitio de manera común si todos los juegos guardan la info en
      TODO: en el mismo sitio*/
    protected abstract boolean saveRecord(int record);
}
