package com.example.matchitup.game;

import com.example.matchitup.DictionaryService;
import com.example.matchitup.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public abstract class Game extends Observable {

    private int record, currentPoints;
    private Map<String, String> word_definition;

    private Map<String, Word> wordMap;

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

    /**
     * Updates the list of words and definitions of the game.
     * @param words a list of word to update
     */
    void updateWords(List<Word> words) {
        wordMap = new HashMap<>();

        for(Word w : words) {
            wordMap.put(w.getWord(), w);
        }
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

        for(Word w : wordMap.values()) {
            defs.add(w.getDef());
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


    
    /*protected abstract List<String> getWords();*/

    /*TODO: Quizas se pueda hacer en un sitio de manera común si todos los juegos guardan la info en
      TODO: en el mismo sitio*/
    protected abstract boolean saveRecord(int record);
}
