package com.example.matchitup.game;

import com.example.matchitup.DictionaryService;

import java.util.HashMap;
import java.util.List;

public class EasyGame extends Game {

    public EasyGame(String gameModeString, int record, int limitWords, int lowFrecuency, int highFrecuency) {
        super(gameModeString, record, limitWords, lowFrecuency, highFrecuency);
    }

    /*
    @Override
    protected List<String> getWords() {
        return DictionaryService.getRandomWords(3, 12000, -1);
    }
*/

}
