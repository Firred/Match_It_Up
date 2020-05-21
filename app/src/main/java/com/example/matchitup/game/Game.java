package com.example.matchitup.game;

import android.widget.ToggleButton;

import com.example.matchitup.Word;

import java.io.Serializable;
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

    /**
     *  Game constructor
     * @param gameModeString String representing the game mode
     * @param record Integer representing the user's maximum punctuation in a certain level
     * @param limitWords Integer representing the maximum number of words for a certain level
     * @param lowFrecuency Integer representing the low frequency of use allowed for words in a certain level
     * @param highFrecuency Integer representing the high frequency of use allowed for words in a certain level
     * @param gameModeId String representing the game mode id
     */
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

    /**
     * Get the low frequency of use allowed for words in a certain level
     * @return Integer symbolizing the frequency
     */
    public int getLowFrecuency() {
        return lowFrecuency;
    }

    /**
     *  Get the high frequency of use allowed for words in a certain level
     * @return Integer symbolizing the frequency
     */
    public int getHighFrecuency() {
        return highFrecuency;
    }

    /**
     *  Get the number of limits words allowed in a certain level
     * @return Integer symbolizing the limit
     */
    public int getLimitWords() {
        return limitWords;
    }

    /**
     *  Get the number of current points
     * @return Integer symbolizing the points
     */
    public int getCurrentPoints() { return currentPoints; }

    /**
     *  Get the game mode in string format
     * @return String symbolizing the game mode
     */
    public String getGameModeString() { return gameModeString; }

    /**
     *  Get the chosen word by the user inside the game
     * @return String symbolizing the chosen word
     */
    public String getChosenWord() { return chosenWord; }

    /**
     *  Get the chosen definition by the user inside the game
     * @return String symbolizing the chosen definition
     */
    public String getChosenDefinition() { return chosenDefinition; }

    /**
     *  Get the game mode id in string format
     * @return String symbolizing the game mode id
     */
    public String getGameModeId() { return  gameModeId; }

    /**
     *  Check whether it's any word selected inside the game or not.
     * @return Boolean symbolizing the state.
     */
    public boolean isCheckedWord() { return checkedWord; }

    /**
     *  Check whether it's any definition selected inside the game or not.
     * @return Boolean symbolizing the state.
     */
    public boolean isCheckedDefinition() { return checkedDefinition; }

    /**
     *  Get the current words and defintions which are displayed inside the game
     * @return Map symbolizing the words with their definitions.
     */
    public Map<String, String> getWordMap() { return wordMap; }


    /**
     *  Set the first ToggleButton pressed by the user inside the game
     *  @param firstButtonPressed The toggle button which has been pressed
     */
    public void setFirstButtonPressed(ToggleButton firstButtonPressed) {
        this.firstButtonPressed = firstButtonPressed;
    }

    /**
     *  Get the first button pressed by the user
     * @return ToggleButton
     */
    public ToggleButton getFirstButtonPressed() {
        return firstButtonPressed;
    }

    /**
     *  Set the current points and notify game observers of this change
     *  @param currentPoints Integer with the points to update
     */
    public void setCurrentPoints(int currentPoints) {
        this.currentPoints = currentPoints;
        if(this.currentPoints <= 0){
            this.currentPoints = 0;
        }
        setChanged();
        notifyObservers();
    }

    /**
     *  Set the chosen word by the user
     *  @param chosenWord String with the chosen word
     */
    public void setChosenWord(String chosenWord) { this.chosenWord = chosenWord; }

    /**
     *  Set the chosen definition by the user
     *  @param chosenDefinition String with the chosen definition
     */
    public void setChosenDefinition(String chosenDefinition) { this.chosenDefinition = chosenDefinition; }

    /**
     *  Set whether it has been a word pressed or not
     *  @param pressedWord Boolean with the state
     */
    public void setCheckedWord(boolean pressedWord) { this.checkedWord = pressedWord; }

    /**
     *  Set whether it has been a definition pressed or not
     *  @param pressedDefinition Boolean with the state
     */
    public void setCheckedDefinition(boolean pressedDefinition) { this.checkedDefinition = pressedDefinition; }

    /**
     *  Check whether the record has been overcome or not
     * @return Boolean with the state
     */
    public boolean isRecord(){
        return currentPoints > record;
    }

    /**
     *  Get the number of words the user has matched correctly
     * @return Integer
     */
    public int getCorrectWords() {
        return correctWords;
    }

    /**
     *  Set the number of words the user has matched correctly
     * @param correctWords Integer representing the number
     */
    public void setCorrectWords(int correctWords) {
        this.correctWords = correctWords;
    }

    /**
     *  Check whether the game must generate new words or not
     * @return Boolean with the state
     */
    public boolean isNextRound(){
        return correctWords >= limitWords;
    }

    /**
     *  Check whether the game round has finished or not
     * @return Boolean with the state
     */
    public boolean isRoundFinished() {
        return roundFinished;
    }

    /**
     *  Set whether the game round has finished or not
     * @param roundFinished Boolean with the state
     */
    public void setRoundFinished(boolean roundFinished) {
        this.roundFinished = roundFinished;
    }

    /**
     *  Check whether the user has matched any word with any definition
     * @return Boolean with the state
     */
    public boolean pairSelected(){
        return checkedWord && checkedDefinition;
    }

    /**
     *  Check whether the user has matched correctly any word with any definition
     * @return int 1 if matched correctly, 0 if not, -1 in case the word wasn't found.
     */
    public int correctPair(){
        String realDefinition = wordMap.get(chosenWord);

        if (realDefinition != null)
            return realDefinition.equals(chosenDefinition) ? 1 : 0;
        else
            //En caso de que entre en la función si el usuario pulsa demasiado rápido
            // y la palabra haya sido borrada.
            return -1;
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

    /**
     * Force the game's update and notify observers to make changes
     */
    void forceUpdate() {
        setChanged();
        notifyObservers();
    }

    /**
     * Check whether the information to be displayed on the screen is complete.
     * The API's calls may fail if a certain number of calls has been exceeded
     * @return Boolean representing the state
     */
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
