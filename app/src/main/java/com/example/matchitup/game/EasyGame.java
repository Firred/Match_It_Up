package com.example.matchitup.game;

import com.example.matchitup.DictionaryService;

import java.util.HashMap;
import java.util.List;

public class EasyGame extends Game {

    public EasyGame(int record) {
        super(record);
    }

    /*
    @Override
    protected List<String> getWords() {
        return DictionaryService.getRandomWords(3, 12000, -1);
    }
*/

    @Override
    protected boolean saveRecord(int record) {
        return false;
    }
}
