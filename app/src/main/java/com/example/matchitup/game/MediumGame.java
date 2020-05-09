package com.example.matchitup.game;

import com.example.matchitup.DictionaryService;

import java.util.HashMap;
import java.util.List;

public class MediumGame extends Game {

    public MediumGame(String gameModeString, int record, int limitWords, int lowFrecuency, int highFrecuency) {
        super(gameModeString, record, limitWords, lowFrecuency, highFrecuency, "medium");
    }

    /*
    @Override
    protected List<String> getWords() {
        return DictionaryService.getRandomWords(4, 5000, 12000);
    }
*/

}
