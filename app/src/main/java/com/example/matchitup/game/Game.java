package com.example.matchitup.game;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ToggleButton;

import com.example.matchitup.DictionaryService;
import com.example.matchitup.Word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public abstract class Game extends Observable implements Serializable {

    private int record, currentPoints, lowFrecuency, highFrecuency, limitWords, correctWords;
    private Map<String, String> wordMap;
    private String gameModeString, chosenWord, chosenDefinition, gameModeId;
    private boolean checkedWord, checkedDefinition, roundFinished;
    private ToggleButton firstButtonPressed;

    public Game(String gameModeString, int record, int limitWords, int lowFrecuency,
                int highFrecuency, String gameModeId) {
        this.gameModeString = gameModeString;
        this.record = record;
        this.limitWords = limitWords;
        this.lowFrecuency = lowFrecuency;
        this.highFrecuency = highFrecuency;
        this.checkedWord = false;
        this.roundFinished = true;
        this.checkedDefinition = false;
        this.currentPoints = 0;
        this.correctWords = 0;
        this.chosenWord = "";
        this.chosenDefinition = "";
        this.gameModeId = gameModeId;
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

    public String getChosenWord() { return chosenWord; }

    public String getChosenDefinition() { return chosenDefinition; }

    public String getGameModeId() { return  gameModeId; }

    public boolean isCheckedWord() { return checkedWord; }

    public boolean isCheckedDefinition() { return checkedDefinition; }

    public Map<String, String> getWordMap() { return wordMap; }


    public void setFirstButtonPressed(ToggleButton firstButtonPressed) {
        this.firstButtonPressed = firstButtonPressed;
    }

    public ToggleButton getFirstButtonPressed() {
        return firstButtonPressed;
    }

    public void setCurrentPoints(int currentPoints) {
        this.currentPoints = currentPoints;
        if(this.currentPoints <= 0){
            this.currentPoints = 0;
        }
        setChanged();
        notifyObservers();
    }

    public void setChosenWord(String chosenWord) { this.chosenWord = chosenWord; }

    public void setChosenDefinition(String chosenDefinition) { this.chosenDefinition = chosenDefinition; }

    public void setCheckedWord(boolean pressedWord) { this.checkedWord = pressedWord; }

    public void setCheckedDefinition(boolean pressedDefinition) { this.checkedDefinition = pressedDefinition; }

    public boolean isRecord(){
        return currentPoints > record;
    }

    public int getCorrectWords() {
        return correctWords;
    }

    public void setCorrectWords(int correctWords) {
        this.correctWords = correctWords;
    }

    public boolean isNextRound(){
        return correctWords >= limitWords;
    }

    public boolean isRoundFinished() {
        return roundFinished;
    }

    public void setRoundFinished(boolean roundFinished) {
        this.roundFinished = roundFinished;
    }

    public boolean pairSelected(){
        return checkedWord && checkedDefinition;
    }

    public boolean correctPair(){
        String realDefinition = wordMap.get(chosenWord);
        return realDefinition.equals(chosenDefinition);
    }

    public void setWordMap(Map<String, String> wordMap) {
        this.wordMap = wordMap;

        setChanged();
        notifyObservers();
    }


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

    void forceUpdate() {
        setChanged();
        notifyObservers();
    }

    public boolean lackOfInfo(){
        boolean error = false;

        Iterator iterator = wordMap.entrySet().iterator();
        while (iterator.hasNext() && !error) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry.getKey() == null || entry.getValue() == null){
                error = true;
            }
        }

        return error;
    }
}
