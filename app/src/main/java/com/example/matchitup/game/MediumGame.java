package com.example.matchitup.game;

import com.example.matchitup.DictionaryService;

import java.util.HashMap;
import java.util.List;

public class MediumGame extends Game {

    public MediumGame(int record) {
        super(record);
    }

    @Override
    protected List<String> getWords() {
        return DictionaryService.getRandomWords(4, 5000, 12000);
    }

    @Override
    protected boolean saveRecord(int record) {
        return false;
    }
}
