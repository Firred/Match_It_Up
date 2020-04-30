package com.example.matchitup.game;

public interface GameFactoryInterface {

    Game easyGame(int record);
    Game mediumGame(int record);
    Game hardGame(int record);

}
