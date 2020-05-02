package com.example.matchitup.game;

import com.example.matchitup.DictionaryService;

import java.util.HashMap;
import java.util.List;

public class HardGame extends Game {

    public HardGame(int record) {
        super(record);
    }


/*    @Override
    protected List<String> getWords() {
        return DictionaryService.getRandomWords(5, 0, 5000);
    }
*/

    @Override
    protected boolean saveRecord(int record) {
        return false;
    }
}
