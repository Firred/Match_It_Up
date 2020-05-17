package com.example.matchitup.game;

public class EasyGame extends Game {

    /**
     * Easy Game Constructor
     * @param gameModeString game mode in string format
     * @param record User's maximum punctuation
     * @param limitWords limits words allowed in a certain level
     * @param lowFrecuency low frequency of use allowed for words in a certain level
     * @param highFrecuency high frequency of use allowed for words in a certain level
     */
    public EasyGame(String gameModeString, int record, int limitWords, int lowFrecuency, int highFrecuency) {
        super(gameModeString, record, limitWords, lowFrecuency, highFrecuency, "easy");
    }
}
