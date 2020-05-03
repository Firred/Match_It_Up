package com.example.matchitup.game;

import android.content.SharedPreferences;

import com.example.matchitup.R;

public class GameFactory implements GameFactoryInterface {

    @Override
    public Game easyGame(String gameModeString, int record) {
        return new EasyGame(gameModeString, record, 3, 12000, -1 );
    }

    @Override
    public Game mediumGame(String gameModeString, int record) {
        return new MediumGame(gameModeString, record, 4, 5000, 12000);
    }

    @Override
    public Game hardGame(String gameModeString, int record) {
        return new HardGame(gameModeString, record, 5, 0, 5000);
    }
}
