package com.example.matchitup.game;

import com.example.matchitup.DictionaryService;

import java.util.HashMap;
import java.util.List;

public class HardGame extends Game {

    public HardGame(String gameModeString, int record, int limitWords, int lowFrecuency, int highFrecuency) {
        super(gameModeString, record, limitWords, lowFrecuency, highFrecuency, "hard");
    }


/*    @Override
    protected List<String> getWords() {
        return DictionaryService.getRandomWords(5, 0, 5000);
    }
*/
}
