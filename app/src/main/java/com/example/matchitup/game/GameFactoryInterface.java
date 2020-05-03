package com.example.matchitup.game;

public interface GameFactoryInterface {

    Game easyGame(String gameModeString, int record);
    Game mediumGame(String gameModeString, int record);
    Game hardGame(String gameModeString, int record);

}
