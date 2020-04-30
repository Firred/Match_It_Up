package com.example.matchitup.game;

import android.content.SharedPreferences;

public class GameFactory implements GameFactoryInterface {

    @Override
    public Game easyGame(int record) {
        return new EasyGame(record);
    }

    @Override
    public Game mediumGame(int record) {
        return new MediumGame(record);
    }

    @Override
    public Game hardGame(int record) {
        return new HardGame(record);
    }
}
